package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cascade;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Music.
 */
@Entity
@Table(name = "music")
@Document(indexName = "music")
public class Music implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "head")
    private String head;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Min(value = 1970)
    @Max(value = 2100)
    @Column(name = "year")
    private Integer year;

    @Column(name = "comment")
    private String comment;

    @Column(name = "cloud_id")
    private String cloudId;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "duration")
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToMany
    @JoinTable(name = "music_genre",
               joinColumns = @JoinColumn(name="musics_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="genres_id", referencedColumnName="ID"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Music music = (Music) o;
        if(music.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, music.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Music{" +
            "id=" + id +
            ", head='" + head + "'" +
            ", title='" + title + "'" +
            ", year='" + year + "'" +
            ", comment='" + comment + "'" +
            ", cloudId='" + cloudId + "'" +
            ", posterUrl='" + posterUrl + "'" +
            ", downloadUrl='" + downloadUrl + "'" +
            ", duration='" + duration + "'" +
            '}';
    }
}
