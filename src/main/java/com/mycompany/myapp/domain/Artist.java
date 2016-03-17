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
 * A Artist.
 */
@Entity
@Table(name = "artist")
@Document(indexName = "artist")
public class Artist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)

    private String name;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<Music> musikss = new HashSet<>();

    public Artist() {
    }

    public Artist(String name) {
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

    public Set<Music> getMusikss() {
        return musikss;
    }

    public void setMusikss(Set<Music> musics) {
        this.musikss = musics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Artist artist = (Artist) o;
        if(artist.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, artist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Artist{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
