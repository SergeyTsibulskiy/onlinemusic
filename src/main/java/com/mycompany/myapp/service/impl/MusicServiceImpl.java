package com.mycompany.myapp.service.impl;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.mycompany.myapp.domain.Album;
import com.mycompany.myapp.domain.Artist;
import com.mycompany.myapp.domain.Genre;
import com.mycompany.myapp.repository.ArtistRepository;
import com.mycompany.myapp.repository.GenreRepository;
import com.mycompany.myapp.repository.search.MusicSearchRepository;
import com.mycompany.myapp.service.*;
import com.mycompany.myapp.domain.Music;
import com.mycompany.myapp.repository.MusicRepository;
import com.mycompany.myapp.repository.search.MusicSearchRepository;
import com.mycompany.myapp.utils.RegexpUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mycompany.myapp.utils.RegexpUtils.getImageFormat;
import static com.mycompany.myapp.utils.StreamUtil.saveImage;
import static com.mycompany.myapp.utils.StreamUtil.stream2file;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Music.
 */
@Service
@Transactional
public class MusicServiceImpl implements MusicService{

    private final Logger log = LoggerFactory.getLogger(MusicServiceImpl.class);

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private GoogleDriveService driveService;

    @Inject
    private GenreRepository genreRepository;

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
        saveArtist(music);
        saveAlbum(music);
        saveGenres(music);
        Music result = musicRepository.save(music);
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

    @Override
    @SuppressWarnings("unchecked")
    public List<Music> search(String title, String album, String genre) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery cqry = cb.createQuery();
        Root<Music> root = cqry.from(Music.class);
        Join<Music, Genre> genresTable = root.join("genres");
        Join<Music, Album> albumTable = root.join("album");
        Predicate pTitle = cb.like(root.get("title"), "%" + title + "%");
        Predicate pGenre = cb.equal(genresTable.get("name"), genre);
        Predicate pAlbum = cb.equal(albumTable.get("name"), album);
        Predicate finalPredicate = null;
        if (!title.isEmpty()) {
            finalPredicate = pTitle;
        }

        if (!genre.isEmpty()) {
            if (finalPredicate == null) {
                finalPredicate = pGenre;
            } else {
                finalPredicate = cb.and(finalPredicate, pGenre);
            }
        }

        if (!album.isEmpty()) {
            if (finalPredicate == null) {
                finalPredicate = pAlbum;
            } else {
                finalPredicate = cb.and(finalPredicate, pAlbum);
            }
        }

        cqry.select(root).where(finalPredicate);

        return manager.createQuery(cqry).getResultList();
    }


    public Music getMetaData(File file) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3file = new Mp3File(file);
        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            Music music = new Music();
            String title = id3v2Tag.getTitle().replaceAll(" ", "_").replaceAll("/", "");
            music.setTitle(title);
            music.setAlbum(new Album(id3v2Tag.getAlbum()));
            music.setArtist(new Artist(id3v2Tag.getArtist()));
            music.setGenres(new HashSet<>(Collections.singletonList(new Genre(id3v2Tag.getGenreDescription()))));
            music.setYear(id3v2Tag.getYear() != null ? Integer.parseInt(id3v2Tag.getYear()) : 1970);
            music.setDuration((int) mp3file.getLengthInMilliseconds());

            byte[] albumImageData = id3v2Tag.getAlbumImage();
            if (albumImageData != null) {
                String imageFormat = getImageFormat(id3v2Tag.getAlbumImageMimeType());
                String imageUrl = saveImage(title, imageFormat, albumImageData);
                music.setPosterUrl(imageUrl);
            }

            return music;
        }

        return null;
    }


    public void saveMusic(String name, String contentType, InputStream inputStream) {
        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            File tempFile = File.createTempFile("tmp", "mp3", null);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(bytes);
            Music music = this.getMetaData(tempFile);

            if (null != music) {
                music.setHead(name);
                if (!isExist(music)) {
                    com.google.api.services.drive.model.File savedFile = driveService.uploadFile(name, contentType, new FileInputStream(tempFile));
                    music.setCloudId(savedFile.getId());
                    music.setDownloadUrl(savedFile.getWebContentLink());
                    this.save(music);
                } else {
                    log.warn("This:" + name + " song is exist");
                }
            } else {
                log.warn("This:" + name + " song has wrong metadata");
            }
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }
    }


    private void saveArtist(Music music) {
        Artist existingArtist = artistService.findByName(music.getArtist().getName());
        if (null != existingArtist) {
            music.setArtist(existingArtist);
        } else {
            artistService.save(music.getArtist());
        }
    }

    private void saveAlbum(Music music) {
        Album existingAlbum = albumService.findByName(music.getAlbum().getName());
        if (null != existingAlbum) {
            music.setAlbum(existingAlbum);
        } else {
            albumService.save(music.getAlbum());
        }
    }

    private void saveGenres(Music music) {
        Set<Genre> genres = music.getGenres();
        Set<Genre> processedGenres = new HashSet<>();
        for (Genre genre : genres) {
            Genre exitGenre = genreService.findByName(genre.getName());
            if (exitGenre != null) {
                processedGenres.add(exitGenre);
            } else {
                genreService.save(genre);
                processedGenres.add(genre);
            }
        }
        music.setGenres(processedGenres);
    }

    private boolean isExist(Music music) {
        return musicRepository.findByTitle(music.getTitle()) != null;
    }
}
