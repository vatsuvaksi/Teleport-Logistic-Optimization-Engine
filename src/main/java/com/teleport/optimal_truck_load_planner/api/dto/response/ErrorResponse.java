package com.teleport.optimal_truck_load_planner.api.dto.response;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(

        String errorCode,
        String message,
        List<String> details,
        Instant timestamp

) {
}