# Combined Test Report Setup

## Overview
The ExtentReportListener has been updated to generate a **single combined HTML report** for all test classes instead of separate reports for each test class.

## What Changed

### Key Improvements
1. **Single Combined Report**: All test classes now write to one HTML report file
2. **Synchronized Initialization**: Thread-safe initialization ensures only one report is created
3. **Atomic Counters**: Thread-safe counters for accurate test statistics across all test classes
4. **Automatic Flush**: Report is automatically flushed when all tests complete using a shutdown hook

### Technical Changes
- Used `AtomicBoolean` and `AtomicInteger` for thread-safe operations
- Added synchronized initialization block to prevent multiple report creation
- Implemented shutdown hook to ensure report is flushed at the end
- Report filename changed to `CombinedTestReport_[timestamp].html`
- Test names now include class name for better identification: `ClassName - testMethodName`

## How It Works

1. **First Test Class**: Initializes the combined report (only once)
2. **Subsequent Test Classes**: Use the same shared report instance
3. **All Tests**: Write to the same report file
4. **End of Execution**: Shutdown hook automatically flushes the report

## Usage

### Running All Tests
```bash
mvn clean test
```

### Running Specific Test Classes
```bash
mvn test -Dtest=LoginTest
mvn test -Dtest=DashboardTest
```

### Running Multiple Test Classes
```bash
mvn test -Dtest=LoginTest,DashboardTest
```

## Report Location

After test execution, the combined report will be generated at:
```
test-reports/CombinedTestReport_[YYYY-MM-DD_HH-mm-ss].html
```

## Report Features

- **Combined View**: All test classes and methods in one report
- **Test Organization**: Tests grouped by class name
- **Statistics**: Combined totals for all tests (passed, failed, skipped)
- **Execution Time**: Total execution time across all test classes
- **System Information**: OS, Java version, Browser, Environment details

## Additional Reports

The system still generates:
- **DetailedReport**: Detailed test case execution report
- **TestSummary**: Summary statistics report

## Manual Report Flush

If needed, you can manually flush the report:
```java
ExtentReportListener.flushReportManually();
```

## Benefits

1. **Single Source of Truth**: One report file for all test results
2. **Easier Analysis**: View all test results in one place
3. **Better Organization**: Tests organized by class name
4. **Accurate Statistics**: Combined totals across all test classes
5. **Simplified Reporting**: No need to merge multiple reports

## Example Output

When you run tests, you'll see:
```
✓ Combined Extent Report initialized: test-reports/CombinedTestReport_2026-01-26_15-45-00.html
✓ Test PASSED: LoginTest - testLoginPageTitle
✓ Test PASSED: LoginTest - testLoginPageURL
✓ Test PASSED: DashboardTest - testDashboardMenus
✓ Combined Extent Report generated successfully: test-reports/CombinedTestReport_2026-01-26_15-45-00.html
```

## Notes

- The report is automatically flushed when the JVM shuts down
- All test classes must use `@ExtendWith(ExtentReportListener.class)` annotation
- The report includes all tests from all test classes in a single HTML file
- Previous individual reports will still be generated if you have old code, but new runs will use the combined report
