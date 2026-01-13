package com.teleport.optimal_truck_load_planner.cache;

import com.teleport.optimal_truck_load_planner.domain.model.OptimizationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Primary
@RequiredArgsConstructor
public class InMemoryOptimizationCache implements OptimizationCache {

    private final CacheKeyGenerator keyGenerator;

    private final Map<String, OptimizationResult> cache =
            new ConcurrentHashMap<>();

    @Override
    public Optional<OptimizationResult> get(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public void put(String key, OptimizationResult result) {
        cache.put(key, result);
    }

    @Override
    public String buildKey(Object request) {
        return keyGenerator.generate(request);
    }
}
