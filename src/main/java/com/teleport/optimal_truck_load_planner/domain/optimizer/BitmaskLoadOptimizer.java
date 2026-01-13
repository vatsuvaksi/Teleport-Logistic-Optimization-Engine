package com.teleport.optimal_truck_load_planner.domain.optimizer;

import com.teleport.optimal_truck_load_planner.domain.constraint.CapacityConstraint;
import com.teleport.optimal_truck_load_planner.domain.constraint.CompatibilityChecker;
import com.teleport.optimal_truck_load_planner.domain.model.OptimizationResult;
import com.teleport.optimal_truck_load_planner.domain.model.Order;
import com.teleport.optimal_truck_load_planner.domain.model.Truck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BitmaskLoadOptimizer implements LoadOptimizer {

    private final List<CompatibilityChecker> compatibilityCheckers;
    private final CapacityConstraint capacityConstraint;
    // capacity kept explicit for early pruning

    @Override
    public OptimizationResult optimize(OptimizationContext context) {

        Truck truck = context.truck();
        List<Order> orders = context.orders();

        int n = orders.size();
        if (n == 0) {
            return OptimizationResult.empty(truck);
        }

        BigDecimal bestPayout = BigDecimal.ZERO;
        int bestMask = 0;

        int maxMask = 1 << n;

        for (int mask = 1; mask < maxMask; mask++) {

            List<Order> selectedOrders = new ArrayList<>();
            BigDecimal totalPayout = BigDecimal.ZERO;

            boolean capacityViolated = false;

            // ---- Build subset + early capacity pruning ----
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    Order order = orders.get(i);
                    selectedOrders.add(order);
                    totalPayout = totalPayout.add(order.payoutCents());

                    if (!capacityConstraint.isValid(selectedOrders, truck)) {
                        capacityViolated = true;
                        break;
                    }
                }
            }

            if (capacityViolated) {
                continue;
            }

            // ---- Run remaining constraints ----
            boolean valid = true;
            for (CompatibilityChecker checker : compatibilityCheckers) {

                // capacity already checked
                if (checker instanceof CapacityConstraint) {
                    continue;
                }

                if (!checker.isValid(selectedOrders, truck)) {
                    valid = false;
                    break;
                }
            }

            if (!valid) {
                continue;
            }

            // ---- Best solution selection ----
            if (totalPayout.compareTo(bestPayout) > 0) {
                bestPayout = totalPayout;
                bestMask = mask;
            }
        }

        return buildResult(bestMask, orders, truck, bestPayout);
    }

    private OptimizationResult buildResult(
            int mask,
            List<Order> orders,
            Truck truck,
            BigDecimal payout
    ) {

        List<String> selectedOrderIds = new ArrayList<>();
        int totalWeight = 0;
        int totalVolume = 0;

        for (int i = 0; i < orders.size(); i++) {
            if ((mask & (1 << i)) != 0) {
                Order o = orders.get(i);
                selectedOrderIds.add(o.id());
                totalWeight += o.weightLbs();
                totalVolume += o.volumeCuft();
            }
        }

        double weightUtil = percentage(totalWeight, truck.maxWeightLbs());
        double volumeUtil = percentage(totalVolume, truck.maxVolumeCuft());

        return new OptimizationResult(
                truck.id(),
                selectedOrderIds,
                payout,
                totalWeight,
                totalVolume,
                weightUtil,
                volumeUtil
        );
    }

    private double percentage(int used, int max) {
        if (max == 0) return 0.0;
        return Math.round(((double) used / max) * 10000.0) / 100.0;
    }
}
