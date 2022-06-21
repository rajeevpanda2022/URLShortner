package com.url.shortening.repository;

import com.url.shortening.entity.URLData;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * URL Repository to work with mongo
 */
public interface URLRepository extends MongoRepository<URLData, Long> {

    URLData findFirstByHash(String hash);
}
