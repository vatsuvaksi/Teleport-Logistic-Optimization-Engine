package com.teleport.optimal_truck_load_planner.domain.constraint;


import com.teleport.optimal_truck_load_planner.domain.model.Order;
import com.teleport.optimal_truck_load_planner.domain.model.Truck;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HazmatConstraint implements CompatibilityChecker {

    @Override
    public boolean isValid(List<Order> orders, Truck truck) {

        Boolean hazmatFlag = null;

        for (Order order : orders) {
            if (hazmatFlag == null) {
                hazmatFlag = order.isHazmat();
            } else if (!hazmatFlag.equals(order.isHazmat())) {
                return false;
            }
        }

        return true;
    }
}
