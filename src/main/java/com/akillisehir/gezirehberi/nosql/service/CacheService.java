package com.akillisehir.gezirehberi.nosql.service;

import com.akillisehir.gezirehberi.nosql.document.RecentSearchDocument;

import java.util.List;

public interface CacheService {

    void saveSearch(String keyword);

    List<RecentSearchDocument> getRecentSearches();
}