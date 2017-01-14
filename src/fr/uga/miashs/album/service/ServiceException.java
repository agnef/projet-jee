package fr.uga.miashs.album.service;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ServiceException extends Exception {

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);

	}

	public ServiceException(String message, Throwable e) {
		super(message, e);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesMessage facesMessages = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
		facesContext.addMessage(null, facesMessages);
	}

}
