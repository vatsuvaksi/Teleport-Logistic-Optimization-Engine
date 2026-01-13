package com.teleport.optimal_truck_load_planner.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OptimizeLoadRequest(

        @NotNull(message = "truck must be provided")
        @Valid
        TruckRequest truck,

        @NotEmpty(message = "orders list cannot be empty")
        @Valid
        List<OrderRequest> orders

) {
}