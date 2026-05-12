package com.akillisehir.gezirehberi.service.concretes;

import com.akillisehir.gezirehberi.dto.RatingDto;
import com.akillisehir.gezirehberi.entity.Place;
import com.akillisehir.gezirehberi.entity.Rating;
import com.akillisehir.gezirehberi.entity.User;
import com.akillisehir.gezirehberi.exception.ResourceNotFoundException;
import com.akillisehir.gezirehberi.repository.PlaceRepository;
import com.akillisehir.gezirehberi.repository.RatingRepository;
import com.akillisehir.gezirehberi.repository.UserRepository;
import com.akillisehir.gezirehberi.service.abstracts.RatingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public RatingServiceImpl(RatingRepository ratingRepository,
                             UserRepository userRepository,
                             PlaceRepository placeRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public RatingDto createRating(RatingDto ratingDto) {
        if (ratingDto.getScore() == null || ratingDto.getScore() < 1 || ratingDto.getScore() > 5) {
            throw new IllegalArgumentException("Puan değeri 1 ile 5 arasında olmalıdır.");
        }

        User user = userRepository.findById(ratingDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + ratingDto.getUserId()));

        Place place = placeRepository.findById(ratingDto.getPlaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Mekan bulunamadı: " + ratingDto.getPlaceId()));

        Rating rating = new Rating();
        rating.setScore(ratingDto.getScore());
        rating.setUser(user);
        rating.setPlace(place);

        Rating savedRating = ratingRepository.save(rating);

        return toDto(savedRating);
    }

    @Override
    public List<RatingDto> getRatingsByPlace(Long placeId) {
        return ratingRepository.findByPlaceId(placeId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private RatingDto toDto(Rating rating) {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setId(rating.getId());
        ratingDto.setScore(rating.getScore());
        ratingDto.setUserId(rating.getUser().getId());
        ratingDto.setPlaceId(rating.getPlace().getId());
        return ratingDto;
    }
}