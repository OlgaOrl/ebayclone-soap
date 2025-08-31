
#!/usr/bin/env bash
set -euo pipefail

# Simple SOAP sanity tests using curl only (no Java/Maven required)
# - Checks WSDL endpoints
# - Sends basic SOAP requests
# - Verifies expected content in responses
#
# Usage:
#   ./scripts/test_soap.sh            # uses http://localhost:8080 by default
#   BASE_URL=http://127.0.0.1:8080 ./scripts/test_soap.sh

BASE_URL=${BASE_URL:-http://localhost:8080}
USER_URL="$BASE_URL/soap/user"
PRODUCT_URL="$BASE_URL/soap/product"
AUCTION_URL="$BASE_URL/soap/auction"
ORDER_URL="$BASE_URL/soap/order"

pass() { echo "[PASS] $*"; }
fail() { echo "[FAIL] $*" 1>&2; exit 1; }
require() { command -v "$1" >/dev/null 2>&1 || fail "Missing required command: $1"; }

require curl

# curl helpers
curl_save() {
  # $1=url, $2=outbody
  local url="$1" out="$2"
  curl -sS -D /tmp/headers.$$ -o "$out" "$url" || true
  # Return code not used to allow reading body on 4xx/5xx (SOAP Fault)
  local code
  code=$(awk 'tolower($1) == "http/1.1" { code=$2 } tolower($1) == "http/2" { code=$2 } END{ print code }' /tmp/headers.$$)
  echo "$code"
}

curl_post_xml() {
  # $1=url, $2=xmlfile, $3=outbody
  local url="$1" xml="$2" out="$3"
  curl -sS -D /tmp/headers.$$ -H "Content-Type: text/xml; charset=utf-8" --data-binary "@$xml" "$url" -o "$out" || true
  local code
  code=$(awk 'tolower($1) == "http/1.1" { code=$2 } tolower($1) == "http/2" { code=$2 } END{ print code }' /tmp/headers.$$)
  echo "$code"
}

# 1) WSDL availability checks
check_wsdl() {
  local name="$1" url="$2?wsdl"
  local body
  body=$(mktemp)
  local code
  code=$(curl_save "$url" "$body")
  [[ "$code" == "200" ]] || fail "$name WSDL: expected 200, got $code"
  grep -qi "<definitions" "$body" || fail "$name WSDL: definitions element not found"
  pass "$name WSDL reachable ($url)"
}

check_wsdl "UserService" "$USER_URL"
check_wsdl "ProductService" "$PRODUCT_URL"
check_wsdl "AuctionService" "$AUCTION_URL"
check_wsdl "OrderService" "$ORDER_URL"

# 2) UserService.registerUser (BARE) positive test
# Namespace for types per annotations: http://ebay.clone.soap/types
USER_REQ=$(mktemp)
cat > "$USER_REQ" <<'XML'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:typ="http://ebay.clone.soap/types">
  <soapenv:Header/>
  <soapenv:Body>
    <typ:request>
      <username>alice</username>
      <email>alice@example.com</email>
      <password>secret12</password>
    </typ:request>
  </soapenv:Body>
</soapenv:Envelope>
XML
USER_RESP=$(mktemp)
code=$(curl_post_xml "$USER_URL" "$USER_REQ" "$USER_RESP")
[[ "$code" == "200" ]] || fail "registerUser: expected HTTP 200, got $code"
grep -q "<status>SUCCESS</status>" "$USER_RESP" || fail "registerUser: SUCCESS not found in response"
pass "UserService.registerUser SUCCESS"

# 3) ProductService.getProduct negative test (expect SOAP Fault for unknown id)
PROD_REQ=$(mktemp)
if [[ -n "${AUTH_TOKEN:-}" ]]; then
cat > "$PROD_REQ" <<XML
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:srv="http://ebay.clone.soap/service" xmlns:auth="http://ebay.clone.soap/auth">
  <soapenv:Header>
    <auth:AuthToken>${AUTH_TOKEN}</auth:AuthToken>
  </soapenv:Header>
  <soapenv:Body>
    <srv:getProduct>
      <productId>999999</productId>
    </srv:getProduct>
  </soapenv:Body>
</soapenv:Envelope>
XML
else
cat > "$PROD_REQ" <<'XML'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:srv="http://ebay.clone.soap/service">
  <soapenv:Header/>
  <soapenv:Body>
    <srv:getProduct>
      <productId>999999</productId>
    </srv:getProduct>
  </soapenv:Body>
</soapenv:Envelope>
XML
fi
PROD_RESP=$(mktemp)
code=$(curl_post_xml "$PRODUCT_URL" "$PROD_REQ" "$PROD_RESP")
# CXF typically returns 500 for SOAP 1.1 Faults; tolerate 200 with Fault too
if [[ "$code" != "500" && "$code" != "200" ]]; then
  fail "getProduct: unexpected HTTP $code"
fi
grep -qi "<faultcode>|<Fault" "$PROD_RESP" || grep -qi "NOT_FOUND" "$PROD_RESP" || fail "getProduct: expected SOAP Fault or NOT_FOUND in response"
pass "ProductService.getProduct fault on unknown id"

# 4) ProductService.searchProducts simple call (wrapped, with nullable params)
SEARCH_REQ=$(mktemp)
cat > "$SEARCH_REQ" <<'XML'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:srv="http://ebay.clone.soap/service" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header/>
  <soapenv:Body>
    <srv:searchProducts>
      <keyword>test</keyword>
      <category xsi:nil="true"/>
      <maxPrice xsi:nil="true"/>
    </srv:searchProducts>
  </soapenv:Body>
</soapenv:Envelope>
XML
SEARCH_RESP=$(mktemp)
code=$(curl_post_xml "$PRODUCT_URL" "$SEARCH_REQ" "$SEARCH_RESP")
[[ "$code" == "200" ]] || fail "searchProducts: expected HTTP 200, got $code"
# Expect an XML list (might be empty); verify it's XML and mentions 'response'
grep -qi "<.*response.*>" "$SEARCH_RESP" || grep -qi "<soapenv:Body>" "$SEARCH_RESP" || fail "searchProducts: expected XML response content"
pass "ProductService.searchProducts basic call"

echo "All checks passed."





