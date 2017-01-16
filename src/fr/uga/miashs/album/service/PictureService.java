package fr.uga.miashs.album.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;


import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;

public class PictureService extends JpaService<Long, Picture>{
	
	public void createP(Picture p, Album a) throws ServiceException {
		
		getEm().getTransaction().begin();
		getEm().persist(p);
		getEm().getTransaction().commit();

		updatePicture(p, a);

	}
	
	public void updatePicture(Picture p, Album a) {
		EntityTransaction t = getEm().getTransaction();
		Album alb = getAlbumById(a.getId());
		t.begin();
		p.setPictAlbum(alb);
		t.commit();
	}
	
	public void deletePicture(long id){
		EntityTransaction t = getEm().getTransaction();
		t.begin();
		Query query = getEm().createQuery("SELECT p FROM Picture p WHERE p.id="+id);
		Picture p = (Picture) query.getSingleResult();
		getEm().remove(p);
		t.commit();
	}
	
	public List<Picture> getPictureNull(){
		// get List of pictures to delete
		List<Picture> picturesNull = new ArrayList<Picture>();
		Query query = getEm().createQuery("SELECT p FROM Picture p WHERE p.album IS NULL");
		picturesNull = query.getResultList();
		return picturesNull;
	}
	
	public Album getAlbumById(long albumId){
		Query query = getEm().createQuery("SELECT a FROM Album a WHERE a.id="+albumId);
		Album a =(Album) query.getSingleResult();
		return a;
	}
	
	public List<Picture> listPictureOwnedBy(long id) throws ServiceException {
		//request is defined in class Picture with an annotation
		Album a = getAlbumById(id);
		Query query = getEm().createNamedQuery("Picture.findAllPicture");
		query.setParameter("album", getEm().merge(a));
		return query.getResultList();
	}

}
