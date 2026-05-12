package com.akillisehir.gezirehberi.repository;

import com.akillisehir.gezirehberi.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByPlaceId(Long placeId);
}