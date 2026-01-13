package com.teleport.optimal_truck_load_planner.domain.optimizer;

import com.teleport.optimal_truck_load_planner.domain.model.OptimizationResult;

public interface LoadOptimizer {
    OptimizationResult optimize(OptimizationContext context);
}
