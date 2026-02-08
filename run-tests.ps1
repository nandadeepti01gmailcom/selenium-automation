# Test Execution Script
# Provides easy commands to run tests with different tag combinations

param(
    [Parameter(Position=0)]
    [string]$TestSuite = "help"
)

function Show-Help {
    Write-Host "`n==== Selenium Test Execution Script ====" -ForegroundColor Cyan
    Write-Host "`nUsage: .\run-tests.ps1 <test-suite>`n" -ForegroundColor Yellow
    
    Write-Host "Available Test Suites:" -ForegroundColor Green
    Write-Host "  smoke          - Quick smoke tests (critical functionality)" -ForegroundColor White
    Write-Host "  regression     - Full regression test suite" -ForegroundColor White
    Write-Host "  sanity         - Quick sanity checks" -ForegroundColor White
    Write-Host "  login          - All login functionality tests" -ForegroundColor White
    Write-Host "  dashboard      - All dashboard tests" -ForegroundColor White
    Write-Host "  positive       - All positive/happy path tests" -ForegroundColor White
    Write-Host "  negative       - All negative/error handling tests" -ForegroundColor White
    Write-Host "  integration    - Integration tests (Slack, APIs, etc)" -ForegroundColor White
    Write-Host "  ui             - All UI tests" -ForegroundColor White
    Write-Host "  critical       - P0 priority tests only" -ForegroundColor White
    Write-Host "  high-priority  - P0 and P1 tests" -ForegroundColor White
    Write-Host "  all            - Run all tests" -ForegroundColor White
    Write-Host "  custom         - Prompts for custom tag input`n" -ForegroundColor White
    
    Write-Host "Examples:" -ForegroundColor Green
    Write-Host "  .\run-tests.ps1 smoke" -ForegroundColor Gray
    Write-Host "  .\run-tests.ps1 regression" -ForegroundColor Gray
    Write-Host "  .\run-tests.ps1 negative`n" -ForegroundColor Gray
}

function Run-Tests {
    param(
        [string]$IncludeTags,
        [string]$ExcludeTags = "",
        [string]$Description
    )
    
    Write-Host "`n========================================" -ForegroundColor Cyan
    Write-Host "Running: $Description" -ForegroundColor Yellow
    Write-Host "Include Tags: $IncludeTags" -ForegroundColor Green
    if ($ExcludeTags) {
        Write-Host "Exclude Tags: $ExcludeTags" -ForegroundColor Red
    }
    Write-Host "========================================`n" -ForegroundColor Cyan
    
    $mvnCommand = "mvn clean test -DincludeTags=$IncludeTags"
    if ($ExcludeTags) {
        $mvnCommand += " -DexcludeTags=$ExcludeTags"
    }
    
    Write-Host "Executing: $mvnCommand`n" -ForegroundColor Gray
    Invoke-Expression $mvnCommand
    
    $exitCode = $LASTEXITCODE
    if ($exitCode -eq 0) {
        Write-Host "`n✓ Tests completed successfully!" -ForegroundColor Green
    } else {
        Write-Host "`n✗ Tests failed with exit code: $exitCode" -ForegroundColor Red
    }
    
    Write-Host "`nCheck test reports in: test-reports\" -ForegroundColor Cyan
}

# Main execution logic
switch ($TestSuite.ToLower()) {
    "smoke" {
        Run-Tests -IncludeTags "smoke" -ExcludeTags "wip,slow" -Description "Smoke Tests (Quick Critical Tests)"
    }
    "regression" {
        Run-Tests -IncludeTags "regression" -ExcludeTags "wip" -Description "Full Regression Test Suite"
    }
    "sanity" {
        Run-Tests -IncludeTags "sanity" -Description "Sanity Tests"
    }
    "login" {
        Run-Tests -IncludeTags "login" -Description "Login Functionality Tests"
    }
    "dashboard" {
        Run-Tests -IncludeTags "dashboard" -Description "Dashboard Tests"
    }
    "positive" {
        Run-Tests -IncludeTags "positive" -Description "Positive/Happy Path Tests"
    }
    "negative" {
        Run-Tests -IncludeTags "negative" -Description "Negative Test Cases"
    }
    "edge" {
        Run-Tests -IncludeTags "edge" -Description "Edge Cases and Boundary Tests"
    }
    "integration" {
        Run-Tests -IncludeTags "integration" -Description "Integration Tests (Slack, External Services)"
    }
    "ui" {
        Run-Tests -IncludeTags "ui" -ExcludeTags "api,database" -Description "UI Tests Only"
    }
    "critical" {
        Run-Tests -IncludeTags "p0" -Description "Critical Priority (P0) Tests"
    }
    "high-priority" {
        Run-Tests -IncludeTags "p0 | p1" -Description "High Priority (P0 + P1) Tests"
    }
    "all" {
        Run-Tests -IncludeTags "" -ExcludeTags "wip" -Description "All Tests (Excluding WIP)"
        Write-Host "`nNote: Running all tests without tag filter" -ForegroundColor Yellow
        mvn clean test -DexcludeTags=wip
    }
    "custom" {
        Write-Host "`n==== Custom Test Execution ====" -ForegroundColor Cyan
        $includeTags = Read-Host "Enter tags to include (e.g., smoke & login)"
        $excludeTags = Read-Host "Enter tags to exclude (optional, press Enter to skip)"
        
        if ([string]::IsNullOrWhiteSpace($excludeTags)) {
            Run-Tests -IncludeTags $includeTags -Description "Custom Test Execution"
        } else {
            Run-Tests -IncludeTags $includeTags -ExcludeTags $excludeTags -Description "Custom Test Execution"
        }
    }
    "help" {
        Show-Help
    }
    default {
        Write-Host "`n✗ Unknown test suite: $TestSuite" -ForegroundColor Red
        Show-Help
    }
}
