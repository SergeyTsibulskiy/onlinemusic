package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.GenreService;
import com.mycompany.myapp.domain.Genre;
import com.mycompany.myapp.repository.GenreRepository;
import com.mycompany.myapp.repository.search.GenreSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Genre.
 */
@Service
@Transactional
public class GenreServiceImpl implements GenreService{

    private final Logger log = LoggerFactory.getLogger(GenreServiceImpl.class);

    @Inject
    private GenreRepository genreRepository;

    @Inject
    private GenreSearchRepository genreSearchRepository;

    /**
     * Save a genre.
     * @return the persisted entity
     */
    public Genre save(Genre genre) {
        log.debug("Request to save Genre : {}", genre);
        Genre result = genreRepository.save(genre);
        genreSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the genres.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Genre> findAll(Pageable pageable) {
        log.debug("Request to get all Genres");
        Page<Genre> result = genreRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one genre by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Genre findOne(Long id) {
        log.debug("Request to get Genre : {}", id);
        Genre genre = genreRepository.findOne(id);
        return genre;
    }

    /**
     *  delete the  genre by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Genre : {}", id);
        genreRepository.delete(id);
        genreSearchRepository.delete(id);
    }

    /**
     * search for the genre corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<Genre> search(String query) {

        log.debug("REST request to search Genres for query {}", query);
        return StreamSupport
            .stream(genreSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }
}
