package fr.uga.miashs.album.control;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.service.AppUserService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

/*
 * L'annotation @Named permet de crÃ©er un bean CDI
 * Le bean CDI va remplacer les ManagedBean de JSF Ã  partir de JSF 2.3
 * Leur intÃ©ret est qu'ils sont utilisables en dehors du contexte JSF.
 * On peut les utiliser aussi via l'annotation @Inject
 * Il faut faire attention que l'annotation @RequestScoped vienne bien du package 
 * javax.enterprise.context et non de l'ancien package javax.faces.bean
 */
//@Named une ou plusieurs istances de cet objet va etre créé
//@RequstScoped par défaut, la duree de vie d'un objet de appusercontroller sera la requete
//possible de mettre @conversationScoped
@Named
@RequestScoped
public class AppUserController implements Serializable {

	@Inject
	private AppUserService appUserService;

	@Inject
	private AppUserSession appUserSession;

	private AppUser user;

	public AppUserController() {
		user = new AppUser();
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	// methode appellé qd le form sera validé
	public String create(){
		try {
			appUserService.create(user);
			appUserSession.setEmail(user.getEmail());
			appUserSession.setPassword(user.getPassword());
			return appUserSession.login();

		} catch (ServiceException e) {
//			FacesContext facesContext = FacesContext.getCurrentInstance();
//			FacesMessage facesMessage = new FacesMessage(e.getLocalizedMessage());
//			// le message sera affiché à l'utilisateur
//			facesContext.addMessage("email", facesMessage);
			return null;

			//// FacesContext facesContext = FacesContext.getCurrentInstance();
			//// FacesMessage facesMessage = new
			//// FacesMessage(FacesMessage.SEVERITY_WARN,
			//// e.getLocalizedMessage(),"");
			//// facesContext.addMessage(null, facesMessage);
			//// return null;
		}
		

	}

	public String delete(long userId) {
		System.out.println(userId);
		appUserService.deleteById(userId);
		return Pages.list_user;
	}

}
