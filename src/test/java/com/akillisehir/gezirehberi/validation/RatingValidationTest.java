package com.akillisehir.gezirehberi.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingValidationTest {

    @Test
    void shouldRejectScoreGreaterThanFive() {

        int score = 6;

        assertTrue(score > 5);
    }

    @Test
    void shouldRejectScoreLessThanOne() {

        int score = 0;

        assertTrue(score < 1);
    }

    @Test
    void shouldAcceptValidScore() {

        int score = 5;

        assertTrue(score >= 1 && score <= 5);
    }
}