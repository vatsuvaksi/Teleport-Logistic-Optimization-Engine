package com.teleport.optimal_truck_load_planner.service;

import com.teleport.optimal_truck_load_planner.TestData;
import com.teleport.optimal_truck_load_planner.api.dto.request.OptimizeLoadRequest;
import com.teleport.optimal_truck_load_planner.api.dto.response.OptimizeLoadResponse;
import com.teleport.optimal_truck_load_planner.cache.OptimizationCache;
import com.teleport.optimal_truck_load_planner.domain.model.OptimizationResult;
import com.teleport.optimal_truck_load_planner.domain.optimizer.LoadOptimizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoadOptimizationServiceTest {

    @Mock
    private LoadOptimizer optimizer;

    @Mock
    private OptimizationCache cache;

    @InjectMocks
    private LoadOptimizationServiceImpl service;

    @Test
    void returns_cached_result_when_present() {
        OptimizeLoadRequest request = TestData.request();
        OptimizationResult result = TestData.result();

        when(cache.buildKey(request)).thenReturn("k1");
        when(cache.get("k1")).thenReturn(Optional.of(result));

        OptimizeLoadResponse response = service.optimize(request);

        assertEquals(result.truckId(), response.truckId());
        verify(optimizer, never()).optimize(any());
    }

    @Test
    void computes_when_cache_miss() {
        when(cache.buildKey(any())).thenReturn("k1");   // ← REQUIRED
        when(cache.get("k1")).thenReturn(Optional.empty());
        when(optimizer.optimize(any())).thenReturn(TestData.result());

        OptimizeLoadResponse response = service.optimize(TestData.request());

        assertNotNull(response);
        verify(optimizer).optimize(any());
    }

}

