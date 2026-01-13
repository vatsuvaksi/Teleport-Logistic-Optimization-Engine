package com.teleport.optimal_truck_load_planner.domain.constraint;

import com.teleport.optimal_truck_load_planner.TestData;
import com.teleport.optimal_truck_load_planner.domain.model.Order;
import com.teleport.optimal_truck_load_planner.domain.model.Truck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CapacityConstraintTest {

    private final CapacityConstraint constraint = new CapacityConstraint();

    @Test
    void valid_when_within_capacity() {
        Truck truck = new Truck("t1", 1000, 1000);
        List<Order> orders = List.of(
                TestData.order(400, 400),
                TestData.order(500, 500)
        );

        assertTrue(constraint.isValid(orders, truck));
    }

    @Test
    void invalid_when_weight_exceeded() {
        Truck truck = new Truck("t1", 800, 1000);
        List<Order> orders = List.of(
                TestData.order(500, 400),
                TestData.order(400, 400)
        );

        assertFalse(constraint.isValid(orders, truck));
    }

    @Test
    void invalid_when_volume_exceeded() {
        Truck truck = new Truck("t1", 1000, 700);
        List<Order> orders = List.of(
                TestData.order(400, 400),
                TestData.order(400, 400)
        );

        assertFalse(constraint.isValid(orders, truck));
    }
}
