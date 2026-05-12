package com.akillisehir.gezirehberi.nosql.service;

import com.akillisehir.gezirehberi.nosql.document.RecentSearchDocument;
import com.akillisehir.gezirehberi.nosql.repository.RecentSearchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CacheServiceImpl implements CacheService {

    private final RecentSearchRepository recentSearchRepository;

    public CacheServiceImpl(RecentSearchRepository recentSearchRepository) {
        this.recentSearchRepository = recentSearchRepository;
    }

    @Override
    public void saveSearch(String keyword) {

        RecentSearchDocument document = new RecentSearchDocument();
        document.setKeyword(keyword);
        document.setSearchedAt(LocalDateTime.now());

        recentSearchRepository.save(document);
    }

    @Override
    public List<RecentSearchDocument> getRecentSearches() {
        return recentSearchRepository.findAll();
    }
}