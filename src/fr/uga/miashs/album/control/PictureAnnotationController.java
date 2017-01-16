package fr.uga.miashs.album.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.PictureAnnotationService;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;

@Named
@SessionScoped
public class PictureAnnotationController implements Serializable {

	// currentPicture, propriete, valeurPropriete : triplet rdf
	private String currentPicture;
	private String propriete;
	private String valeurPropriete;
	private String pictureUri;
	
	private String requete;
	
	@Inject
	private PictureAnnotationService pictureAnnotationService;
	
	//getters & setters
	
	public String getCurrentPicture(){
		System.out.println("get current picture = current picture : " + currentPicture);
		return currentPicture;
	}
	
	public String getPictureUri() {
		return pictureUri;
	}

	public void setPictureUri(String pictureUri) {
		this.pictureUri = pictureUri;
	}

	public void setCurrentPicture(String picture){
		this.currentPicture = picture;
		System.out.println("set current picture = current picture " + picture);
	}
	
	public String getRequete() {
		return requete;
	}

	public void setRequete(String requete) {
		this.requete = requete;
	}

	public String getPropriete() {
		return propriete;
	}

	public void setPropriete(String propriete) {
		this.propriete = propriete;
		System.out.println("set propriété : " + propriete);
	}

	public String getValeurPropriete() {
		return valeurPropriete;
	}

	public void setValeurPropriete(String valeurPropriete) {
		this.valeurPropriete = valeurPropriete;
		System.out.println("set valeur de la propriété : " + valeurPropriete);
	}
	
	// queries
	
	public List<String> getQueryPictureResult(){	
		System.out.println("requete : " + requete);
		List<String> pictName = new ArrayList<String>();
		List<Picture> pictures = pictureAnnotationService.queries(requete);
		for(int i=0; i<pictures.size(); i++){
			pictName.add(pictures.get(i).getFileName());
			System.out.println(pictName.get(i));
		}
		return pictName;

	}
	
	public void insertOntology(){
		System.out.println("premiere" + valeurPropriete);
		if(propriete.equals("pictureDate")){
			valeurPropriete = valeurPropriete + "T00:00:00";
		}
		System.out.println("modif" + valeurPropriete);
		
		pictureAnnotationService.insertOntology(currentPicture, propriete, valeurPropriete);
	}
	
	 public List<String> seeAnnotations(){
	        System.out.println("ANNOTATIONS : pictureannotationcontroller");
	        List<String> annotations = pictureAnnotationService.seeAnnotations(pictureUri);
	        List<String> displayAnnotations = new ArrayList<String>();
	        for (int i=0; i<annotations.size(); i++){
	        	if (annotations.get(i).equals("http://www.semanticweb.org/masterDCISS/projetAlbum#hasInside"))
	        		displayAnnotations.add("représente : ");
	        	else if (annotations.get(i).equals("http://www.semanticweb.org/masterDCISS/projetAlbum#inYear"))
	        		displayAnnotations.add("en l'année : ");
	        	else if(annotations.get(i).equals("http://www.semanticweb.org/masterDCISS/projetAlbum#isTakenBy"))
	        		displayAnnotations.add("prise par : ");
	        	else if(annotations.get(i).equals("http://www.semanticweb.org/masterDCISS/projetAlbum#isTakenIn"))
	        		displayAnnotations.add("prise à : ");
	        	else if(annotations.get(i).equals("http://www.semanticweb.org/masterDCISS/projetAlbum#during"))
	        		displayAnnotations.add("pendant : ");
	        	else if(annotations.get(i).equals("http://www.semanticweb.org/masterDCISS/projetAlbum#pictureDate"))
	        		displayAnnotations.add("prise le : ");
	        	else if (!annotations.get(i).equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") &&
	        			!annotations.get(i).equals("http://www.w3.org/2002/07/owl#Thing") &&
	        			!annotations.get(i).equals("http://www.w3.org/2000/01/rdf-schema#Resource") )
	        		displayAnnotations.add(annotations.get(i) + " ");
	        	
	        	
	        }
	            
	        return displayAnnotations;
	    }
	
	
}
