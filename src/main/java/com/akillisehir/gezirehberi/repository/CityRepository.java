package com.akillisehir.gezirehberi.repository;

import com.akillisehir.gezirehberi.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}