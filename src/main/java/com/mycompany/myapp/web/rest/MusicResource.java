package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Music;
import com.mycompany.myapp.service.GoogleDriveService;
import com.mycompany.myapp.service.MusicService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Music.
 */
@RestController
@RequestMapping("/api")
public class MusicResource {

    private final Logger log = LoggerFactory.getLogger(MusicResource.class);

    @Inject
    private MusicService musicService;

    /**
     * POST  /musics -> Create a new music.
     */
    @RequestMapping(value = "/musics",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Music> createMusic(@Valid @RequestBody Music music) throws URISyntaxException {
        log.debug("REST request to save Music : {}", music);
        if (music.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("music", "idexists", "A new music cannot already have an ID")).body(null);
        }
        Music result = musicService.save(music);
        return ResponseEntity.created(new URI("/api/musics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("music", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /musics -> Updates an existing music.
     */
    @RequestMapping(value = "/musics",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Music> updateMusic(@Valid @RequestBody Music music) throws URISyntaxException {
        log.debug("REST request to update Music : {}", music);
        if (music.getId() == null) {
            return createMusic(music);
        }
        Music result = musicService.save(music);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("music", music.getId().toString()))
            .body(result);
    }

    /**
     * GET  /musics -> get all the musics.
     */
    @RequestMapping(value = "/musics",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Music>> getAllMusics(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Musics");
        Page<Music> page = musicService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/musics");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /musics/:id -> get the "id" music.
     */
    @RequestMapping(value = "/musics/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Music> getMusic(@PathVariable Long id) {
        log.debug("REST request to get Music : {}", id);
        Music music = musicService.findOne(id);
        return Optional.ofNullable(music)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /musics/:id -> delete the "id" music.
     */
    @RequestMapping(value = "/musics/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMusic(@PathVariable Long id) {
        log.debug("REST request to delete Music : {}", id);
        musicService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("music", id.toString())).build();
    }

    /**
     * SEARCH  /_search/musics/:query -> search for the music corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/musics/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Music> searchMusics(@PathVariable String query) {
        log.debug("Request to search Musics for query {}", query);
        return musicService.search(query);
    }

    /**
     * Test  /musics/search -> test search.
     */
    @RequestMapping(value = "/musics/search",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<Music>> searchMusic(@RequestParam(required = false) String query, @RequestParam String album, @RequestParam String genre) {
        List<Music> musics = musicService.search(query, "", album, genre);

        return new ResponseEntity<>(musics, HttpStatus.OK);
    }
}
