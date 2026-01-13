package com.teleport.optimal_truck_load_planner.domain.constraint;

import com.teleport.optimal_truck_load_planner.TestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HazmatConstraintTest {

    private final HazmatConstraint constraint = new HazmatConstraint();

    @Test
    void valid_when_all_non_hazmat() {
        assertTrue(constraint.isValid(
                List.of(TestData.order(false), TestData.order(false)),
                TestData.truck()
        ));
    }

    @Test
    void valid_when_all_hazmat() {
        assertTrue(constraint.isValid(
                List.of(TestData.order(true), TestData.order(true)),
                TestData.truck()
        ));
    }

    @Test
    void invalid_when_mixed_hazmat() {
        assertFalse(constraint.isValid(
                List.of(TestData.order(true), TestData.order(false)),
                TestData.truck()
        ));
    }
}
