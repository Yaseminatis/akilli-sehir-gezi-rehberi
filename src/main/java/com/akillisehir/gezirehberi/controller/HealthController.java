package com.akillisehir.gezirehberi.controller;

import com.akillisehir.gezirehberi.util.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public ApiResponse<String> health() {
        return new ApiResponse<>(true, "API aktif", "Sistem çalışıyor");
    }
}