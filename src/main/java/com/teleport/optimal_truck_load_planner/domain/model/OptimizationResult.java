package com.teleport.optimal_truck_load_planner.domain.model;

import java.math.BigDecimal;
import java.util.List;

public record OptimizationResult(

        String truckId,
        List<String> selectedOrderIds,

        BigDecimal totalPayoutCents,

        int totalWeightLbs,
        int totalVolumeCuft,

        double weightUtilizationPercent,
        double volumeUtilizationPercent
) {

    public static OptimizationResult empty(Truck truck) {
        return new OptimizationResult(
                truck.id(),
                List.of(),
                BigDecimal.ZERO,
                0,
                0,
                0.0,
                0.0
        );
    }
}
