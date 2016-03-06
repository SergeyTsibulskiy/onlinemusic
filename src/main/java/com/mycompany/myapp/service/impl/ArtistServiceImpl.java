package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ArtistService;
import com.mycompany.myapp.domain.Artist;
import com.mycompany.myapp.repository.ArtistRepository;
import com.mycompany.myapp.repository.search.ArtistSearchRepository;
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
 * Service Implementation for managing Artist.
 */
@Service
@Transactional
public class ArtistServiceImpl implements ArtistService{

    private final Logger log = LoggerFactory.getLogger(ArtistServiceImpl.class);
    
    @Inject
    private ArtistRepository artistRepository;
    
    @Inject
    private ArtistSearchRepository artistSearchRepository;
    
    /**
     * Save a artist.
     * @return the persisted entity
     */
    public Artist save(Artist artist) {
        log.debug("Request to save Artist : {}", artist);
        Artist result = artistRepository.save(artist);
        artistSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the artists.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Artist> findAll(Pageable pageable) {
        log.debug("Request to get all Artists");
        Page<Artist> result = artistRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one artist by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Artist findOne(Long id) {
        log.debug("Request to get Artist : {}", id);
        Artist artist = artistRepository.findOne(id);
        return artist;
    }

    /**
     *  delete the  artist by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Artist : {}", id);
        artistRepository.delete(id);
        artistSearchRepository.delete(id);
    }

    /**
     * search for the artist corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Artist> search(String query) {
        
        log.debug("REST request to search Artists for query {}", query);
        return StreamSupport
            .stream(artistSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
