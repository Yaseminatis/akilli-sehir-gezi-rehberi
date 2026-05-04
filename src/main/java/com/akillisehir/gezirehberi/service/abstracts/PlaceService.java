package com.akillisehir.gezirehberi.service.abstracts;

import com.akillisehir.gezirehberi.dto.PlaceDto;
import com.akillisehir.gezirehberi.enums.PlaceCategory;

import java.util.List;

public interface PlaceService {

    List<PlaceDto> getAllPlaces();

    PlaceDto getPlaceById(Long id);

    List<PlaceDto> getPlacesByCity(Long cityId);

    List<PlaceDto> getPlacesByCategory(PlaceCategory category);

    PlaceDto createPlace(PlaceDto placeDto);

    PlaceDto updatePlace(Long id, PlaceDto placeDto);

    void deletePlace(Long id);
}