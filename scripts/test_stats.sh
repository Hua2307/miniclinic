#!/usr/bin/env bash
set -euo pipefail

# Self-contained test script:
# 1) ensure appt_id=1 is BOOKED
# 2) login as D001
# 3) PUT /api/appointments/1/status {"status":"COMPLETED"}
# 4) GET /api/stats and assert byStatus.COMPLETED >= 1

DB=miniclinic.db
COOKIE=/tmp/miniclinic_test_cookies.txt

if ! command -v jq >/dev/null 2>&1; then
  echo "jq is required for this test. Install jq and retry." >&2
  exit 2
fi

echo "Resetting appointment 1 to BOOKED in ${DB}"
sqlite3 "${DB}" "UPDATE appointment SET status='BOOKED' WHERE appt_id=1;"

echo "Logging in as D001"
# capture response and status for debugging
LOGIN_RESP_FILE=$(mktemp)
HTTP_LOGIN_CODE=$(curl -s -c "${COOKIE}" -d "doctorId=D001&password=pass1234" -X POST http://localhost:8080/login -L -w "%{http_code}" -o "${LOGIN_RESP_FILE}")
echo "Login HTTP code: ${HTTP_LOGIN_CODE}"
if ! grep -q "JSESSIONID" "${COOKIE}" 2>/dev/null; then
  echo "Login cookie not found; dumping response for debugging:" >&2
  sed -n '1,200p' "${LOGIN_RESP_FILE}" >&2
  rm -f "${LOGIN_RESP_FILE}"
  exit 5
fi
rm -f "${LOGIN_RESP_FILE}"

echo "Setting appointment 1 -> COMPLETED via API"
HTTP_CODE=$(curl -s -b "${COOKIE}" -H "Content-Type: application/json" -X PUT -d '{"status":"COMPLETED"}' http://localhost:8080/api/appointments/1/status -w "%{http_code}" -o /dev/null)
echo "PUT returned HTTP ${HTTP_CODE}"
if [ "${HTTP_CODE}" != "200" ]; then
  echo "PUT failed with HTTP ${HTTP_CODE}" >&2
  exit 3
fi

echo "Fetching /api/stats"
STATS_JSON=$(curl -s http://localhost:8080/api/stats)
echo "Response: ${STATS_JSON}"

COMPLETED_COUNT=$(echo "${STATS_JSON}" | jq '.byStatus.COMPLETED // 0')
echo "byStatus.COMPLETED = ${COMPLETED_COUNT}"

if [ "${COMPLETED_COUNT}" -ge 1 ]; then
  echo "TEST PASS: byStatus.COMPLETED >= 1"
  exit 0
else
  echo "TEST FAIL: byStatus.COMPLETED < 1" >&2
  exit 4
fi
