package com.teleport.optimal_truck_load_planner.api.controller;

import com.teleport.optimal_truck_load_planner.api.dto.request.OptimizeLoadRequest;
import com.teleport.optimal_truck_load_planner.api.dto.response.OptimizeLoadResponse;
import com.teleport.optimal_truck_load_planner.exception.PayloadTooLargeException;
import com.teleport.optimal_truck_load_planner.service.LoadOptimizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/load-optimizer")
@RequiredArgsConstructor
public class LoadOptimizerController {

    private final LoadOptimizationService service;
    private static final int MAX_ORDERS = 22;

    @PostMapping("/optimize")
    public ResponseEntity<OptimizeLoadResponse> optimize(
            @Valid
            @RequestBody OptimizeLoadRequest request) {

        if (request.orders().size() > MAX_ORDERS) {
            throw new PayloadTooLargeException(
                    "Maximum allowed orders per request is " + MAX_ORDERS
            );
        }
        OptimizeLoadResponse response = service.optimize(request);
        return ResponseEntity.ok(response);
    }
}
