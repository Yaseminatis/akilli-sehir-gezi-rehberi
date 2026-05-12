package com.akillisehir.gezirehberi.nosql.controller;

import com.akillisehir.gezirehberi.dto.ApiResponse;
import com.akillisehir.gezirehberi.nosql.document.FavoriteDocument;
import com.akillisehir.gezirehberi.nosql.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nosql/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteDocument>> addFavorite(
            @RequestParam Long userId,
            @RequestParam Long placeId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Mekan favorilere başarıyla eklendi.",
                        favoriteService.addFavorite(userId, placeId)
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<FavoriteDocument>>> getFavoritesByUser(@PathVariable Long userId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Kullanıcının favorileri başarıyla listelendi.",
                        favoriteService.getFavoritesByUser(userId)
                )
        );
    }
}