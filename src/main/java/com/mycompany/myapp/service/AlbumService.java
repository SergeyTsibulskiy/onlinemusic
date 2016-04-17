package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Album.
 */
public interface AlbumService {

    /**
     * Save a album.
     * @return the persisted entity
     */
    public Album save(Album album);

    /**
     *  get all the albums.
     *  @return the list of entities
     */
    public Page<Album> findAll(Pageable pageable);

    /**
     *  get the "id" album.
     *  @return the entity
     */
    public Album findOne(Long id);

    /**
     *  delete the "id" album.
     */
    public void delete(Long id);

    public Album findByName(String name);
}
