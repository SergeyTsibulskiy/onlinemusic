package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Music.
 */
public interface MusicService {

    /**
     * Save a music.
     * @return the persisted entity
     */
    public Music save(Music music);

    /**
     *  get all the musics.
     *  @return the list of entities
     */
    public Page<Music> findAll(Pageable pageable);

    /**
     *  get the "id" music.
     *  @return the entity
     */
    public Music findOne(Long id);

    /**
     *  delete the "id" music.
     */
    public void delete(Long id);

    /**
     * search for the music corresponding
     * to the query.
     */
    public List<Music> search(String query);
}
