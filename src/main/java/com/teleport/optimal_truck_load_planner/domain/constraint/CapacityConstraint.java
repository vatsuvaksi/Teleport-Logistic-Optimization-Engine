package com.teleport.optimal_truck_load_planner.domain.constraint;

import com.teleport.optimal_truck_load_planner.domain.model.Order;
import com.teleport.optimal_truck_load_planner.domain.model.Truck;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CapacityConstraint implements CompatibilityChecker {

    @Override
    public boolean isValid(List<Order> orders, Truck truck) {

        int totalWeight = 0;
        int totalVolume = 0;

        for (Order order : orders) {
            totalWeight += order.weightLbs();
            totalVolume += order.volumeCuft();

            if (totalWeight > truck.maxWeightLbs()
                    || totalVolume > truck.maxVolumeCuft()) {
                return false;
            }
        }

        return true;
    }
}
