package com.akillisehir.gezirehberi.nosql.service;

import com.akillisehir.gezirehberi.exception.ResourceNotFoundException;
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

    @Override
    public TravelPlanDocument getPlanById(String planId) {
        return travelPlanRepository.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gezi planı bulunamadı: " + planId
                        ));
    }

    @Override
    public TravelPlanDocument updatePlan(String planId,
                                         TravelPlanDocument travelPlanDocument) {

        TravelPlanDocument existingPlan = getPlanById(planId);

        existingPlan.setTitle(travelPlanDocument.getTitle());
        existingPlan.setPlaceIds(travelPlanDocument.getPlaceIds());

        return travelPlanRepository.save(existingPlan);
    }

    @Override
    public void deletePlan(String planId) {
        TravelPlanDocument existingPlan = getPlanById(planId);
        travelPlanRepository.delete(existingPlan);
    }
}