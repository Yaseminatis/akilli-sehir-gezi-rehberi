package com.akillisehir.gezirehberi.service.abstracts;

import com.akillisehir.gezirehberi.dto.RatingDto;

import java.util.List;

public interface RatingService {

    RatingDto createRating(RatingDto ratingDto);

    List<RatingDto> getRatingsByPlace(Long placeId);
}