
#!/usr/bin/env pwsh
Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

# Simple SOAP sanity tests using Invoke-WebRequest only (no Java/Maven required)
# Usage:
#   ./scripts/test_soap.ps1                # uses http://localhost:8080 by default
#   $env:BASE_URL = 'http://127.0.0.1:8080'; ./scripts/test_soap.ps1

$BaseUrl = if ($env:BASE_URL) { $env:BASE_URL } else { 'http://localhost:8080' }
$UserUrl = "$BaseUrl/soap/user"
$ProductUrl = "$BaseUrl/soap/product"
$AuctionUrl = "$BaseUrl/soap/auction"
$OrderUrl = "$BaseUrl/soap/order"

function Pass([string]$msg) { Write-Host "[PASS] $msg" -ForegroundColor Green }
function Fail([string]$msg) { Write-Host "[FAIL] $msg" -ForegroundColor Red; exit 1 }

function Get-Http($url) {
  try {
    $resp = Invoke-WebRequest -UseBasicParsing -Uri $url -Method GET -ContentType 'text/xml; charset=utf-8'
    return @{ Code = 200; Body = $resp.Content }
  } catch {
    if ($_.Exception.Response -ne $null) {
      $code = [int]$_.Exception.Response.StatusCode
      $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
      $body = $reader.ReadToEnd()
      return @{ Code = $code; Body = $body }
    } else { throw }
  }
}

function Post-Xml($url, [string]$xml) {
  try {
    $resp = Invoke-WebRequest -UseBasicParsing -Uri $url -Method POST -ContentType 'text/xml; charset=utf-8' -Body $xml
    return @{ Code = 200; Body = $resp.Content }
  } catch {
    if ($_.Exception.Response -ne $null) {
      $code = [int]$_.Exception.Response.StatusCode
      $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
      $body = $reader.ReadToEnd()
      return @{ Code = $code; Body = $body }
    } else { throw }
  }
}

# 1) WSDL availability
$wsdls = @(
  @{ Name = 'UserService'; Url = "$UserUrl?wsdl" },
  @{ Name = 'ProductService'; Url = "$ProductUrl?wsdl" },
  @{ Name = 'AuctionService'; Url = "$AuctionUrl?wsdl" },
  @{ Name = 'OrderService'; Url = "$OrderUrl?wsdl" }
)
foreach ($w in $wsdls) {
  $r = Get-Http $w.Url
  if ($r.Code -ne 200) { Fail "$($w.Name) WSDL: expected 200, got $($r.Code)" }
  if ($r.Body -notmatch '<definitions' -and $r.Body -notmatch '<wsdl:definitions') { Fail "$($w.Name) WSDL: definitions element not found" }
  Pass "$($w.Name) WSDL reachable ($($w.Url))"
}

# 2) UserService.registerUser (BARE)
$registerXml = @"
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
"@
$r = Post-Xml $UserUrl $registerXml
if ($r.Code -ne 200) { Fail "registerUser: expected HTTP 200, got $($r.Code)" }
if ($r.Body -notmatch '<status>SUCCESS</status>') { Fail 'registerUser: SUCCESS not found in response' }
Pass 'UserService.registerUser SUCCESS'

# 3) ProductService.getProduct negative
$getProductXml = @"
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:srv="http://ebay.clone.soap/service">
  <soapenv:Header/>
  <soapenv:Body>
    <srv:getProduct>
      <productId>999999</productId>
    </srv:getProduct>
  </soapenv:Body>
</soapenv:Envelope>
"@
$r = Post-Xml $ProductUrl $getProductXml
if (($r.Code -ne 500) -and ($r.Code -ne 200)) { Fail "getProduct: unexpected HTTP $($r.Code)" }
if (($r.Body -notmatch '<Fault') -and ($r.Body -notmatch 'NOT_FOUND')) { Fail 'getProduct: expected SOAP Fault or NOT_FOUND' }
Pass 'ProductService.getProduct fault on unknown id'

# 4) ProductService.searchProducts basic call
$searchXml = @"
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
"@
$r = Post-Xml $ProductUrl $searchXml
if ($r.Code -ne 200) { Fail "searchProducts: expected HTTP 200, got $($r.Code)" }
if (($r.Body -notmatch '<response') -and ($r.Body -notmatch '<soapenv:Body')) { Fail 'searchProducts: expected XML response content' }
Pass 'ProductService.searchProducts basic call'

Write-Host 'All checks passed.' -ForegroundColor Green




