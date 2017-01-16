package fr.uga.miashs.album.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;;



/*
 * On a choisi ApplicationScoped car une seule instance de chaque service suffit � l'application
 * Ce choix de reporte sur toute les sous classes
 * Si on ne met rien @RequestScoped est choisi par d�faut
 */
@ApplicationScoped
public abstract class JpaService<K,V> implements GenericService<K,V>, Serializable {

	private Class<V> cls;
	
	//point d'entree jpa
	@PersistenceUnit( unitName = "EssaiJPA" )
	private EntityManagerFactory emf;
	
	//pour chaque transaction, on pourra faire des requetes sur a base de donnees
	private static EntityManager em=null;
	
	public JpaService() {
		cls= (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	@PostConstruct
	public void init () {
		em = emf.createEntityManager();
	}
	
	@PreDestroy
	public void close() {
		em.close();
	}
	
	protected EntityManager getEm() {
		return em;
	}
	
	public void create(V v) throws ServiceException {
		EntityTransaction t = em.getTransaction();
		t.begin();
		em.persist(v);
		t.commit();

	}
	
	public V read(K id) {
		return em.find(cls, id);
//		cls= classe de l'objet qu'on veut recuperer
	}
	
	public V update(V v) {
		EntityTransaction t = em.getTransaction();
		t.begin();
		V res = em.merge(v);
		t.commit();
		return res;
	}
	
	public void delete(V v) {
		EntityTransaction t = em.getTransaction();
		t.begin();
		em.remove(v);
		t.commit();
	}
	
	public void deleteById(K id) {
		System.out.println(id);

		EntityTransaction t = em.getTransaction();
		t.begin();
		// getReference (contrairement � find) permet de charger seulement l'id
		// et non toutes les donn�es de l'objet
		em.remove(em.getReference(cls, id));
		t.commit();
	}
}
