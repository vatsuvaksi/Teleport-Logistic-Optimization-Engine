package com.teleport.optimal_truck_load_planner.domain.model;
import com.teleport.optimal_truck_load_planner.api.dto.request.TruckRequest;


public record Truck(
        String id,
        int maxWeightLbs,
        int maxVolumeCuft
) {

    public static Truck from(TruckRequest request) {
        return new Truck(
                request.id(),
                request.maxWeightLbs(),
                request.maxVolumeCuft()
        );
    }
}
