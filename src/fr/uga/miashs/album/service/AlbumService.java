package fr.uga.miashs.album.service;

import java.util.List;

import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;


public class AlbumService extends JpaService<Long,Album> {
	
//peut etre des erreurs à capter
	public void create(Album a) throws ServiceException {
		// je suis sûre que l'utilisateur attaché existe ds la bd
		a.setOwner(getEm().merge(a.getOwner()));
		super.create(a);
	}
	
	public void delete(Album a) {
		super.deleteById(a.getId());
	}
	
	public List<Album> listAlbumOwnedBy(AppUser a) throws ServiceException {
		//La requete est définie dans la classe Album grâce à une annotation
		Query query = getEm().createNamedQuery("Album.findAllOwned");
		query.setParameter("owner", getEm().merge(a));
		return query.getResultList();
	}
}
