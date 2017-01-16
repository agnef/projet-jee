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
			return null;

		}
		

	}

	public String delete(long userId) {
		System.out.println(userId);
		appUserService.deleteById(userId);
		return Pages.list_user;
	}

}
