package com.teleport.optimal_truck_load_planner.exception;

public class OptimizationException extends RuntimeException {

    public OptimizationException(String message) {
        super(message);
    }

    public OptimizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
