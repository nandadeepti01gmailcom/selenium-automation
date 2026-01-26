package com.example.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Detailed Report Generator
 * Creates a comprehensive HTML report showing all test case details
 * Includes test name, status, execution time, and detailed logs
 */
public class DetailedReportGenerator {

    private static final String REPORT_PATH = "test-reports/";
    private static List<TestCaseDetail> testCaseDetails = new ArrayList<>();

    public static class TestCaseDetail {
        public String testName;
        public String status;
        public long executionTime;
        public String startTime;
        public String endTime;
        public String details;

        public TestCaseDetail(String testName, String status, long executionTime, String startTime, String endTime) {
            this.testName = testName;
            this.status = status;
            this.executionTime = executionTime;
            this.startTime = startTime;
            this.endTime = endTime;
            this.details = "";
        }
    }

    /**
     * Add a test case detail
     */
    public static void addTestCaseDetail(String testName, String status, long executionTime, String startTime, String endTime) {
        testCaseDetails.add(new TestCaseDetail(testName, status, executionTime, startTime, endTime));
    }

    /**
     * Generate detailed HTML report
     */
    public static void generateDetailedReport() {
        try {
            new File(REPORT_PATH).mkdirs();
            
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportFileName = REPORT_PATH + "DetailedReport_" + timestamp + ".html";
            
            String htmlContent = generateHtmlContent();
            
            FileWriter writer = new FileWriter(reportFileName);
            writer.write(htmlContent);
            writer.close();
            
            System.out.println("✓ Detailed Report generated: " + reportFileName);
        } catch (IOException e) {
            System.err.println("✗ Error generating detailed report: " + e.getMessage());
        }
    }

    /**
     * Generate HTML content for detailed report
     */
    private static String generateHtmlContent() {
        StringBuilder tableRows = new StringBuilder();
        int passCount = 0;
        int failCount = 0;
        
        for (int i = 0; i < testCaseDetails.size(); i++) {
            TestCaseDetail detail = testCaseDetails.get(i);
            String statusClass = detail.status.equalsIgnoreCase("PASS") ? "status-pass" : "status-fail";
            String statusIcon = detail.status.equalsIgnoreCase("PASS") ? "✓ PASS" : "✗ FAIL";
            
            if (detail.status.equalsIgnoreCase("PASS")) passCount++;
            else failCount++;
            
            tableRows.append("                <tr>\n");
            tableRows.append("                    <td class=\"serial\">").append(i + 1).append("</td>\n");
            tableRows.append("                    <td class=\"test-name\">").append(detail.testName).append("</td>\n");
            tableRows.append("                    <td class=\"").append(statusClass).append("\">").append(statusIcon).append("</td>\n");
            tableRows.append("                    <td class=\"time\">").append(formatTime(detail.executionTime)).append("</td>\n");
            tableRows.append("                    <td class=\"timestamp\">").append(detail.startTime).append("</td>\n");
            tableRows.append("                </tr>\n");
        }
        
        String passPercentage = testCaseDetails.size() > 0 ? String.format("%.2f", (passCount * 100.0) / testCaseDetails.size()) : "0";
        
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Detailed Test Execution Report</title>\n" +
                "    <style>\n" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);\n" +
                "            min-height: 100vh;\n" +
                "            padding: 30px 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background: white;\n" +
                "            border-radius: 12px;\n" +
                "            box-shadow: 0 15px 50px rgba(0, 0, 0, 0.3);\n" +
                "            max-width: 1200px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 40px;\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 40px;\n" +
                "            border-bottom: 3px solid #2a5298;\n" +
                "            padding-bottom: 20px;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #1e3c72;\n" +
                "            font-size: 2.0em;\n" +
                "            margin-bottom: 10px;\n" +
                "            font-weight: 700;\n" +
                "            letter-spacing: 0.5px;\n" +
                "        }\n" +
                "        .subtitle {\n" +
                "            color: #666;\n" +
                "            font-size: 0.9em;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .stats-summary {\n" +
                "            display: grid;\n" +
                "            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));\n" +
                "            gap: 15px;\n" +
                "            margin-bottom: 30px;\n" +
                "        }\n" +
                "        .stat-item {\n" +
                "            background: #e0e0e0;\n" +
                "            color: #333;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            text-align: center;\n" +
                "            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .stat-item.passed {\n" +
                "            background: #d4edda;\n" +
                "            color: #155724;\n" +
                "        }\n" +
                "        .stat-item.failed {\n" +
                "            background: #f8d7da;\n" +
                "            color: #721c24;\n" +
                "        }\n" +
                "        .stat-number {\n" +
                "            font-size: 2.2em;\n" +
                "            font-weight: bold;\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "        .stat-label {\n" +
                "            font-size: 0.9em;\n" +
                "            opacity: 0.95;\n" +
                "        }\n" +
                "        .table-title {\n" +
                "            font-size: 1.2em;\n" +
                "            color: #1e3c72;\n" +
                "            margin-bottom: 20px;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        .table-wrapper {\n" +
                "            overflow-x: auto;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            font-size: 0.9em;\n" +
                "        }\n" +
                "        thead {\n" +
                "            background: #808080;\n" +
                "            color: white;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        th {\n" +
                "            padding: 15px 12px;\n" +
                "            text-align: left;\n" +
                "            font-size: 0.9em;\n" +
                "            letter-spacing: 0.3px;\n" +
                "        }\n" +
                "        td {\n" +
                "            padding: 12px 12px;\n" +
                "            border-bottom: 1px solid #e0e0e0;\n" +
                "        }\n" +
                "        tbody tr {\n" +
                "            transition: background-color 0.3s ease;\n" +
                "        }\n" +
                "        tbody tr:hover {\n" +
                "            background-color: #f8f9fa;\n" +
                "        }\n" +
                "        tbody tr:nth-child(odd) {\n" +
                "            background-color: #f9f9f9;\n" +
                "        }\n" +
                "        .serial {\n" +
                "            font-weight: 600;\n" +
                "            color: #2a5298;\n" +
                "            width: 60px;\n" +
                "        }\n" +
                "        .test-name {\n" +
                "            font-weight: 500;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        .status-pass {\n" +
                "            color: #38ef7d;\n" +
                "            font-weight: 700;\n" +
                "            background-color: rgba(56, 239, 125, 0.1);\n" +
                "            padding: 8px 12px;\n" +
                "            border-radius: 6px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .status-fail {\n" +
                "            color: #ff6a00;\n" +
                "            font-weight: 700;\n" +
                "            background-color: rgba(255, 106, 0, 0.1);\n" +
                "            padding: 8px 12px;\n" +
                "            border-radius: 6px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .time {\n" +
                "            color: #667eea;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        .timestamp {\n" +
                "            color: #999;\n" +
                "            font-size: 0.95em;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            color: #999;\n" +
                "            font-size: 0.95em;\n" +
                "            margin-top: 40px;\n" +
                "            padding-top: 20px;\n" +
                "            border-top: 1px solid #e0e0e0;\n" +
                "        }\n" +
                "        .back-link {\n" +
                "            display: inline-block;\n" +
                "            margin-bottom: 30px;\n" +
                "            padding: 12px 25px;\n" +
                "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "            color: white;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 6px;\n" +
                "            font-weight: 600;\n" +
                "            transition: transform 0.3s, box-shadow 0.3s;\n" +
                "            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);\n" +
                "        }\n" +
                "        .back-link:hover {\n" +
                "            transform: translateY(-2px);\n" +
                "            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Automation test report for Myntra</h1>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"stats-summary\">\n" +
                "            <div class=\"stat-item\">\n" +
                "                <div class=\"stat-label\">Total Tests</div>\n" +
                "                <div class=\"stat-number\">" + testCaseDetails.size() + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"stat-item passed\">\n" +
                "                <div class=\"stat-label\">Passed</div>\n" +
                "                <div class=\"stat-number\">" + passCount + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"stat-item failed\">\n" +
                "                <div class=\"stat-label\">Failed</div>\n" +
                "                <div class=\"stat-number\">" + failCount + "</div>\n" +
                "            </div>\n" +
                "            <div class=\"stat-item\">\n" +
                "                <div class=\"stat-label\">Pass Rate</div>\n" +
                "                <div class=\"stat-number\">" + passPercentage + "%</div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"table-title\">Test Case Execution Details</div>\n" +
                "        <div class=\"table-wrapper\">\n" +
                "            <table>\n" +
                "                <thead>\n" +
                "                    <tr>\n" +
                "                        <th>#</th>\n" +
                "                        <th>Test Case Name</th>\n" +
                "                        <th>Status</th>\n" +
                "                        <th>Execution Time</th>\n" +
                "                        <th>Started At</th>\n" +
                "                    </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                tableRows.toString() +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"footer\">\n" +
                "            <p>Report Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * Format execution time
     */
    private static String formatTime(long timeMs) {
        if (timeMs < 1000) {
            return timeMs + " ms";
        } else if (timeMs < 60000) {
            return String.format("%.2f s", timeMs / 1000.0);
        } else {
            long minutes = timeMs / 60000;
            long seconds = (timeMs % 60000) / 1000;
            return minutes + "m " + seconds + "s";
        }
    }

    /**
     * Clear test case details (for new test run)
     */
    public static void clear() {
        testCaseDetails.clear();
    }
}
