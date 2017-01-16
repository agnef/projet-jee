package fr.uga.miashs.album.model;
import java.net.URI;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.CascadeType;
import javax.validation.constraints.NotNull;

import org.primefaces.model.StreamedContent;

import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;

@Entity
@NamedQueries({
    @NamedQuery(name="Picture.findAllPicture",
                query="SELECT p FROM Picture p WHERE p.album=:album"),
}

		)
public class Picture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Album album;
	
	@NotNull
	private String title;
	
	@NotNull
	private URI uri;
	//fait le lien avec le web semantique
	
	@NotNull
	private String localfile;
	
	@NotNull
	private String filename;
	
	public Picture(){
	}
	

	public Picture(String titre, URI chemin, String cheminLocal, String nomfichier) {
		title = titre;
		uri = chemin;
		localfile = cheminLocal;
		filename = nomfichier;
	}

	public URI getUri(){
		return this.uri;
	}
	
	public String getLocalfile() {
		return localfile;
	}
	
	public Album getPictAlbum(){
		return album;
	}
	
	public String getFileName(){
		return filename;
	}
	
	public long getId(){
		return id;
	}
	
	public void setPictAlbum(Album album){
		this.album = album;
	}
	
	public void setPath(String p){
		localfile=p;
	}
	
}
