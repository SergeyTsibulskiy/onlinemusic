package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.AlbumService;
import com.mycompany.myapp.domain.Album;
import com.mycompany.myapp.repository.AlbumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Album.
 */
@Service
@Transactional
public class AlbumServiceImpl implements AlbumService{

    private final Logger log = LoggerFactory.getLogger(AlbumServiceImpl.class);

    @Inject
    private AlbumRepository albumRepository;

    /**
     * Save a album.
     * @return the persisted entity
     */
    public Album save(Album album) {
        log.debug("Request to save Album : {}", album);
        Album result = albumRepository.save(album);
        return result;
    }

    /**
     *  get all the albums.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Album> findAll(Pageable pageable) {
        log.debug("Request to get all Albums");
        Page<Album> result = albumRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one album by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Album findOne(Long id) {
        log.debug("Request to get Album : {}", id);
        Album album = albumRepository.findOne(id);
        return album;
    }

    /**
     *  delete the  album by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Album : {}", id);
        albumRepository.delete(id);
    }

    @Override
    public Album findByName(String name) {
        return albumRepository.findByName(name);
    }
}
