package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Genre.
 */
@Entity
@Table(name = "genre", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "name"}))
@Document(indexName = "genre")
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<Music> musicss = new HashSet<>();

    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Music> getMusicss() {
        return musicss;
    }

    public void setMusicss(Set<Music> musics) {
        this.musicss = musics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Genre genre = (Genre) o;
        if(genre.name == null || name == null) {
            return false;
        }
        return Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Genre{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
