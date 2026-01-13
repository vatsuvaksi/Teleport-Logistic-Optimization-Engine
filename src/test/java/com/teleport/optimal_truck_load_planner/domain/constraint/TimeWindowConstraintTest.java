package com.teleport.optimal_truck_load_planner.domain.constraint;

import com.teleport.optimal_truck_load_planner.TestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeWindowConstraintTest {

    private final TimeWindowConstraint constraint = new TimeWindowConstraint();

    @Test
    void valid_when_pickup_before_delivery() {
        assertTrue(constraint.isValid(
                List.of(TestData.orderWithDates("2025-01-01", "2025-01-05")),
                TestData.truck()
        ));
    }

    @Test
    void invalid_when_pickup_after_delivery() {
        assertFalse(constraint.isValid(
                List.of(TestData.orderWithDates("2025-01-10", "2025-01-05")),
                TestData.truck()
        ));
    }
}
