package fr.uga.miashs.album.service;

public interface GenericService<K, V> {
	// K type de la clé, V type de donnees a memrier

	public void create(V v) throws ServiceException;
	public V read(K id) throws ServiceException;
	public void delete(V v) throws ServiceException;
	public void deleteById(K id) throws ServiceException;
	
	
	
	
}
