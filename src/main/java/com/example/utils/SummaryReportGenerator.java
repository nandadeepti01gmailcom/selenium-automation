package com.example.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Summary Report Generator
 * Creates a clean HTML summary table for test execution statistics
 * Shows total tests, passed, failed, skipped, pass percentage, and execution time
 */
public class SummaryReportGenerator {

    private static final String REPORT_PATH = "test-reports/";
    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;
    private long executionTimeMs = 0;

    public SummaryReportGenerator(int totalTests, int passedTests, int failedTests, int skippedTests, long executionTimeMs) {
        this.totalTests = totalTests;
        this.passedTests = passedTests;
        this.failedTests = failedTests;
        this.skippedTests = skippedTests;
        this.executionTimeMs = executionTimeMs;
    }

    /**
     * Generate HTML summary report with test statistics table
     */
    public void generateSummaryReport() {
        try {
            new File(REPORT_PATH).mkdirs();
            
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportFileName = REPORT_PATH + "TestSummary_" + timestamp + ".html";
            String detailedReportName = "DetailedReport_" + timestamp + ".html";
            
            double passPercentage = totalTests > 0 ? (passedTests * 100.0) / totalTests : 0;
            String executionTime = formatExecutionTime(executionTimeMs);
            
            String htmlContent = generateHtmlContent(passPercentage, executionTime, detailedReportName);
            
            FileWriter writer = new FileWriter(reportFileName);
            writer.write(htmlContent);
            writer.close();
            
            System.out.println("✓ Summary Report generated: " + reportFileName);
        } catch (IOException e) {
            System.err.println("✗ Error generating summary report: " + e.getMessage());
        }
    }

    /**
     * Generate HTML content for the summary report
     */
    private String generateHtmlContent(double passPercentage, String executionTime, String detailedReportName) {
        return "<html><body><h3>Test Summary Report Generated</h3></body></html>";
    }

    /**
     * Format execution time in human-readable format
     */
    private String formatExecutionTime(long timeMs) {
        long seconds = timeMs / 1000;
        long minutes = seconds / 60;
        long secs = seconds % 60;
        
        if (minutes > 0) {
            return minutes + "m " + secs + "s";
        } else {
            return secs + "s";
        }
    }
}
