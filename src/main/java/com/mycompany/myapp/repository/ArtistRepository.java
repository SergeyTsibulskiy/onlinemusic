package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Artist;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Artist entity.
 */
public interface ArtistRepository extends JpaRepository<Artist,Long> {

}
