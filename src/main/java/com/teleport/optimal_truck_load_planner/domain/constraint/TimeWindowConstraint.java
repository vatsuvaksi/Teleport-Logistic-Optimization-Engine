package com.teleport.optimal_truck_load_planner.domain.constraint;

import com.teleport.optimal_truck_load_planner.domain.model.Order;
import com.teleport.optimal_truck_load_planner.domain.model.Truck;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TimeWindowConstraint implements CompatibilityChecker {

    @Override
    public boolean isValid(List<Order> orders, Truck truck) {

        for (Order order : orders) {

            LocalDate pickup = order.pickupDate();
            LocalDate delivery = order.deliveryDate();

            if (pickup == null || delivery == null) {
                return false;
            }

            if (pickup.isAfter(delivery)) {
                return false;
            }
        }

        return true;
    }
}
