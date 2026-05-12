package com.akillisehir.gezirehberi.nosql.controller;

import com.akillisehir.gezirehberi.dto.ApiResponse;
import com.akillisehir.gezirehberi.nosql.document.RecentSearchDocument;
import com.akillisehir.gezirehberi.nosql.service.CacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nosql/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Object>> saveSearch(@RequestParam String keyword) {

        cacheService.saveSearch(keyword);

        return ResponseEntity.ok(
                ApiResponse.success("Arama cache sistemine kaydedildi.", null)
        );
    }

    @GetMapping("/recent-searches")
    public ResponseEntity<ApiResponse<List<RecentSearchDocument>>> getRecentSearches() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Son aramalar başarıyla listelendi.",
                        cacheService.getRecentSearches()
                )
        );
    }
}