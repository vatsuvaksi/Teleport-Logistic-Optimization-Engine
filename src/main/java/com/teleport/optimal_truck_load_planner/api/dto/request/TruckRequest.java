package com.teleport.optimal_truck_load_planner.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record TruckRequest(

        @NotBlank(message = "truck id is required")
        String id,

        @Min(value = 1, message = "max_weight_lbs must be > 0")
        @JsonProperty("max_weight_lbs")
        int maxWeightLbs,

        @Min(value = 1, message = "max_volume_cuft must be > 0")
        @JsonProperty("max_volume_cuft")
        int maxVolumeCuft

) {}