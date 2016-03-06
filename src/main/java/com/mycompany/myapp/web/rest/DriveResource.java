package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.api.services.drive.model.File;
import com.mycompany.myapp.service.GoogleDriveService;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Drive.
 */
@RestController
public class DriveResource {

    private final Logger log = LoggerFactory.getLogger(DriveResource.class);

    @Inject
    private GoogleDriveService driveService;

    /**
     * GET  /musics -> get all the musics.
     */
    @RequestMapping(value = "/files",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity getAllMusics() throws URISyntaxException {
        log.debug("REST request to get a page of Files");
        try {
            driveService.getFilesFromFolder("0ByyuEB-HD5xibFVtQ1NjOTZlZG8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
