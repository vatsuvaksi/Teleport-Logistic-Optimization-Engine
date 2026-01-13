package com.teleport.optimal_truck_load_planner.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleport.optimal_truck_load_planner.TestData;
import com.teleport.optimal_truck_load_planner.domain.model.OptimizationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryOptimizationCacheTest {

    private OptimizationCache cache;

    @BeforeEach
    void setup() {
        cache = new InMemoryOptimizationCache(
                new CacheKeyGenerator()
        );
    }


    @Test
    void put_and_get_works() {
        String key = "k1";
        OptimizationResult result = TestData.result();

        cache.put(key, result);

        assertTrue(cache.get(key).isPresent());
    }

    @Test
    void get_missing_key_returns_empty() {
        assertTrue(cache.get("missing").isEmpty());
    }
}

