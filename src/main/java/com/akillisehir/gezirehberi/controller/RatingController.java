package com.akillisehir.gezirehberi.controller;

import com.akillisehir.gezirehberi.dto.ApiResponse;
import com.akillisehir.gezirehberi.dto.RatingDto;
import com.akillisehir.gezirehberi.service.abstracts.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RatingDto>> createRating(@RequestBody RatingDto ratingDto) {
        return ResponseEntity.ok(
                ApiResponse.success("Puan başarıyla eklendi.", ratingService.createRating(ratingDto))
        );
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<ApiResponse<List<RatingDto>>> getRatingsByPlace(@PathVariable Long placeId) {
        return ResponseEntity.ok(
                ApiResponse.success("Mekana ait puanlar başarıyla listelendi.", ratingService.getRatingsByPlace(placeId))
        );
    }
}