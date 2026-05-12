package com.akillisehir.gezirehberi.nosql.repository;

import com.akillisehir.gezirehberi.nosql.document.TravelPlanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TravelPlanRepository extends MongoRepository<TravelPlanDocument, String> {

    List<TravelPlanDocument> findByUserId(Long userId);
}