package com.teleport.optimal_truck_load_planner.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class helloController {

    @GetMapping(value = "/")
    public ResponseEntity<String> readme() throws IOException {

        return ResponseEntity.ok("Welcome to Truck optimizer Engine");
    }
}
