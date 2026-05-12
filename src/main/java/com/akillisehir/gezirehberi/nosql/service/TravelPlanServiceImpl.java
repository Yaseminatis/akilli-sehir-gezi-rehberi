package com.akillisehir.gezirehberi.nosql.service;

import com.akillisehir.gezirehberi.nosql.document.TravelPlanDocument;
import com.akillisehir.gezirehberi.nosql.repository.TravelPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelPlanServiceImpl implements TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;

    public TravelPlanServiceImpl(TravelPlanRepository travelPlanRepository) {
        this.travelPlanRepository = travelPlanRepository;
    }

    @Override
    public TravelPlanDocument createPlan(TravelPlanDocument travelPlanDocument) {
        return travelPlanRepository.save(travelPlanDocument);
    }

    @Override
    public List<TravelPlanDocument> getPlansByUser(Long userId) {
        return travelPlanRepository.findByUserId(userId);
    }
}