package com.akillisehir.gezirehberi.nosql.service;

import com.akillisehir.gezirehberi.nosql.document.FavoriteDocument;
import com.akillisehir.gezirehberi.nosql.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public FavoriteDocument addFavorite(Long userId, Long placeId) {

        if (favoriteRepository.existsByUserIdAndPlaceId(userId, placeId)) {
            throw new IllegalArgumentException("Bu mekan zaten favorilere eklenmiş.");
        }

        FavoriteDocument favoriteDocument = new FavoriteDocument();
        favoriteDocument.setUserId(userId);
        favoriteDocument.setPlaceId(placeId);

        return favoriteRepository.save(favoriteDocument);
    }

    @Override
    public List<FavoriteDocument> getFavoritesByUser(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }
}