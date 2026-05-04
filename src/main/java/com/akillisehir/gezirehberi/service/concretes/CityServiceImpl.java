package com.akillisehir.gezirehberi.service.concretes;

import com.akillisehir.gezirehberi.dto.CityDto;
import com.akillisehir.gezirehberi.entity.City;
import com.akillisehir.gezirehberi.exception.ResourceNotFoundException;
import com.akillisehir.gezirehberi.mapper.CityMapper;
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
    public List<CityDto> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(CityMapper::toDto)
                .toList();
    }

    @Override
    public CityDto getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Şehir bulunamadı: " + id));

        return CityMapper.toDto(city);
    }

    @Override
    public CityDto createCity(CityDto cityDto) {
        City city = CityMapper.toEntity(cityDto);
        City savedCity = cityRepository.save(city);

        return CityMapper.toDto(savedCity);
    }

    @Override
    public CityDto updateCity(Long id, CityDto cityDto) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Şehir bulunamadı: " + id));

        city.setName(cityDto.getName());
        city.setCountry(cityDto.getCountry());
        city.setDescription(cityDto.getDescription());
        city.setImageUrl(cityDto.getImageUrl());

        City updatedCity = cityRepository.save(city);

        return CityMapper.toDto(updatedCity);
    }

    @Override
    public void deleteCity(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Şehir bulunamadı: " + id));

        cityRepository.delete(city);
    }
}