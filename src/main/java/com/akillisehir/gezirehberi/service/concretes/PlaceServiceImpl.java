package com.akillisehir.gezirehberi.service.concretes;

import com.akillisehir.gezirehberi.dto.PlaceDto;
import com.akillisehir.gezirehberi.entity.City;
import com.akillisehir.gezirehberi.entity.Place;
import com.akillisehir.gezirehberi.enums.PlaceCategory;
import com.akillisehir.gezirehberi.exception.ResourceNotFoundException;
import com.akillisehir.gezirehberi.mapper.PlaceMapper;
import com.akillisehir.gezirehberi.repository.CityRepository;
import com.akillisehir.gezirehberi.repository.PlaceRepository;
import com.akillisehir.gezirehberi.service.abstracts.PlaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final CityRepository cityRepository;

    public PlaceServiceImpl(PlaceRepository placeRepository, CityRepository cityRepository) {
        this.placeRepository = placeRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<PlaceDto> getAllPlaces() {
        return placeRepository.findAll()
                .stream()
                .map(PlaceMapper::toDto)
                .toList();
    }

    @Override
    public PlaceDto getPlaceById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mekan bulunamadı: " + id));

        return PlaceMapper.toDto(place);
    }

    @Override
    public List<PlaceDto> getPlacesByCity(Long cityId) {
        return placeRepository.findByCityId(cityId)
                .stream()
                .map(PlaceMapper::toDto)
                .toList();
    }

    @Override
    public List<PlaceDto> getPlacesByCategory(PlaceCategory category) {
        return placeRepository.findByCategory(category)
                .stream()
                .map(PlaceMapper::toDto)
                .toList();
    }

    @Override
    public PlaceDto createPlace(PlaceDto placeDto) {
        Place place = PlaceMapper.toEntity(placeDto);

        City city = cityRepository.findById(placeDto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("Şehir bulunamadı"));

        place.setCity(city);

        Place savedPlace = placeRepository.save(place);

        return PlaceMapper.toDto(savedPlace);
    }

    @Override
    public PlaceDto updatePlace(Long id, PlaceDto placeDto) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mekan bulunamadı: " + id));

        place.setName(placeDto.getName());
        place.setDescription(placeDto.getDescription());
        place.setCategory(placeDto.getCategory());

        if (placeDto.getCityId() != null) {
            City city = cityRepository.findById(placeDto.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Şehir bulunamadı"));
            place.setCity(city);
        }

        Place updatedPlace = placeRepository.save(place);

        return PlaceMapper.toDto(updatedPlace);
    }

    @Override
    public void deletePlace(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mekan bulunamadı: " + id));

        placeRepository.delete(place);
    }
}