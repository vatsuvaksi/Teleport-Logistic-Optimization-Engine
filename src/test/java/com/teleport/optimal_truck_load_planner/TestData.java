package com.teleport.optimal_truck_load_planner;


import com.teleport.optimal_truck_load_planner.api.dto.request.OptimizeLoadRequest;
import com.teleport.optimal_truck_load_planner.api.dto.request.OrderRequest;
import com.teleport.optimal_truck_load_planner.api.dto.request.TruckRequest;
import com.teleport.optimal_truck_load_planner.domain.model.OptimizationResult;
import com.teleport.optimal_truck_load_planner.domain.model.Order;
import com.teleport.optimal_truck_load_planner.domain.model.Route;
import com.teleport.optimal_truck_load_planner.domain.model.Truck;
import com.teleport.optimal_truck_load_planner.domain.optimizer.OptimizationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class TestData {

    private TestData() {
        // utility class
    }

    /* -------------------------------------------------
     * Truck helpers
     * ------------------------------------------------- */

    public static Truck truck() {
        return new Truck("truck-1", 1000, 1000);
    }

    public static Truck smallTruck() {
        return new Truck("truck-small", 500, 500);
    }

    /* -------------------------------------------------
     * Order helpers (domain)
     * ------------------------------------------------- */

    public static Order order(int weight, int volume) {
        return order(
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(100),
                weight,
                volume,
                "LA",
                "TX",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                false
        );
    }

    public static Order order(boolean hazmat) {
        return order(
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(100),
                100,
                100,
                "LA",
                "TX",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                hazmat
        );
    }

    public static Order order(String origin, String destination) {
        return order(
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(100),
                100,
                100,
                origin,
                destination,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                false
        );
    }

    public static Order orderWithDates(String pickup, String delivery) {
        return order(
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(100),
                100,
                100,
                "LA",
                "TX",
                LocalDate.parse(pickup),
                LocalDate.parse(delivery),
                false
        );
    }

    public static Order order(String id, int payoutCents) {
        return order(
                id,
                BigDecimal.valueOf(payoutCents),
                100,
                100,
                "LA",
                "TX",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                false
        );
    }

    public static Order order(
            String id,
            BigDecimal payout,
            int weight,
            int volume,
            String origin,
            String destination,
            LocalDate pickup,
            LocalDate delivery,
            boolean hazmat
    ) {
        return new Order(
                id,
                payout,
                weight,
                volume,
                new Route(origin, destination),
                pickup,
                delivery,
                hazmat
        );
    }

    /* -------------------------------------------------
     * Optimization context helpers
     * ------------------------------------------------- */

    public static OptimizationContext contextWithOrders(Order... orders) {
        return new OptimizationContext(
                truck(),
                List.of(orders)
        );
    }

    /* -------------------------------------------------
     * DTO helpers
     * ------------------------------------------------- */

    public static OptimizeLoadRequest request() {
        return new OptimizeLoadRequest(
                new TruckRequest("truck-1", 1000, 1000),
                List.of(
                        orderRequest("ord-1", 200),
                        orderRequest("ord-2", 300)
                )
        );
    }

    public static OptimizeLoadRequest otherRequest() {
        return new OptimizeLoadRequest(
                new TruckRequest("truck-1", 1000, 1000),
                List.of(
                        orderRequest("ord-3", 500)
                )
        );
    }

    private static OrderRequest orderRequest(String id, int payoutCents) {
        return new OrderRequest(
                id,
                BigDecimal.valueOf(payoutCents),
                100,
                100,
                "LA",
                "TX",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                false
        );
    }

    /* -------------------------------------------------
     * Optimization result helpers
     * ------------------------------------------------- */

    public static OptimizationResult result() {
        return new OptimizationResult(
                "truck-1",
                List.of("ord-1", "ord-2"),
                BigDecimal.valueOf(500),
                200,
                200,
                20.0,
                20.0
        );
    }
}
