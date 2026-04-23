package com.akillisehir.gezirehberi.service.abstracts;

import com.akillisehir.gezirehberi.entity.Place;
import com.akillisehir.gezirehberi.enums.PlaceCategory;

import java.util.List;

public interface PlaceService {

    List<Place> getAllPlaces();

    Place getPlaceById(Long id);

    List<Place> getPlacesByCity(Long cityId);

    List<Place> getPlacesByCategory(PlaceCategory category);

    Place createPlace(Place place);
}