package com.akillisehir.gezirehberi.service.concretes;

import com.akillisehir.gezirehberi.entity.City;
import com.akillisehir.gezirehberi.repository.CityRepository;
import com.akillisehir.gezirehberi.service.abstracts.CityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public City getCityById(Long id) {
        return cityRepository.findById(id).orElse(null);
    }

    @Override
    public City createCity(City city) {
        return cityRepository.save(city);
    }
}