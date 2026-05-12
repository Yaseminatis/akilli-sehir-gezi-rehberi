package com.akillisehir.gezirehberi.nosql.controller;

import com.akillisehir.gezirehberi.dto.ApiResponse;
import com.akillisehir.gezirehberi.nosql.document.TravelPlanDocument;
import com.akillisehir.gezirehberi.nosql.service.TravelPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nosql/travel-plans")
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    public TravelPlanController(TravelPlanService travelPlanService) {
        this.travelPlanService = travelPlanService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TravelPlanDocument>> createPlan(
            @RequestBody TravelPlanDocument travelPlanDocument) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Gezi planı başarıyla oluşturuldu.",
                        travelPlanService.createPlan(travelPlanDocument)
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TravelPlanDocument>>> getPlansByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Kullanıcının gezi planları başarıyla listelendi.",
                        travelPlanService.getPlansByUser(userId)
                )
        );
    }
}