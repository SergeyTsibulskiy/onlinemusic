package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.MusicService;
import com.mycompany.myapp.domain.Music;
import com.mycompany.myapp.repository.MusicRepository;
import com.mycompany.myapp.repository.search.MusicSearchRepository;
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
 * Service Implementation for managing Music.
 */
@Service
@Transactional
public class MusicServiceImpl implements MusicService{

    private final Logger log = LoggerFactory.getLogger(MusicServiceImpl.class);
    
    @Inject
    private MusicRepository musicRepository;
    
    @Inject
    private MusicSearchRepository musicSearchRepository;
    
    /**
     * Save a music.
     * @return the persisted entity
     */
    public Music save(Music music) {
        log.debug("Request to save Music : {}", music);
        Music result = musicRepository.save(music);
        musicSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the musics.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Music> findAll(Pageable pageable) {
        log.debug("Request to get all Musics");
        Page<Music> result = musicRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one music by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Music findOne(Long id) {
        log.debug("Request to get Music : {}", id);
        Music music = musicRepository.findOneWithEagerRelationships(id);
        return music;
    }

    /**
     *  delete the  music by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Music : {}", id);
        musicRepository.delete(id);
        musicSearchRepository.delete(id);
    }

    /**
     * search for the music corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Music> search(String query) {
        
        log.debug("REST request to search Musics for query {}", query);
        return StreamSupport
            .stream(musicSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
