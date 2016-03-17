package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Artist.
 */
public interface ArtistService {

    /**
     * Save a artist.
     * @return the persisted entity
     */
    public Artist save(Artist artist);

    /**
     *  get all the artists.
     *  @return the list of entities
     */
    public Page<Artist> findAll(Pageable pageable);

    /**
     *  get the "id" artist.
     *  @return the entity
     */
    public Artist findOne(Long id);

    /**
     *  delete the "id" artist.
     */
    public void delete(Long id);

    /**
     * search for the artist corresponding
     * to the query.
     */
    public List<Artist> search(String query);

    public Artist findByName(String name);
}
