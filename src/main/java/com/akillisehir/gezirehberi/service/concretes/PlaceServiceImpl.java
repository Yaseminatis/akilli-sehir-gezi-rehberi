package com.akillisehir.gezirehberi.service.concretes;

import com.akillisehir.gezirehberi.entity.Place;
import com.akillisehir.gezirehberi.enums.PlaceCategory;
import com.akillisehir.gezirehberi.repository.PlaceRepository;
import com.akillisehir.gezirehberi.service.abstracts.PlaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    @Override
    public Place getPlaceById(Long id) {
        return placeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Place> getPlacesByCity(Long cityId) {
        return placeRepository.findByCityId(cityId);
    }

    @Override
    public List<Place> getPlacesByCategory(PlaceCategory category) {
        return placeRepository.findByCategory(category);
    }

    @Override
    public Place createPlace(Place place) {
        return placeRepository.save(place);
    }
}