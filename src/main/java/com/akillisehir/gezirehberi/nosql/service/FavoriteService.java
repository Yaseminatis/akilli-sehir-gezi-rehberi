package com.akillisehir.gezirehberi.nosql.service;

import com.akillisehir.gezirehberi.nosql.document.FavoriteDocument;

import java.util.List;

public interface FavoriteService {

    FavoriteDocument addFavorite(Long userId, Long placeId);

    List<FavoriteDocument> getFavoritesByUser(Long userId);

    void removeFavorite(Long userId, Long placeId);
}