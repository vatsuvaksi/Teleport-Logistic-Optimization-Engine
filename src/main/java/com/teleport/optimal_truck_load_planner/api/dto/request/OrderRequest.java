package com.teleport.optimal_truck_load_planner.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderRequest(

        @NotBlank(message = "order id is required")
        String id,

        @NotNull(message = "payout_cents is required")
        @Min(value = 0, message = "payout_cents must be >= 0")
        @JsonProperty("payout_cents")
        BigDecimal payoutCents,

        @Min(value = 1, message = "weight_lbs must be > 0")
        @JsonProperty("weight_lbs")
        int weightLbs,

        @Min(value = 1, message = "volume_cuft must be > 0")
        @JsonProperty("volume_cuft")
        int volumeCuft,

        @NotBlank(message = "origin is required")
        @JsonProperty("origin")
        String origin,

        @JsonProperty("destination")
        @NotBlank(message = "destination is required")
        String destination,

        @JsonProperty("pickup_date")
        @NotNull(message = "pickup_date is required")
        LocalDate pickupDate,

        @JsonProperty("delivery_date")
        @NotNull(message = "delivery_date is required")
        LocalDate deliveryDate,

        @JsonProperty("is_hazmat")
        boolean isHazmat

) {
}