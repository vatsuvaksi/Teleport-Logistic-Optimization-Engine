package com.teleport.optimal_truck_load_planner.domain.optimizer;

import com.teleport.optimal_truck_load_planner.TestData;
import com.teleport.optimal_truck_load_planner.domain.constraint.CapacityConstraint;
import com.teleport.optimal_truck_load_planner.domain.constraint.HazmatConstraint;
import com.teleport.optimal_truck_load_planner.domain.constraint.RouteConstraint;
import com.teleport.optimal_truck_load_planner.domain.constraint.TimeWindowConstraint;
import com.teleport.optimal_truck_load_planner.domain.model.OptimizationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitmaskLoadOptimizerTest {

    private LoadOptimizer optimizer;

    @BeforeEach
    void setup() {
        optimizer = new BitmaskLoadOptimizer(
                List.of(
                        new CapacityConstraint(),
                        new HazmatConstraint(),
                        new RouteConstraint(),
                        new TimeWindowConstraint()
                ),
                new CapacityConstraint()
        );
    }

    @Test
    void selects_best_payout_combination() {
        OptimizationResult result = optimizer.optimize(
                TestData.contextWithOrders(
                        TestData.order("1", 100),
                        TestData.order("2", 200),
                        TestData.order(
                                "3",
                                BigDecimal.valueOf(1000),
                                10_000, // exceeds capacity
                                10_000,
                                "LA",
                                "TX",
                                LocalDate.now(),
                                LocalDate.now().plusDays(1),
                                false
                        )
                )
        );

        assertEquals(List.of("1", "2"), result.selectedOrderIds());
    }


    @Test
    void ignores_orders_exceeding_capacity() {
        OptimizationResult result = optimizer.optimize(
                TestData.contextWithOrders(
                        TestData.order(5000, 5000), // too big
                        TestData.order(100, 100)
                )
        );

        assertEquals(1, result.selectedOrderIds().size());
    }

    @Test
    void returns_empty_when_no_valid_combination() {
        OptimizationResult result = optimizer.optimize(
                TestData.contextWithOrders(
                        TestData.order(99999, 99999)
                )
        );

        assertTrue(result.selectedOrderIds().isEmpty());
    }

    @Test
    void handles_empty_orders() {
        OptimizationResult result = optimizer.optimize(
                new OptimizationContext(TestData.truck(), List.of())
        );

        assertTrue(result.selectedOrderIds().isEmpty());
        assertEquals(BigDecimal.ZERO, result.totalPayoutCents());
    }
}

