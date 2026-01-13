# Optimal Truck Load Planner

## Overview

This project implements a **production-grade, stateless load optimization service** for a logistics platform.
Given a truck and a set of shipment orders, the service selects the **optimal combination of orders** that maximizes carrier revenue while respecting all operational constraints.
---

## How to Run the Application

### Prerequisites

* Docker (recommended)
* OR Java 21 + Maven 3.9+

---

### Option 1: Run with Docker (Recommended)

```bash
git clone https://github.com/vatsuvaksi/Teleport-Logistic-Optimization-Engine.git
cd optimal-truck-load-planner

docker compose up --build
```

The service will be available at:

```
http://localhost:8080
```

Redis is automatically started via Docker Compose and used as an **optional cache**.

---

### Option 2: Run Locally (Without Docker)

```bash
mvn clean package
java -jar target/*.jar
```

In this mode, the application uses **in-memory caching** only.

---

### Example Request

```bash
curl -X POST http://localhost:8080/api/v1/load-optimizer/optimize \
  -H "Content-Type: application/json" \
  -d @sample-request.json
```

---

## Core Assumptions (As per Problem Statement)

* A single truck is optimized per request
* Truck has **fixed maximum weight and volume capacity**
* Orders are indivisible (0/1 selection)
* Money is handled strictly as **BigDecimal (integer cents conceptually)**
* All selected orders must be compatible:

    * Same origin → destination (single headhaul lane)
    * Pickup date ≤ delivery date
    * No conflicting time windows (simplified)
* Hazmat and non-hazmat orders **cannot be mixed**
* The service is **stateless** (no database)

---

## Why This Is a Knapsack-Style Problem

This problem maps directly to a **bounded 0/1 Knapsack optimization**:

* Each order is an *item*
* Profit = `payout_to_carrier`
* Costs = `weight` and `volume` (two-dimensional knapsack)
* Constraints = route, hazmat, time-window compatibility
* Number of orders is bounded (≤ 22), allowing exponential solutions

The implementation uses **bitmask-based dynamic programming** to evaluate all feasible subsets efficiently.

---

## Key Design Decisions & Features

### Architecture & Design

* **Clean Architecture** with strict separation of concerns
* Controllers are thin (no business logic)
* Domain logic is framework-agnostic
* Optimizer is independent of HTTP, caching, and persistence

---

### Optimization Engine

* Bitmask-based subset enumeration (`2^N`, N ≤ 22)
* Early pruning on capacity violations
* Deterministic and explainable results
* Guaranteed correctness over heuristics

---

### Constraint Strategy Pattern

* Each business rule is implemented as an independent **CompatibilityChecker**
* Constraints include:

    * Capacity
    * Hazmat isolation
    * Route compatibility
    * Time-window validation

**Why:**

* Open/Closed Principle
* Easy to add/remove rules
* Constraints are unit-testable in isolation

---

### Caching Strategy (Optional, Safe-by-Design)

* **In-memory cache** (default, always available)
* **Redis cache via Jedis** (enabled via profile)
* Cache key generated deterministically from request
* Short TTL (10 minutes)

**Why caching is used:**

* Optimization is CPU-bound
* Repeated requests with identical payloads are common
* Redis enables horizontal scalability
* Cache failures never affect correctness

---

### Error Handling & API Consistency

* Global exception handling
* Consistent JSON responses for all errors
* Correct HTTP status codes
* No stack traces leaked to clients

---

### General Request Flow

1. Request validation (DTO + manual checks)
2. Cache key generation
3. Cache lookup (Redis or in-memory)
4. Optimization execution on cache miss
5. Constraint validation per subset
6. Best-result selection
7. Response mapping

---

## Edge Case & Failure Behavior

| Case                           | Behavior             | Status    |
| ------------------------------ | -------------------- | --------- |
| Empty orders list              | Rejected             | 400       |
| Invalid dates / missing fields | Rejected             | 400       |
| Order > truck capacity         | Ignored by optimizer | 200       |
| No feasible combination        | Empty result         | 200       |
| Hazmat conflicts               | Optimizer excludes   | 200       |
| Redis down                     | Fallback to compute  | 200       |
| Optimizer bug / overflow       | Safe error           | 422 / 500 |
| Unexpected runtime error       | Consistent JSON      | 500       |

---

## Why This Solution Is Production-Ready

* Deterministic and correct optimization
* Predictable performance under strict limits
* Stateless and horizontally scalable
* Fault-tolerant caching strategy
* High test coverage (90%+)
* Dockerized with minimal runtime image

---

## Tech Stack

* Java 21
* Spring Boot 4
* JUnit 5 + Mockito
* Redis (Jedis)
* Docker & Docker Compose

---

## Final Note

This project prioritizes **correctness first**, followed by performance, extensibility, and operational safety exactly what is expected from a core logistics optimization service.
