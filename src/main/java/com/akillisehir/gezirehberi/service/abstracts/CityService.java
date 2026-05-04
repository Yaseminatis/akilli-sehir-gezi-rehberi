package com.akillisehir.gezirehberi.service.abstracts;

import com.akillisehir.gezirehberi.dto.CityDto;

import java.util.List;

public interface CityService {

    List<CityDto> getAllCities();

    CityDto getCityById(Long id);

    CityDto createCity(CityDto cityDto);

    CityDto updateCity(Long id, CityDto cityDto);

    void deleteCity(Long id);
}