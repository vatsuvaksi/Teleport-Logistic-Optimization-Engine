package com.teleport.optimal_truck_load_planner.domain.optimizer;

import com.teleport.optimal_truck_load_planner.api.dto.request.OptimizeLoadRequest;
import com.teleport.optimal_truck_load_planner.domain.model.Order;
import com.teleport.optimal_truck_load_planner.domain.model.Truck;

import java.util.List;

public record OptimizationContext(
        Truck truck,
        List<Order> orders
) {

    public static OptimizationContext from(OptimizeLoadRequest request) {
        Truck truck = Truck.from(request.truck());
        List<Order> orders = request.orders()
                .stream()
                .map(Order::from)
                .toList();

        return new OptimizationContext(truck, orders);
    }
}