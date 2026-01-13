package com.teleport.optimal_truck_load_planner.domain.constraint;

import com.teleport.optimal_truck_load_planner.domain.model.Order;
import com.teleport.optimal_truck_load_planner.domain.model.Route;
import com.teleport.optimal_truck_load_planner.domain.model.Truck;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteConstraint implements CompatibilityChecker {

    @Override
    public boolean isValid(List<Order> orders, Truck truck) {

        Route route = null;

        for (Order order : orders) {
            if (route == null) {
                route = order.route();
            } else if (!route.equals(order.route())) {
                return false;
            }
        }

        return true;
    }
}

