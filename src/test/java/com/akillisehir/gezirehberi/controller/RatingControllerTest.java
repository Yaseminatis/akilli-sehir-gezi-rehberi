package com.akillisehir.gezirehberi.controller;

import com.akillisehir.gezirehberi.dto.RatingDto;
import com.akillisehir.gezirehberi.service.abstracts.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class RatingControllerTest {

    private MockMvc mockMvc;
    private RatingService ratingService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ratingService = mock(RatingService.class);
        RatingController ratingController = new RatingController(ratingService);
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateRatingSuccessfully() throws Exception {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setScore(5);
        ratingDto.setUserId(1L);
        ratingDto.setPlaceId(1L);

        when(ratingService.createRating(ratingDto)).thenReturn(ratingDto);

        mockMvc.perform(post("/api/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}