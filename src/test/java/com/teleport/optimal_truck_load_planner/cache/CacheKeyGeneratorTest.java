package com.teleport.optimal_truck_load_planner.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleport.optimal_truck_load_planner.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

//class CacheKeyGeneratorTest {
//
//    private CacheKeyGenerator generator;
//
//    @BeforeEach
//    void setup() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.findAndRegisterModules();
//        generator = new CacheKeyGenerator(mapper);
////        generator = new CacheKeyGenerator(new ObjectMapper());
//    }
//
//    @Test
//    void same_request_produces_same_key() {
//        String k1 = generator.generate(TestData.request());
//        String k2 = generator.generate(TestData.request());
//
//        assertEquals(k1, k2);
//    }
//
//    @Test
//    void different_request_produces_different_key() {
//        String k1 = generator.generate(TestData.request());
//        String k2 = generator.generate(TestData.otherRequest());
//
//        assertNotEquals(k1, k2);
//    }
//}

class CacheKeyGeneratorTest {

    private CacheKeyGenerator generator;

    @BeforeEach
    void setup() {
        generator = new CacheKeyGenerator();
    }

    @Test
    void same_request_produces_same_key() {
        String k1 = generator.generate(TestData.request());
        String k2 = generator.generate(TestData.request());
        assertEquals(k1, k2);
    }

    @Test
    void different_request_produces_different_key() {
        String k1 = generator.generate(TestData.request());
        String k2 = generator.generate(TestData.otherRequest());
        assertNotEquals(k1, k2);
    }
}

