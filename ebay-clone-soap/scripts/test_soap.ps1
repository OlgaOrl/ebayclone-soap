
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
$Token = if ($env:AUTH_TOKEN) { $env:AUTH_TOKEN } else { 'test-token' }

function Pass([string]$msg) { Write-Host "[PASS] $msg" -ForegroundColor Green }
function Fail([string]$msg) { Write-Host "[FAIL] $msg" -ForegroundColor Red; exit 1 }

function Get-Http($url) {
  try {
    $hasSkip = (Get-Command Invoke-WebRequest).Parameters.ContainsKey('SkipHttpErrorCheck')
    if ($hasSkip) {
      $resp = Invoke-WebRequest -UseBasicParsing -Uri $url -Method GET -ContentType 'text/xml; charset=utf-8' -SkipHttpErrorCheck
      return @{ Code = [int]$resp.StatusCode; Body = $resp.Content }
    } else {
      $resp = Invoke-WebRequest -UseBasicParsing -Uri $url -Method GET -ContentType 'text/xml; charset=utf-8'
      return @{ Code = 200; Body = $resp.Content }
    }
  } catch {
    if ($null -ne $_.Exception.Response) {
      $code = [int]$_.Exception.Response.StatusCode
      $stream = $_.Exception.Response.GetResponseStream()
      $body = if ($null -ne $stream) { (New-Object System.IO.StreamReader($stream)).ReadToEnd() } else { ($_.ErrorDetails.Message | Out-String) }
      return @{ Code = $code; Body = $body }
    } else { throw }
  }
}

function Post-Xml($url, [string]$xml) {
  try {
    $hasSkip = (Get-Command Invoke-WebRequest).Parameters.ContainsKey('SkipHttpErrorCheck')
    if ($hasSkip) {
      $resp = Invoke-WebRequest -UseBasicParsing -Uri $url -Method POST -ContentType 'text/xml; charset=utf-8' -Body $xml -SkipHttpErrorCheck
      $code = [int]$resp.StatusCode
      $body = $resp.Content
      if (-not $body -or $body.Length -eq 0) {
        if ($null -ne $resp.RawContent) {
          $body = $resp.RawContent
        } elseif ($null -ne $resp.RawContentStream) {
          try { $resp.RawContentStream.Position = 0 } catch {}
          $reader = New-Object System.IO.StreamReader($resp.RawContentStream)
          $body = $reader.ReadToEnd()
        }
      }
      return @{ Code = $code; Body = $body }
    } else {
      $resp = Invoke-WebRequest -UseBasicParsing -Uri $url -Method POST -ContentType 'text/xml; charset=utf-8' -Body $xml
      $body = $resp.Content
      return @{ Code = 200; Body = $body }
    }
  } catch {
    if ($null -ne $_.Exception.Response) {
      $code = [int]$_.Exception.Response.StatusCode
      $stream = $_.Exception.Response.GetResponseStream()
      $body = if ($null -ne $stream) { (New-Object System.IO.StreamReader($stream)).ReadToEnd() } else { ($_.ErrorDetails.Message | Out-String) }
      return @{ Code = $code; Body = $body }
    } else { throw }
  }
}

# 1) WSDL availability
$wsdls = @(
  @{ Name = 'UserService'; Url = "${UserUrl}?wsdl" },
  @{ Name = 'ProductService'; Url = "${ProductUrl}?wsdl" },
  @{ Name = 'AuctionService'; Url = "${AuctionUrl}?wsdl" },
  @{ Name = 'OrderService'; Url = "${OrderUrl}?wsdl" }
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
      <typ:username>alice_$([DateTimeOffset]::UtcNow.ToUnixTimeSeconds())</typ:username>
      <typ:email>alice_$([DateTimeOffset]::UtcNow.ToUnixTimeSeconds())@example.com</typ:email>
      <typ:password>secret12</typ:password>
    </typ:request>
  </soapenv:Body>
</soapenv:Envelope>
"@
$r = Post-Xml $UserUrl $registerXml
if ($r.Code -ne 200) { Fail "registerUser: expected HTTP 200, got $($r.Code)" }
if ($r.Body -notmatch '<status>SUCCESS</status>') { Fail 'registerUser: SUCCESS not found in response' }
Pass 'UserService.registerUser SUCCESS'

# 3) ProductService.getProduct negative (always include AuthToken)
$getProductXml = @"
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:srv="http://ebay.clone.soap/service" xmlns:typ="http://ebay.clone.soap/types" xmlns:auth="http://ebay.clone.soap/auth">
  <soapenv:Header>
    <auth:AuthToken>$Token</auth:AuthToken>
  </soapenv:Header>
  <soapenv:Body>
    <srv:getProduct>
      <typ:productId>999999</typ:productId>
    </srv:getProduct>
  </soapenv:Body>
</soapenv:Envelope>
"@
$r = Post-Xml $ProductUrl $getProductXml
if (($r.Code -ne 500) -and ($r.Code -ne 200)) { Fail "getProduct: unexpected HTTP $($r.Code)" }
$hasFault = ($r.Code -eq 500) -or ($r.Body -match 'Fault' -or $r.Body -match 'faultcode' -or $r.Body -match 'Product not found' -or $r.Body -match 'NOT_FOUND' -or $r.Body -match 'faultstring' -or $r.Body -match 'ServiceFault')
if (-not $hasFault) {
  Write-Host ("[DEBUG] getProduct HTTP $($r.Code)") -ForegroundColor Yellow
  $preview = if ($r.Body) { $r.Body.Substring(0, [Math]::Min(600, $r.Body.Length)) } else { '<empty body>' }
  Write-Host $preview
  Fail 'getProduct: expected SOAP Fault or NOT_FOUND'
}
Pass 'ProductService.getProduct fault on unknown id'

# 4) ProductService.searchProducts basic call (always include AuthToken)
$searchXml = @"
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:srv="http://ebay.clone.soap/service" xmlns:typ="http://ebay.clone.soap/types" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:auth="http://ebay.clone.soap/auth">
  <soapenv:Header>
    <auth:AuthToken>$Token</auth:AuthToken>
  </soapenv:Header>
  <soapenv:Body>
    <srv:searchProducts>
      <typ:keyword>test</typ:keyword>
    </srv:searchProducts>
  </soapenv:Body>
</soapenv:Envelope>
"@
$r = Post-Xml $ProductUrl $searchXml
if ($r.Code -ne 200) { Fail "searchProducts: expected HTTP 200, got $($r.Code)" }
if (-not $r.Body -or $r.Body.Trim().Length -eq 0) {
  Write-Host '[DEBUG] searchProducts: empty response body (HTTP 200)' -ForegroundColor Yellow
}
Pass 'ProductService.searchProducts basic call'

Write-Host 'All checks passed.' -ForegroundColor Green





