package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Music;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Music entity.
 */
public interface MusicRepository extends JpaRepository<Music,Long> {

    @Query("select distinct music from Music music left join fetch music.genres")
    List<Music> findAllWithEagerRelationships();

    @Query("select music from Music music left join fetch music.genres where music.id =:id")
    Music findOneWithEagerRelationships(@Param("id") Long id);

    Music findByTitle(String title);
}
