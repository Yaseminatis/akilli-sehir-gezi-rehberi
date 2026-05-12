package com.akillisehir.gezirehberi.controller;

import com.akillisehir.gezirehberi.dto.ApiResponse;
import com.akillisehir.gezirehberi.dto.PlaceDto;
import com.akillisehir.gezirehberi.enums.PlaceCategory;
import com.akillisehir.gezirehberi.service.abstracts.PlaceService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<PlaceDto>>> getAllPlaces() {
        return ResponseEntity.ok(
                ApiResponse.success("Mekanlar başarıyla listelendi.", placeService.getAllPlaces())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaceDto>> getPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Mekan başarıyla getirildi.", placeService.getPlaceById(id))
        );
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> getPlacesByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(
                ApiResponse.success("Şehre ait mekanlar başarıyla listelendi.", placeService.getPlacesByCity(cityId))
        );
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> getPlacesByCategory(@RequestParam PlaceCategory category) {
        return ResponseEntity.ok(
                ApiResponse.success("Kategoriye ait mekanlar başarıyla listelendi.", placeService.getPlacesByCategory(category))
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PlaceDto>>> searchPlaces(@RequestParam String keyword) {
        return ResponseEntity.ok(
                ApiResponse.success("Arama sonuçları başarıyla listelendi.", placeService.searchPlaces(keyword))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlaceDto>> createPlace(@RequestBody PlaceDto placeDto) {
        return ResponseEntity.ok(
                ApiResponse.success("Mekan başarıyla oluşturuldu.", placeService.createPlace(placeDto))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaceDto>> updatePlace(@PathVariable Long id, @RequestBody PlaceDto placeDto) {
        return ResponseEntity.ok(
                ApiResponse.success("Mekan başarıyla güncellendi.", placeService.updatePlace(id, placeDto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);

        return ResponseEntity.ok(
                ApiResponse.success("Mekan başarıyla silindi.", null)
        );
    }
}