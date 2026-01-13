package com.teleport.optimal_truck_load_planner.domain.constraint;

import com.teleport.optimal_truck_load_planner.TestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteConstraintTest {

    private final RouteConstraint constraint = new RouteConstraint();

    @Test
    void valid_when_same_route() {
        assertTrue(constraint.isValid(
                List.of(
                        TestData.order("LA", "TX"),
                        TestData.order("LA", "TX")
                ),
                TestData.truck()
        ));
    }

    @Test
    void invalid_when_different_routes() {
        assertFalse(constraint.isValid(
                List.of(
                        TestData.order("LA", "TX"),
                        TestData.order("NY", "TX")
                ),
                TestData.truck()
        ));
    }
}
