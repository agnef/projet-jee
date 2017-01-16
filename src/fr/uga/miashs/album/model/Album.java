package fr.uga.miashs.album.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
    @NamedQuery(name="Album.findAllOwned",
                query="SELECT a FROM Album a WHERE a.owner=:owner"),
}

		)
public class Album implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	
	@NotNull
	private String title;
	
	private String description;

	@NotNull
	@ManyToOne(cascade = CascadeType.PERSIST)
	private AppUser owner;
	
	@ManyToMany
	private Set<AppUser> sharedWith;
	
	@OneToMany(mappedBy="album", cascade = CascadeType.ALL)
	private Set<Picture> pictures;
	
	
	
	protected Album() {
	}
	
	public Album(AppUser owner) {
		this.owner=owner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AppUser getOwner() {
		return owner;
	}

	public void setOwner(AppUser owner) {
		this.owner = owner;
	}

	public long getId() {
		return id;
	}

	public Set<AppUser> getSharedWith() {
		return sharedWith;
	}

	
	
	public Set<Picture> getPictures() {
		return pictures;
	}
	
	public Set<String> getPicturesName() {
		Set<String> picturesName = new HashSet<String>();
		for (Picture p : pictures)
			picturesName.add(p.getFileName());
		return picturesName;
			
	}
	
	public AppUser shareAlbum(AppUser user){
		if(sharedWith == null)
			sharedWith = new HashSet<AppUser>();
		sharedWith.add(user);
		for (AppUser a : sharedWith)
			System.out.println("sharedWith : " + a.getFirstname());
		return user;
	}
	
	public Picture addPicture(Picture picture){
		if(pictures == null)
			pictures = new HashSet<Picture>();
		pictures.add(picture);
		return picture;
	}
	
	
}
