package com.akillisehir.gezirehberi.nosql.repository;

import com.akillisehir.gezirehberi.nosql.document.RecentSearchDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecentSearchRepository extends MongoRepository<RecentSearchDocument, String> {
}