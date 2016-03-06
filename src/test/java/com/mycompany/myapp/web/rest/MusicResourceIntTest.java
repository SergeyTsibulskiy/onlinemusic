package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Music;
import com.mycompany.myapp.repository.MusicRepository;
import com.mycompany.myapp.service.MusicService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MusicResource REST controller.
 *
 * @see MusicResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MusicResourceIntTest {

    private static final String DEFAULT_HEAD = "AAAAA";
    private static final String UPDATED_HEAD = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_ALBUM = "AAAAA";
    private static final String UPDATED_ALBUM = "BBBBB";

    private static final Integer DEFAULT_YEAR = 1970;
    private static final Integer UPDATED_YEAR = 1971;
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";
    private static final String DEFAULT_CLOUD_ID = "AAAAA";
    private static final String UPDATED_CLOUD_ID = "BBBBB";

    @Inject
    private MusicRepository musicRepository;

    @Inject
    private MusicService musicService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMusicMockMvc;

    private Music music;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MusicResource musicResource = new MusicResource();
        ReflectionTestUtils.setField(musicResource, "musicService", musicService);
        this.restMusicMockMvc = MockMvcBuilders.standaloneSetup(musicResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        music = new Music();
        music.setHead(DEFAULT_HEAD);
        music.setTitle(DEFAULT_TITLE);
        music.setAlbum(DEFAULT_ALBUM);
        music.setYear(DEFAULT_YEAR);
        music.setComment(DEFAULT_COMMENT);
        music.setCloudId(DEFAULT_CLOUD_ID);
    }

    @Test
    @Transactional
    public void createMusic() throws Exception {
        int databaseSizeBeforeCreate = musicRepository.findAll().size();

        // Create the Music

        restMusicMockMvc.perform(post("/api/musics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(music)))
                .andExpect(status().isCreated());

        // Validate the Music in the database
        List<Music> musics = musicRepository.findAll();
        assertThat(musics).hasSize(databaseSizeBeforeCreate + 1);
        Music testMusic = musics.get(musics.size() - 1);
        assertThat(testMusic.getHead()).isEqualTo(DEFAULT_HEAD);
        assertThat(testMusic.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMusic.getAlbum()).isEqualTo(DEFAULT_ALBUM);
        assertThat(testMusic.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testMusic.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testMusic.getCloudId()).isEqualTo(DEFAULT_CLOUD_ID);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = musicRepository.findAll().size();
        // set the field null
        music.setTitle(null);

        // Create the Music, which fails.

        restMusicMockMvc.perform(post("/api/musics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(music)))
                .andExpect(status().isBadRequest());

        List<Music> musics = musicRepository.findAll();
        assertThat(musics).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMusics() throws Exception {
        // Initialize the database
        musicRepository.saveAndFlush(music);

        // Get all the musics
        restMusicMockMvc.perform(get("/api/musics?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(music.getId().intValue())))
                .andExpect(jsonPath("$.[*].head").value(hasItem(DEFAULT_HEAD.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].album").value(hasItem(DEFAULT_ALBUM.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
                .andExpect(jsonPath("$.[*].cloudId").value(hasItem(DEFAULT_CLOUD_ID.toString())));
    }

    @Test
    @Transactional
    public void getMusic() throws Exception {
        // Initialize the database
        musicRepository.saveAndFlush(music);

        // Get the music
        restMusicMockMvc.perform(get("/api/musics/{id}", music.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(music.getId().intValue()))
            .andExpect(jsonPath("$.head").value(DEFAULT_HEAD.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.album").value(DEFAULT_ALBUM.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.cloudId").value(DEFAULT_CLOUD_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMusic() throws Exception {
        // Get the music
        restMusicMockMvc.perform(get("/api/musics/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMusic() throws Exception {
        // Initialize the database
        musicRepository.saveAndFlush(music);

		int databaseSizeBeforeUpdate = musicRepository.findAll().size();

        // Update the music
        music.setHead(UPDATED_HEAD);
        music.setTitle(UPDATED_TITLE);
        music.setAlbum(UPDATED_ALBUM);
        music.setYear(UPDATED_YEAR);
        music.setComment(UPDATED_COMMENT);
        music.setCloudId(UPDATED_CLOUD_ID);

        restMusicMockMvc.perform(put("/api/musics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(music)))
                .andExpect(status().isOk());

        // Validate the Music in the database
        List<Music> musics = musicRepository.findAll();
        assertThat(musics).hasSize(databaseSizeBeforeUpdate);
        Music testMusic = musics.get(musics.size() - 1);
        assertThat(testMusic.getHead()).isEqualTo(UPDATED_HEAD);
        assertThat(testMusic.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMusic.getAlbum()).isEqualTo(UPDATED_ALBUM);
        assertThat(testMusic.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testMusic.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testMusic.getCloudId()).isEqualTo(UPDATED_CLOUD_ID);
    }

    @Test
    @Transactional
    public void deleteMusic() throws Exception {
        // Initialize the database
        musicRepository.saveAndFlush(music);

		int databaseSizeBeforeDelete = musicRepository.findAll().size();

        // Get the music
        restMusicMockMvc.perform(delete("/api/musics/{id}", music.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Music> musics = musicRepository.findAll();
        assertThat(musics).hasSize(databaseSizeBeforeDelete - 1);
    }
}
