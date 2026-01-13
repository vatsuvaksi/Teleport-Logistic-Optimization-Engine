package com.teleport.optimal_truck_load_planner.cache;


import com.teleport.optimal_truck_load_planner.domain.model.OptimizationResult;

import java.util.Optional;

public interface OptimizationCache {

    Optional<OptimizationResult> get(String key);

    void put(String key, OptimizationResult result);

    /**
     * Builds a deterministic cache key from the request.
     */
    String buildKey(Object request);
}