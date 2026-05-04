package com.akillisehir.gezirehberi.controller;

import com.akillisehir.gezirehberi.dto.PlaceDto;
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
    public List<PlaceDto> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{id}")
    public PlaceDto getPlaceById(@PathVariable Long id) {
        return placeService.getPlaceById(id);
    }

    @GetMapping("/city/{cityId}")
    public List<PlaceDto> getPlacesByCity(@PathVariable Long cityId) {
        return placeService.getPlacesByCity(cityId);
    }

    @GetMapping("/category")
    public List<PlaceDto> getPlacesByCategory(@RequestParam PlaceCategory category) {
        return placeService.getPlacesByCategory(category);
    }

    @PostMapping
    public PlaceDto createPlace(@RequestBody PlaceDto placeDto) {
        return placeService.createPlace(placeDto);
    }

    @PutMapping("/{id}")
    public PlaceDto updatePlace(@PathVariable Long id, @RequestBody PlaceDto placeDto) {
        return placeService.updatePlace(id, placeDto);
    }

    @DeleteMapping("/{id}")
    public void deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
    }
}