package com.akillisehir.gezirehberi.repository;

import com.akillisehir.gezirehberi.entity.Place;
import com.akillisehir.gezirehberi.enums.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByCityId(Long cityId);

    List<Place> findByCategory(PlaceCategory category);
}