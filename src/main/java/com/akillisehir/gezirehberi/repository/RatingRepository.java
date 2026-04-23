package com.akillisehir.gezirehberi.repository;

import com.akillisehir.gezirehberi.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}