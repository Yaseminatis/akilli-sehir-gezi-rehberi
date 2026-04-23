package com.akillisehir.gezirehberi.controller;

import com.akillisehir.gezirehberi.entity.Place;
import com.akillisehir.gezirehberi.enums.PlaceCategory;
import com.akillisehir.gezirehberi.service.abstracts.PlaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{id}")
    public Place getPlaceById(@PathVariable Long id) {
        return placeService.getPlaceById(id);
    }

    @GetMapping("/city/{cityId}")
    public List<Place> getPlacesByCity(@PathVariable Long cityId) {
        return placeService.getPlacesByCity(cityId);
    }

    @GetMapping("/category")
    public List<Place> getPlacesByCategory(@RequestParam PlaceCategory category) {
        return placeService.getPlacesByCategory(category);
    }

    @PostMapping
    public Place createPlace(@RequestBody Place place) {
        return placeService.createPlace(place);
    }
}