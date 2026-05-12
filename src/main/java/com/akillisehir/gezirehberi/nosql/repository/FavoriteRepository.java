package com.akillisehir.gezirehberi.nosql.repository;

import com.akillisehir.gezirehberi.nosql.document.FavoriteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FavoriteRepository extends MongoRepository<FavoriteDocument, String> {

    List<FavoriteDocument> findByUserId(Long userId);

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);
}