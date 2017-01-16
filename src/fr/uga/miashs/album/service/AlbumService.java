package fr.uga.miashs.album.service;

import java.util.List;

import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;


public class AlbumService extends JpaService<Long,Album> {
	

	public void create(Album a) throws ServiceException {
		a.setOwner(getEm().merge(a.getOwner()));
		super.create(a);
	}
	
	public void delete(Album a) {
		super.deleteById(a.getId());
	}
	
	public List<Album> listAlbumOwnedBy(AppUser a) throws ServiceException {
		Query query = getEm().createNamedQuery("Album.findAllOwned");
		query.setParameter("owner", getEm().merge(a));
		return query.getResultList();
	}
}
