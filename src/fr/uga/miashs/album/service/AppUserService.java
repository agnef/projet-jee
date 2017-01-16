package fr.uga.miashs.album.service;

import java.util.Collections;
import java.util.List;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import fr.uga.miashs.album.model.AppUser;

@Named
@ApplicationScoped
public class AppUserService extends JpaService<Long,AppUser> {

	@Override
	public void create(AppUser v) throws ServiceException {
		try {
			super.create(v);
		}
		catch (RollbackException e) {
			//la transaction peut etre annulée si l'email existe deja ds la bd
			if (e.getCause() instanceof EntityExistsException) {
				throw new ServiceException("Un utilisateur avec l'email "+v.getEmail()+" existe deja ",e);
			}
			else {
				throw new ServiceException(e);

			}
		}
	}

	public AppUser login(String email, String password) throws ServiceException {
		// a un mot de passe, on veut recuperer l'instance de 'utilisateur qui a ce password
		Query query = getEm().createNamedQuery("AppUser.login");
		query.setParameter("email", email);
		query.setParameter("password", password);
		try {
			return (AppUser) query.getSingleResult();
			//query.getResultList() si plusiuers res
			
		}
		catch (NoResultException e) {
			throw new ServiceException("Utilisateur Inconnu",e);
		}
	}
	
	public List<AppUser> getListUsers() {
		 Query query = getEm().createNamedQuery("AppUser.findAll");
		 try {
		 return query.getResultList();
		 }
		 catch (Exception e) {
			 e.printStackTrace();
			 return Collections.emptyList();
		 }
		 
	}
}
