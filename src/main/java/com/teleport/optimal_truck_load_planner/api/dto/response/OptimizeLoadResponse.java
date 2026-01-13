package com.teleport.optimal_truck_load_planner.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record OptimizeLoadResponse(

        @JsonProperty("truck_id")
        String truckId,
        @JsonProperty("selected_order_ids")
        List<String> selectedOrderIds,
        @JsonProperty("total_payout_cents")
        BigDecimal totalPayoutCents,
        @JsonProperty("total_weight_lbs")
        int totalWeightLbs,
        @JsonProperty("total_volume_cuft")
        int totalVolumeCuft,
        @JsonProperty("utilization_weight_percent")
        double utilizationWeightPercent,
        @JsonProperty("utilization_volume_percent")
        double utilizationVolumePercent

) {}