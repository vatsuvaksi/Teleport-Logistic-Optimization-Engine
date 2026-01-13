package com.teleport.optimal_truck_load_planner.exception;

import com.teleport.optimal_truck_load_planner.api.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.List;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * ==================================================
     * 400 — INVALID REQUEST (Client-side errors)
     * ==================================================
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            BindException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {

        log.warn("Invalid request: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                "INVALID_REQUEST",
                "Invalid request payload",
                List.of(ex.getMessage()),
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * ==================================================
     * 413 — PAYLOAD TOO LARGE
     * ==================================================
     */
    @ExceptionHandler({
            MaxUploadSizeExceededException.class,
            PayloadTooLargeException.class
    })
    public ResponseEntity<ErrorResponse> handlePayloadTooLarge(Exception ex) {

        log.warn("Payload too large: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                "PAYLOAD_TOO_LARGE",
                "Request payload exceeds allowed size",
                List.of(),
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(response);
    }

    /**
     * ==================================================
     * 422 — OPTIMIZATION FAILURE (Domain-level error)
     * ==================================================
     */

    @ExceptionHandler(OptimizationException.class)
    public ResponseEntity<ErrorResponse> handleOptimizationFailure(OptimizationException ex) {

        log.error("Optimization failure", ex);

        ErrorResponse response = new ErrorResponse(
                "OPTIMIZATION_FAILED",
                ex.getMessage(),
                List.of(),
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(response);
    }

    /**
     * ==================================================
     * 500 — INTERNAL SERVER ERROR (Catch-all)
     * ==================================================
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedError(Exception ex) {

        log.error("Unexpected server error", ex);

        ErrorResponse response = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Unexpected server error",
                List.of(),
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}