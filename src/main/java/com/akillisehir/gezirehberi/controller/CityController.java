package com.akillisehir.gezirehberi.controller;

import com.akillisehir.gezirehberi.dto.CityDto;
import com.akillisehir.gezirehberi.service.abstracts.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<CityDto> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    public CityDto getCityById(@PathVariable Long id) {
        return cityService.getCityById(id);
    }

    @PostMapping
    public CityDto createCity(@RequestBody CityDto cityDto) {
        return cityService.createCity(cityDto);
    }

    @PutMapping("/{id}")
    public CityDto updateCity(@PathVariable Long id, @RequestBody CityDto cityDto) {
        return cityService.updateCity(id, cityDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
    }
}