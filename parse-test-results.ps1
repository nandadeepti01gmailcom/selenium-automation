# Test Results XML Parser
# Reads and displays detailed test execution information from Surefire XML reports

param(
    [string]$ReportsPath = "target/surefire-reports"
)

Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host "             TEST EXECUTION DETAILED REPORT                     " -ForegroundColor Cyan
Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host ""

if (-not (Test-Path $ReportsPath)) {
    Write-Host "ERROR: Reports directory not found: $ReportsPath" -ForegroundColor Red
    Write-Host "Please run tests first: mvn test" -ForegroundColor Yellow
    exit 1
}

# Find all XML test result files
$xmlFiles = Get-ChildItem -Path $ReportsPath -Filter "TEST-*.xml"

if ($xmlFiles.Count -eq 0) {
    Write-Host "ERROR: No test XML files found in $ReportsPath" -ForegroundColor Red
    Write-Host "Please run tests first: mvn test" -ForegroundColor Yellow
    exit 1
}

Write-Host "Found $($xmlFiles.Count) test suite(s)" -ForegroundColor Green
Write-Host ""

$totalTests = 0
$totalPassed = 0
$totalFailed = 0
$totalSkipped = 0
$totalErrors = 0
$allFailures = @()

foreach ($xmlFile in $xmlFiles) {
    Write-Host "=================================================================" -ForegroundColor Yellow
    Write-Host "Test Suite: $($xmlFile.BaseName -replace 'TEST-', '')" -ForegroundColor Yellow
    Write-Host "=================================================================" -ForegroundColor Yellow
    Write-Host ""
    
    [xml]$xml = Get-Content $xmlFile.FullName
    $testsuite = $xml.testsuite
    
    # Test suite summary
    $tests = [int]$testsuite.tests
    $failures = [int]$testsuite.failures
    $errors = [int]$testsuite.errors
    $skipped = [int]$testsuite.skipped
    $passed = $tests - $failures - $errors - $skipped
    $time = [double]$testsuite.time
    
    $totalTests += $tests
    $totalPassed += $passed
    $totalFailed += $failures
    $totalErrors += $errors
    $totalSkipped += $skipped
    
    Write-Host "Test Suite Information:" -ForegroundColor Cyan
    Write-Host "  Name:      $($testsuite.name)" -ForegroundColor White
    Write-Host "  Time:      $($testsuite.timestamp)" -ForegroundColor White
    Write-Host "  Duration:  $time seconds" -ForegroundColor White
    Write-Host ""
    
    Write-Host "Test Results:" -ForegroundColor Cyan
    Write-Host "  Total:     $tests" -ForegroundColor White
    Write-Host "  Passed:    $passed" -ForegroundColor Green
    Write-Host "  Failed:    $failures" -ForegroundColor $(if ($failures -gt 0) { "Red" } else { "White" })
    Write-Host "  Errors:    $errors" -ForegroundColor $(if ($errors -gt 0) { "Red" } else { "White" })
    Write-Host "  Skipped:   $skipped" -ForegroundColor $(if ($skipped -gt 0) { "Yellow" } else { "White" })
    Write-Host ""
    
    # System properties
    if ($testsuite.properties.property) {
        Write-Host "Environment Details:" -ForegroundColor Cyan
        foreach ($prop in $testsuite.properties.property) {
            if ($prop.name -like "test.*") {
                Write-Host "  $($prop.name): $($prop.value)" -ForegroundColor White
            }
        }
        Write-Host ""
    }
    
    # Test cases details
    Write-Host "Test Cases:" -ForegroundColor Cyan
    Write-Host ""
    
    $testcases = $testsuite.testcase
    if ($testcases -isnot [array]) {
        $testcases = @($testcases)
    }
    
    foreach ($testcase in $testcases) {
        $testName = $testcase.name
        $className = $testcase.classname
        $testTime = [double]$testcase.time
        
        # Determine test status
        $status = "PASSED"
        $statusColor = "Green"
        $icon = "[PASS]"
        
        if ($testcase.failure) {
            $status = "FAILED"
            $statusColor = "Red"
            $icon = "[FAIL]"
            $failureInfo = @{
                TestSuite = $testsuite.name
                TestName = $testName
                ClassName = $className
                Message = $testcase.failure.message
                Type = $testcase.failure.type
                Details = $testcase.failure.'#text'
                Time = $testTime
            }
            $allFailures += $failureInfo
        }
        elseif ($testcase.error) {
            $status = "ERROR"
            $statusColor = "Red"
            $icon = "[ERR ]"
            $failureInfo = @{
                TestSuite = $testsuite.name
                TestName = $testName
                ClassName = $className
                Message = $testcase.error.message
                Type = $testcase.error.type
                Details = $testcase.error.'#text'
                Time = $testTime
            }
            $allFailures += $failureInfo
        }
        elseif ($testcase.skipped) {
            $status = "SKIPPED"
            $statusColor = "Yellow"
            $icon = "[SKIP]"
        }
        
        Write-Host "  $icon " -NoNewline -ForegroundColor $statusColor
        Write-Host "$testName " -NoNewline -ForegroundColor White
        Write-Host "($testTime s)" -ForegroundColor Gray
        
        if ($status -ne "PASSED" -and $testcase.failure) {
            Write-Host "         Reason: $($testcase.failure.message)" -ForegroundColor Red
        }
        elseif ($status -ne "PASSED" -and $testcase.error) {
            Write-Host "         Reason: $($testcase.error.message)" -ForegroundColor Red
        }
    }
    Write-Host ""
}

# Overall Summary
Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host "                    OVERALL SUMMARY                             " -ForegroundColor Cyan
Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Total Test Suites: $($xmlFiles.Count)" -ForegroundColor White
Write-Host "Total Tests:       $totalTests" -ForegroundColor White
Write-Host "Passed:            $totalPassed" -ForegroundColor Green
Write-Host "Failed:            $totalFailed" -ForegroundColor $(if ($totalFailed -gt 0) { "Red" } else { "Green" })
Write-Host "Errors:            $totalErrors" -ForegroundColor $(if ($totalErrors -gt 0) { "Red" } else { "Green" })
Write-Host "Skipped:           $totalSkipped" -ForegroundColor $(if ($totalSkipped -gt 0) { "Yellow" } else { "White" })
Write-Host ""

# Detailed failure information
if ($allFailures.Count -gt 0) {
    Write-Host "=================================================================" -ForegroundColor Red
    Write-Host "                  DETAILED FAILURE REPORT                       " -ForegroundColor Red
    Write-Host "=================================================================" -ForegroundColor Red
    Write-Host ""
    
    $failureNum = 1
    foreach ($failure in $allFailures) {
        Write-Host "Failure #$failureNum" -ForegroundColor Red
        Write-Host "-----------------------------------------------------------------" -ForegroundColor Gray
        Write-Host "Test Suite:  $($failure.TestSuite)" -ForegroundColor White
        Write-Host "Test Name:   $($failure.TestName)" -ForegroundColor White
        Write-Host "Class:       $($failure.ClassName)" -ForegroundColor White
        Write-Host "Type:        $($failure.Type)" -ForegroundColor Yellow
        Write-Host "Message:     $($failure.Message)" -ForegroundColor Red
        Write-Host "Duration:    $($failure.Time) seconds" -ForegroundColor White
        Write-Host ""
        Write-Host "Stack Trace:" -ForegroundColor Cyan
        Write-Host $failure.Details -ForegroundColor Gray
        Write-Host ""
        $failureNum++
    }
}

# Success or failure summary
Write-Host "=================================================================" -ForegroundColor Cyan
if ($totalFailed -eq 0 -and $totalErrors -eq 0) {
    Write-Host "               ALL TESTS PASSED SUCCESSFULLY!                   " -ForegroundColor Green
} else {
    Write-Host "           TESTS FAILED - REVIEW DETAILS ABOVE                 " -ForegroundColor Red
}
Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host ""

# Export detailed JSON report
$jsonReport = @{
    timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    summary = @{
        totalSuites = $xmlFiles.Count
        totalTests = $totalTests
        passed = $totalPassed
        failed = $totalFailed
        errors = $totalErrors
        skipped = $totalSkipped
    }
    failures = $allFailures
}

$jsonPath = "$ReportsPath/test-execution-summary.json"
$jsonReport | ConvertTo-Json -Depth 10 | Out-File -FilePath $jsonPath -Encoding UTF8
Write-Host "Detailed JSON report saved to: $jsonPath" -ForegroundColor Cyan
