package com.teleport.optimal_truck_load_planner.service;

import com.teleport.optimal_truck_load_planner.api.dto.request.OptimizeLoadRequest;
import com.teleport.optimal_truck_load_planner.api.dto.response.OptimizeLoadResponse;

public interface LoadOptimizationService {
    OptimizeLoadResponse optimize(OptimizeLoadRequest request);
}
