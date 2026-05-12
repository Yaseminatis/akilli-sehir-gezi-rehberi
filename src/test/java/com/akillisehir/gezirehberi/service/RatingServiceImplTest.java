package com.akillisehir.gezirehberi.service;

import com.akillisehir.gezirehberi.dto.RatingDto;
import com.akillisehir.gezirehberi.entity.Place;
import com.akillisehir.gezirehberi.entity.Rating;
import com.akillisehir.gezirehberi.entity.User;
import com.akillisehir.gezirehberi.repository.PlaceRepository;
import com.akillisehir.gezirehberi.repository.RatingRepository;
import com.akillisehir.gezirehberi.repository.UserRepository;
import com.akillisehir.gezirehberi.service.concretes.RatingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    void shouldCreateRatingWhenScoreIsValid() {

        RatingDto ratingDto = new RatingDto();
        ratingDto.setScore(5);
        ratingDto.setUserId(1L);
        ratingDto.setPlaceId(1L);

        User user = new User();

        Place place = new Place();

        Rating savedRating = new Rating();
        savedRating.setScore(5);
        savedRating.setUser(user);
        savedRating.setPlace(place);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));
        when(ratingRepository.save(any(Rating.class))).thenReturn(savedRating);

        RatingDto result = ratingService.createRating(ratingDto);

        assertNotNull(result);
        assertEquals(5, result.getScore());

        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void shouldThrowExceptionWhenScoreIsInvalid() {

        RatingDto ratingDto = new RatingDto();
        ratingDto.setScore(6);
        ratingDto.setUserId(1L);
        ratingDto.setPlaceId(1L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ratingService.createRating(ratingDto)
        );

        assertEquals(
                "Puan değeri 1 ile 5 arasında olmalıdır.",
                exception.getMessage()
        );

        verify(ratingRepository, never()).save(any(Rating.class));
    }
}