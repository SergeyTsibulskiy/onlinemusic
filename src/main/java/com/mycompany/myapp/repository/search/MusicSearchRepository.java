package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Music;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Music entity.
 */
public interface MusicSearchRepository extends ElasticsearchRepository<Music, Long> {
}
