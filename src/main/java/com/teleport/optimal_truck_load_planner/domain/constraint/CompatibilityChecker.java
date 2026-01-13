package com.teleport.optimal_truck_load_planner.domain.constraint;


import com.teleport.optimal_truck_load_planner.domain.model.Order;
import com.teleport.optimal_truck_load_planner.domain.model.Truck;

import java.util.List;

public interface CompatibilityChecker {

    /**
     * Returns true if the given orders are compatible with the truck
     * under this specific constraint.
     */
    boolean isValid(List<Order> orders, Truck truck);
}
