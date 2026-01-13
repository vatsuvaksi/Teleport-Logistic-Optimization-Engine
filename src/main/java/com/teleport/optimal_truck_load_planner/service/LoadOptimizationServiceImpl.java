package com.teleport.optimal_truck_load_planner.service;

import com.teleport.optimal_truck_load_planner.api.dto.request.OptimizeLoadRequest;
import com.teleport.optimal_truck_load_planner.api.dto.response.OptimizeLoadResponse;
import com.teleport.optimal_truck_load_planner.cache.OptimizationCache;
import com.teleport.optimal_truck_load_planner.domain.model.OptimizationResult;
import com.teleport.optimal_truck_load_planner.domain.optimizer.LoadOptimizer;
import com.teleport.optimal_truck_load_planner.domain.optimizer.OptimizationContext;
import com.teleport.optimal_truck_load_planner.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoadOptimizationServiceImpl implements LoadOptimizationService {

    private final LoadOptimizer loadOptimizer;
    private final OptimizationCache optimizationCache;

    @Override
    public OptimizeLoadResponse optimize(OptimizeLoadRequest request) {

        // Defensive validation (beyond @Valid)
        if (request.orders().isEmpty()) {
            throw new InvalidRequestException("Orders list cannot be empty");
        }

        String cacheKey = optimizationCache.buildKey(request);

        // Cache lookup
        return optimizationCache.get(cacheKey)
                .map(this::toResponse)
                .orElseGet(() -> computeAndCache(request, cacheKey));
    }

    private OptimizeLoadResponse computeAndCache(
            OptimizeLoadRequest request,
            String cacheKey
    ) {
        OptimizationContext context = OptimizationContext.from(request);

        OptimizationResult result = loadOptimizer.optimize(context);

        optimizationCache.put(cacheKey, result);

        return toResponse(result);
    }

    private OptimizeLoadResponse toResponse(OptimizationResult result) {

        return new OptimizeLoadResponse(
                result.truckId(),
                result.selectedOrderIds(),
                result.totalPayoutCents(),
                result.totalWeightLbs(),
                result.totalVolumeCuft(),
                result.weightUtilizationPercent(),
                result.volumeUtilizationPercent()
        );
    }
}