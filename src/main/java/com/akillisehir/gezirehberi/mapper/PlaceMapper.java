package com.akillisehir.gezirehberi.mapper;

import com.akillisehir.gezirehberi.dto.PlaceDto;
import com.akillisehir.gezirehberi.entity.Place;

public class PlaceMapper {

    public static PlaceDto toDto(Place place) {
        PlaceDto dto = new PlaceDto();

        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setDescription(place.getDescription());
        dto.setCategory(place.getCategory());

        if (place.getCity() != null) {
            dto.setCityId(place.getCity().getId());
        }

        return dto;
    }

    public static Place toEntity(PlaceDto dto) {
        Place place = new Place();

        place.setName(dto.getName());
        place.setDescription(dto.getDescription());
        place.setCategory(dto.getCategory());

        return place;
    }
}