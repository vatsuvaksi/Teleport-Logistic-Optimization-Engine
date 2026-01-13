package com.teleport.optimal_truck_load_planner.domain.model;

import com.teleport.optimal_truck_load_planner.api.dto.request.OrderRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Order(
        String id,
        BigDecimal payoutCents,
        int weightLbs,
        int volumeCuft,
        Route route,
        LocalDate pickupDate,
        LocalDate deliveryDate,
        boolean isHazmat
) {

    public static Order from(OrderRequest request) {
        return new Order(
                request.id(),
                request.payoutCents(),
                request.weightLbs(),
                request.volumeCuft(),
                new Route(request.origin(), request.destination()),
                request.pickupDate(),
                request.deliveryDate(),
                request.isHazmat()
        );
    }
}
