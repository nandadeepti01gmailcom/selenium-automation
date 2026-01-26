package com.example.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Extent Report Listener for JUnit 5
 * Generates comprehensive HTML reports for test execution
 * Reports are saved in: test-reports/ folder with timestamp
 * 
 * Features:
 * - Automatic report generation
 * - Test status tracking (PASS/FAIL/SKIP)
 * - Execution time tracking
 * - Screenshot attachment support
 * - Environment and system information
 * - Single combined report for all test classes
 * 
 * Usage:
 * Add @ExtendWith(ExtentReportListener.class) annotation to test class
 */
public class ExtentReportListener implements 
        BeforeAllCallback, 
        AfterAllCallback, 
        BeforeTestExecutionCallback, 
        AfterTestExecutionCallback {

    private static ExtentReports extentReports;
    private static ExtentTest extentTest;
    private static final String REPORT_PATH = "test-reports/";
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static final AtomicBoolean reportFlushed = new AtomicBoolean(false);
    private static final AtomicInteger totalTests = new AtomicInteger(0);
    private static final AtomicInteger passedTests = new AtomicInteger(0);
    private static final AtomicInteger failedTests = new AtomicInteger(0);
    private static final AtomicInteger skippedTests = new AtomicInteger(0);
    private static long startTime = 0;
    private static long testStartTime = 0;
    private static String reportFileName = null;
    
    static {
        // Register shutdown hook to ensure report is flushed at the end
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            flushReport();
        }));
    }

    /**
     * Initialize Extent Reports before all tests
     * Creates report file with timestamp
     * Sets up report configuration and theme
     * Uses synchronized initialization to ensure only one report is created
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        // Synchronized initialization to ensure only one report is created
        synchronized (ExtentReportListener.class) {
            if (!initialized.get()) {
                // Create test-reports directory if not exists
                new File(REPORT_PATH).mkdirs();
                
                // Initialize start time only once
                startTime = System.currentTimeMillis();
                
                // Clear previous test details
                DetailedReportGenerator.clear();

                // Generate report file name with timestamp (only once)
                String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                reportFileName = REPORT_PATH + "CombinedTestReport_" + timestamp + ".html";

                // Initialize Spark Reporter
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFileName);
                sparkReporter.config().setTheme(Theme.DARK);
                sparkReporter.config().setDocumentTitle("Combined Test Execution Report");
                sparkReporter.config().setReportName("Selenium Test Automation - Combined Report");
                
                // Enable dashboard with test statistics summary
                sparkReporter.config().setOfflineMode(true);

                // Initialize Extent Reports
                extentReports = new ExtentReports();
                extentReports.attachReporter(sparkReporter);
                
                // Add system information (excluding Date)
                extentReports.setSystemInfo("OS", System.getProperty("os.name"));
                extentReports.setSystemInfo("OS Version", System.getProperty("os.version"));
                extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
                extentReports.setSystemInfo("Browser", "Chrome");
                extentReports.setSystemInfo("Environment", ConfigReader.getEnvironment());

                initialized.set(true);
                System.out.println("✓ Combined Extent Report initialized: " + reportFileName);
            }
        }
    }

    /**
     * Create test entry before each test execution
     * Records test method name and description
     */
    @Override
    public void beforeTestExecution(ExtensionContext context) {
        if (extentReports == null) {
            // If report not initialized yet, initialize it
            beforeAll(context);
        }
        
        String testMethodName = context.getDisplayName();
        String testClass = context.getTestClass().map(Class::getSimpleName).orElse("Unknown");
        
        testStartTime = System.currentTimeMillis();
        totalTests.incrementAndGet();
        extentTest = extentReports.createTest(testClass + " - " + testMethodName);
        extentTest.info("Test Class: " + testClass);
        extentTest.info("Test Method: " + testMethodName);
        extentTest.info("Test started at: " + new Date());
    }

    /**
     * Log test results after each test execution
     * Records pass, fail, or skip status
     */
    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (extentTest != null) {
            long testEndTime = System.currentTimeMillis();
            long executionTime = testEndTime - testStartTime;
            String startTimeStr = new SimpleDateFormat("HH:mm:ss").format(new Date(testStartTime));
            String testName = context.getTestClass().map(Class::getSimpleName).orElse("Unknown") + " - " + context.getDisplayName();
            
            if (context.getExecutionException().isPresent()) {
                // Test failed
                Throwable exception = context.getExecutionException().get();
                extentTest.fail("Test failed with exception: " + exception.getMessage());
                extentTest.fail(exception);
                failedTests.incrementAndGet();
                DetailedReportGenerator.addTestCaseDetail(testName, "FAIL", executionTime, startTimeStr, new SimpleDateFormat("HH:mm:ss").format(new Date(testEndTime)));
                System.out.println("✗ Test FAILED: " + testName);
            } else {
                // Test passed
                extentTest.pass("Test passed successfully");
                passedTests.incrementAndGet();
                DetailedReportGenerator.addTestCaseDetail(testName, "PASS", executionTime, startTimeStr, new SimpleDateFormat("HH:mm:ss").format(new Date(testEndTime)));
                System.out.println("✓ Test PASSED: " + testName);
            }
        }
    }

    /**
     * Finalize and flush reports after all tests
     * Generates the final HTML report
     * Uses synchronized block to ensure report is flushed only once
     */
    @Override
    public void afterAll(ExtensionContext context) {
        // Don't flush here - let the shutdown hook handle it
        // This prevents multiple flushes when multiple test classes finish
    }
    
    /**
     * Flush the report (called by shutdown hook or manually)
     * Ensures report is flushed only once
     */
    private static synchronized void flushReport() {
        if (extentReports != null && !reportFlushed.get()) {
            extentReports.flush();
            reportFlushed.set(true);
            System.out.println("✓ Combined Extent Report generated successfully: " + reportFileName);
            
            // Generate detailed report with test case details
            DetailedReportGenerator.generateDetailedReport();
            
            // Generate summary report
            long endTime = System.currentTimeMillis();
            long executionTimeMs = endTime - startTime;
            
            SummaryReportGenerator summaryGenerator = new SummaryReportGenerator(
                totalTests.get(), passedTests.get(), failedTests.get(), skippedTests.get(), executionTimeMs
            );
            summaryGenerator.generateSummaryReport();
        }
    }
    
    /**
     * Public method to manually flush the report
     * Can be called explicitly if needed
     */
    public static void flushReportManually() {
        flushReport();
    }

    /**
     * Get current ExtentTest instance for logging
     * Allows tests to add custom logs to the report
     * 
     * @return Current ExtentTest instance
     */
    public static ExtentTest getExtentTest() {
        return extentTest;
    }

    /**
     * Log info message to the report
     * 
     * @param message Message to log
     */
    public static void logInfo(String message) {
        if (extentTest != null) {
            extentTest.info(message);
        }
    }

    /**
     * Log pass message to the report
     * 
     * @param message Message to log
     */
    public static void logPass(String message) {
        if (extentTest != null) {
            extentTest.pass(message);
        }
    }

    /**
     * Log fail message to the report
     * 
     * @param message Message to log
     */
    public static void logFail(String message) {
        if (extentTest != null) {
            extentTest.fail(message);
        }
    }
}
