package com.teleport.optimal_truck_load_planner.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleport.optimal_truck_load_planner.domain.model.OptimizationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Optional;

@Slf4j
@Component
@Profile("redis")
@RequiredArgsConstructor
public class RedisOptimizationCache implements OptimizationCache {

    private static final int TTL_SECONDS = 600; // 10 minutes

    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;
    private final CacheKeyGenerator keyGenerator;

    @Override
    public Optional<OptimizationResult> get(String key) {

        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            if (value == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(value, OptimizationResult.class));
        } catch (Exception ex) {
            log.warn("Redis cache GET failed, falling back to compute", ex);
            return Optional.empty();
        }
    }

    @Override
    public void put(String key, OptimizationResult result) {

        try (Jedis jedis = jedisPool.getResource()) {
            String json = objectMapper.writeValueAsString(result);
            jedis.setex(key, TTL_SECONDS, json);
        } catch (Exception ex) {
            log.warn("Redis cache PUT failed, ignoring", ex);
        }
    }

    @Override
    public String buildKey(Object request) {
        return keyGenerator.generate(request);
    }
}
