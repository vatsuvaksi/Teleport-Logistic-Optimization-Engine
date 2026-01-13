package com.teleport.optimal_truck_load_planner.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${redis.host:localhost}")
    private String host;

    @Value("${redis.port:6379}")
    private int port;

    @Value("${redis.timeout.ms:2000}")
    private int timeoutMs;

    @Bean
    public JedisPool jedisPool() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);

        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);

        return new JedisPool(poolConfig, host, port, timeoutMs);
    }
}
