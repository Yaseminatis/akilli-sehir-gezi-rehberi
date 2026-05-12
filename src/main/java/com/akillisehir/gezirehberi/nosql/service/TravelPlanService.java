package com.akillisehir.gezirehberi.nosql.service;

import com.akillisehir.gezirehberi.nosql.document.TravelPlanDocument;

import java.util.List;

public interface TravelPlanService {

    TravelPlanDocument createPlan(TravelPlanDocument travelPlanDocument);

    List<TravelPlanDocument> getPlansByUser(Long userId);
}