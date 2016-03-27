package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.api.services.drive.model.File;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.mycompany.myapp.domain.Artist;
import com.mycompany.myapp.domain.Genre;
import com.mycompany.myapp.domain.Music;
import com.mycompany.myapp.service.GoogleDriveService;
import com.mycompany.myapp.service.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;

import static com.mycompany.myapp.utils.RegexpUtils.getImageFormat;
import static com.mycompany.myapp.utils.StreamUtil.saveImage;
import static com.mycompany.myapp.utils.StreamUtil.stream2file;

/**
 * REST controller for managing Drive.
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriveResource {

    @Autowired
    private MusicService musicService;

    @Inject
    private GoogleDriveService driveService;

    private final Logger log = LoggerFactory.getLogger(DriveResource.class);

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

            driveService.getFilesFromFolder(GoogleDriveService.FOLDER_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/drive/music/upload", method = RequestMethod.POST)
    @Timed
    @ResponseBody
    public ResponseEntity.BodyBuilder uploadFileHandler(@RequestParam("file") MultipartFile file) {
        try {
            InputStream input = file.getInputStream();
            musicService.saveMusic(file.getOriginalFilename(), file.getContentType(), input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok();
    }

    /**
     * Upload multiple file using Spring Controller
     */
    @RequestMapping(value = "drive/music/uploads", method = RequestMethod.POST)
    public @ResponseBody
    String uploadMultipleFileHandler(HttpServletRequest request) {

        String message = "";


        return message;
    }

}
