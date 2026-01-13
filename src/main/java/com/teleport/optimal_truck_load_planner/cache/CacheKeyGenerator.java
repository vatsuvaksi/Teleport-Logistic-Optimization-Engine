package com.teleport.optimal_truck_load_planner.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component

public class CacheKeyGenerator {

//    public String generate(Object request) {
//        try {
//            String json = objectMapper.writeValueAsString(request);
//
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hash = digest.digest(json.getBytes(StandardCharsets.UTF_8));
//
//            return HexFormat.of().formatHex(hash);
//        } catch (Exception e) {
//            throw new IllegalStateException("Failed to generate cache key", e);
//        }
//    }
public String generate(Object request) {
    try {
        String canonical = String.valueOf(request);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(canonical.getBytes(StandardCharsets.UTF_8));

        return HexFormat.of().formatHex(hash);
    } catch (Exception e) {
        throw new IllegalStateException("Failed to generate cache key", e);
    }
}
}
