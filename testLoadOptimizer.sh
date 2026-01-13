#!/usr/bin/env bash

API_URL="http://localhost:8080/api/v1/load-optimizer/optimize"
LOG_FILE="load_optimizer_test.log"

GREEN="\033[0;32m"
RED="\033[0;31m"
CYAN="\033[0;36m"
NC="\033[0m"

echo "==========================================" | tee $LOG_FILE
echo " Optimal Truck Load Planner - Test Suite  " | tee -a $LOG_FILE
echo "==========================================" | tee -a $LOG_FILE
echo "" | tee -a $LOG_FILE

now_ms() {
  python3 -c 'import time; print(int(time.time() * 1000))'
}

run_test() {
  local name="$1"
  local payload="$2"
  local expected_status="$3"

  echo -e "${CYAN}Running test:${NC} $name" | tee -a $LOG_FILE

  start_time=$(now_ms)

  response=$(curl -s -w "\n%{http_code}" -X POST "$API_URL" \
    -H "Content-Type: application/json" \
    -d "$payload")

  end_time=$(now_ms)
  duration=$((end_time - start_time))

  status=$(echo "$response" | tail -n 1)
  body=$(echo "$response" | sed '$d')

  if [[ "$status" == "$expected_status" ]]; then
    echo -e "${GREEN}PASS${NC} | HTTP $status | ${duration} ms" | tee -a $LOG_FILE
  else
    echo -e "${RED}FAIL${NC} | Expected $expected_status, got $status | ${duration} ms" | tee -a $LOG_FILE
  fi

  echo -e "${CYAN}Response body:${NC}"
  echo "$body" | jq '.' 2>/dev/null || echo "$body"

  echo "$body" | jq '.' >> $LOG_FILE 2>/dev/null || echo "$body" >> $LOG_FILE
  echo "------------------------------------------" | tee -a $LOG_FILE
}

# ------------------------------------------------------------------
# Common truck block (snake_case, matches DTO)
# ------------------------------------------------------------------

TRUCK_BLOCK='"truck":{
  "id":"truck-1",
  "max_weight_lbs":44000,
  "max_volume_cuft":3000
}'

# ------------------------------------------------------------------
# 1. Simple valid case
# ------------------------------------------------------------------
run_test "Simple valid case" "{
  $TRUCK_BLOCK,
  \"orders\":[
    {
      \"id\":\"o1\",
      \"payout_cents\":150000,
      \"weight_lbs\":12000,
      \"volume_cuft\":800,
      \"origin\":\"LA\",
      \"destination\":\"TX\",
      \"pickup_date\":\"2025-01-01\",
      \"delivery_date\":\"2025-01-03\",
      \"is_hazmat\":false
    },
    {
      \"id\":\"o2\",
      \"payout_cents\":200000,
      \"weight_lbs\":15000,
      \"volume_cuft\":900,
      \"origin\":\"LA\",
      \"destination\":\"TX\",
      \"pickup_date\":\"2025-01-01\",
      \"delivery_date\":\"2025-01-04\",
      \"is_hazmat\":false
    }
  ]
}" 200

# ------------------------------------------------------------------
# 2. Empty orders list
# ------------------------------------------------------------------
run_test "Empty orders list" "{
  $TRUCK_BLOCK,
  \"orders\":[]
}" 400

# ------------------------------------------------------------------
# 3. Invalid pickup/delivery dates
# ------------------------------------------------------------------
run_test "Invalid pickup/delivery dates" "{
  $TRUCK_BLOCK,
  \"orders\":[
    {
      \"id\":\"o1\",
      \"payout_cents\":100000,
      \"weight_lbs\":1000,
      \"volume_cuft\":100,
      \"origin\":\"LA\",
      \"destination\":\"TX\",
      \"pickup_date\":\"2025-02-10\",
      \"delivery_date\":\"2025-02-01\",
      \"is_hazmat\":false
    }
  ]
}" 200

# ------------------------------------------------------------------
# 4. Hazmat conflict
# ------------------------------------------------------------------
run_test "Hazmat conflict" "{
  $TRUCK_BLOCK,
  \"orders\":[
    {
      \"id\":\"h1\",
      \"payout_cents\":100000,
      \"weight_lbs\":5000,
      \"volume_cuft\":300,
      \"origin\":\"LA\",
      \"destination\":\"TX\",
      \"pickup_date\":\"2025-01-01\",
      \"delivery_date\":\"2025-01-03\",
      \"is_hazmat\":true
    },
    {
      \"id\":\"n1\",
      \"payout_cents\":200000,
      \"weight_lbs\":6000,
      \"volume_cuft\":400,
      \"origin\":\"LA\",
      \"destination\":\"TX\",
      \"pickup_date\":\"2025-01-01\",
      \"delivery_date\":\"2025-01-03\",
      \"is_hazmat\":false
    }
  ]
}" 200

# ------------------------------------------------------------------
# 5. Single order exceeds capacity
# ------------------------------------------------------------------
run_test "Single order exceeds capacity" "{
  $TRUCK_BLOCK,
  \"orders\":[
    {
      \"id\":\"big1\",
      \"payout_cents\":500000,
      \"weight_lbs\":60000,
      \"volume_cuft\":5000,
      \"origin\":\"LA\",
      \"destination\":\"TX\",
      \"pickup_date\":\"2025-01-01\",
      \"delivery_date\":\"2025-01-03\",
      \"is_hazmat\":false
    }
  ]
}" 200

# ------------------------------------------------------------------
# 6. No feasible combination
# ------------------------------------------------------------------
run_test "No feasible combination" "{
  $TRUCK_BLOCK,
  \"orders\":[
    {
      \"id\":\"b1\",
      \"payout_cents\":200000,
      \"weight_lbs\":50000,
      \"volume_cuft\":4000,
      \"origin\":\"LA\",
      \"destination\":\"TX\",
      \"pickup_date\":\"2025-01-01\",
      \"delivery_date\":\"2025-01-03\",
      \"is_hazmat\":false
    },
    {
      \"id\":\"b2\",
      \"payout_cents\":300000,
      \"weight_lbs\":55000,
      \"volume_cuft\":4500,
      \"origin\":\"LA\",
      \"destination\":\"TX\",
      \"pickup_date\":\"2025-01-01\",
      \"delivery_date\":\"2025-01-03\",
      \"is_hazmat\":false
    }
  ]
}" 200

# ------------------------------------------------------------------
# 7. 22 orders (non-hazmat)
# ------------------------------------------------------------------
ORDERS_22=$(jq -nc '
[range(1;23) | {
  id: ("o"+(.|tostring)),
  payout_cents: (100000 + .),
  weight_lbs: 1000,
  volume_cuft: 100,
  origin:"LA",
  destination:"TX",
  pickup_date:"2025-01-01",
  delivery_date:"2025-01-05",
  is_hazmat:false
}]')

run_test "22 orders - normal" "{
  $TRUCK_BLOCK,
  \"orders\":$ORDERS_22
}" 200

# ------------------------------------------------------------------
# 8. 22 orders (hazmat only)
# ------------------------------------------------------------------
ORDERS_22_HAZ=$(jq -nc '
[range(1;23) | {
  id: ("h"+(.|tostring)),
  payout_cents: (150000 + .),
  weight_lbs: 1000,
  volume_cuft: 100,
  origin:"LA",
  destination:"TX",
  pickup_date:"2025-01-01",
  delivery_date:"2025-01-05",
  is_hazmat:true
}]')

run_test "22 orders - hazmat only" "{
  $TRUCK_BLOCK,
  \"orders\":$ORDERS_22_HAZ
}" 200

# ------------------------------------------------------------------
# 9. Route mismatch
# ------------------------------------------------------------------
run_test "Route mismatch" "{
  $TRUCK_BLOCK,
  \"orders\":[
    {
      \"id\":\"o1\",
      \"payout_cents\":100000,
      \"weight_lbs\":1000,
      \"volume_cuft\":100,
      \"origin\":\"LA\",
      \"destination\":\"TX\",
      \"pickup_date\":\"2025-01-01\",
      \"delivery_date\":\"2025-01-03\",
      \"is_hazmat\":false
    },
    {
      \"id\":\"o2\",
      \"payout_cents\":200000,
      \"weight_lbs\":1000,
      \"volume_cuft\":100,
      \"origin\":\"NY\",
      \"destination\":\"TX\",
      \"pickup_date\":\"2025-01-01\",
      \"delivery_date\":\"2025-01-03\",
      \"is_hazmat\":false
    }
  ]
}" 200

echo ""
echo "==========================================" | tee -a $LOG_FILE
echo " Test execution completed" | tee -a $LOG_FILE
echo " Logs written to $LOG_FILE"
echo "=========================================="
