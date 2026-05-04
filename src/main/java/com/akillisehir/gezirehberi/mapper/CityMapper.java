package com.akillisehir.gezirehberi.mapper;

import com.akillisehir.gezirehberi.dto.CityDto;
import com.akillisehir.gezirehberi.entity.City;

public class CityMapper {

    public static CityDto toDto(City city) {
        CityDto dto = new CityDto();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setCountry(city.getCountry());
        dto.setDescription(city.getDescription());
        dto.setImageUrl(city.getImageUrl());
        return dto;
    }

    public static City toEntity(CityDto dto) {
        City city = new City();
        city.setName(dto.getName());
        city.setCountry(dto.getCountry());
        city.setDescription(dto.getDescription());
        city.setImageUrl(dto.getImageUrl());
        return city;
    }
}