package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Genre.
 */
public interface GenreService {

    /**
     * Save a genre.
     * @return the persisted entity
     */
    public Genre save(Genre genre);

    /**
     *  get all the genres.
     *  @return the list of entities
     */
    public Page<Genre> findAll(Pageable pageable);

    /**
     *  get the "id" genre.
     *  @return the entity
     */
    public Genre findOne(Long id);

    /**
     *  delete the "id" genre.
     */
    public void delete(Long id);

    /**
     * search for the genre corresponding
     * to the query.
     */
    public List<Genre> search(String query);
}
