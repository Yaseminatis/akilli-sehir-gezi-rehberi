package com.akillisehir.gezirehberi.service.abstracts;

import com.akillisehir.gezirehberi.entity.City;

import java.util.List;

public interface CityService {

    List<City> getAllCities();

    City getCityById(Long id);

    City createCity(City city);
}