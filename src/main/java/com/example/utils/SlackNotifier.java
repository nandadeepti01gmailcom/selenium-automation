package com.example.utils;

import okhttp3.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

/**
 * Slack Notifier Utility
 * Sends test execution results to Slack channel via webhook
 * 
 * Features:
 * - Sends formatted test summary to Slack
 * - Includes test statistics (passed, failed, skipped)
 * - Adds execution time and timestamp
 * - Color-coded messages (green for pass, red for fail)
 * - Configurable via config.properties
 * 
 * Setup:
 * 1. Create a Slack webhook URL in your Slack workspace
 * 2. Add slack.webhook.url to config.properties
 * 3. Enable/disable with slack.notifications.enabled property
 * 
 * Usage:
 * SlackNotifier.sendTestResults(totalTests, passedTests, failedTests, skippedTests, duration, reportPath);
 */
public class SlackNotifier {
    
    private static final OkHttpClient client = new OkHttpClient();
    
    /**
     * Send test execution results to Slack
     * 
     * @param totalTests Total number of tests executed
     * @param passedTests Number of tests that passed
     * @param failedTests Number of tests that failed
     * @param skippedTests Number of tests that were skipped
     * @param durationMs Test execution duration in milliseconds
     * @param reportPath Path to the HTML test report
     */
    public static void sendTestResults(int totalTests, int passedTests, int failedTests, 
                                      int skippedTests, long durationMs, String reportPath) {
        
        // Check if Slack notifications are enabled
        String enabled = ConfigReader.getProperty("slack.notifications.enabled");
        if (enabled == null || !enabled.equalsIgnoreCase("true")) {
            System.out.println("ℹ️ Slack notifications are disabled. Set slack.notifications.enabled=true to enable.");
            return;
        }
        
        String webhookUrl = ConfigReader.getProperty("slack.webhook.url");
        if (webhookUrl == null || webhookUrl.trim().isEmpty()) {
            System.err.println("❌ Slack webhook URL not configured. Add slack.webhook.url to config.properties");
            return;
        }
        
        try {
            // Build a plain-text summary and reuse sendSimpleMessage (which wraps JSON correctly)
            String plain = buildSlackPlainText(totalTests, passedTests, failedTests,
                                              skippedTests, durationMs, reportPath);
            sendSimpleMessage(plain);
            System.out.println("✅ Test results sent to Slack successfully!");
        } catch (Exception e) {
            System.err.println("❌ Failed to send Slack notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Build formatted Slack message with test results
     */
    private static String buildSlackMessage(int totalTests, int passedTests, int failedTests, 
                                           int skippedTests, long durationMs, String reportPath) {
        
        // Determine overall status and color
        String status = failedTests > 0 ? "FAILED" : "PASSED";
        String color = failedTests > 0 ? "#ff0000" : "#36a64f"; // Red for fail, green for pass
        String emoji = failedTests > 0 ? "❌" : "✅";
        
        // Format duration
        String duration = formatDuration(durationMs);
        
        // Get current timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Get environment
        String environment = ConfigReader.getProperty("environment", "dev").toUpperCase();
        
        // Build a simple top-level text message to avoid complex attachment payload issues
        StringBuilder text = new StringBuilder();
        text.append(emoji).append(" Test Execution ").append(status).append("\\n");
        text.append("Environment: ").append(environment).append("\\n");
        text.append("Total: ").append(totalTests).append("  |  Passed: ").append(passedTests).append("  |  Failed: ").append(failedTests).append("  |  Skipped: ").append(skippedTests).append("\\n");
        text.append("Duration: ").append(duration).append("\\n");
        text.append("Timestamp: ").append(timestamp).append("\\n");

        if (reportPath != null && !reportPath.trim().isEmpty()) {
            String reportUrl = ConfigReader.getProperty("report.base.url");
            if (reportUrl != null && !reportUrl.trim().isEmpty()) {
                String fileName = new File(reportPath).getName();
                String fullUrl = reportUrl.endsWith("/") ? reportUrl + fileName : reportUrl + "/" + fileName;
                text.append("Report: ").append(fullUrl).append("\\n");
            } else {
                text.append("Report: ").append(new File(reportPath).getAbsolutePath()).append("\\n");
            }
        }

        // Simple JSON with escaped text to ensure valid payload
        String escaped = escapeJson(text.toString());
        return "{\\\"text\\\":\\\"" + escaped + "\\\"}";
    }

    // Very small JSON string escaper for our text payload
    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Build a plain text summary for Slack (used by sendSimpleMessage)
    private static String buildSlackPlainText(int totalTests, int passedTests, int failedTests,
                                             int skippedTests, long durationMs, String reportPath) {
        String status = failedTests > 0 ? "FAILED" : "PASSED";
        String emoji = failedTests > 0 ? "❌" : "✅";
        String duration = formatDuration(durationMs);
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String environment = ConfigReader.getProperty("environment", "dev").toUpperCase();

        StringBuilder sb = new StringBuilder();
        sb.append(emoji).append(" Test Execution ").append(status).append("\n");
        sb.append("Environment: ").append(environment).append("\n");
        sb.append("Total: ").append(totalTests).append("  |  Passed: ").append(passedTests).append("  |  Failed: ").append(failedTests).append("  |  Skipped: ").append(skippedTests).append("\n");
        sb.append("Duration: ").append(duration).append("\n");
        sb.append("Timestamp: ").append(timestamp).append("\n");
        if (reportPath != null && !reportPath.trim().isEmpty()) {
            String reportUrl = ConfigReader.getProperty("report.base.url");
            if (reportUrl != null && !reportUrl.trim().isEmpty()) {
                String fileName = new File(reportPath).getName();
                String fullUrl = reportUrl.endsWith("/") ? reportUrl + fileName : reportUrl + "/" + fileName;
                // Use Slack's link formatting so the URL is clickable with friendly text
                sb.append("Report: <").append(fullUrl).append("|Click here to view detailed HTML report>").append("\n");
            } else {
                sb.append("Report: ").append(new File(reportPath).getAbsolutePath()).append("\n");
            }
        }
        return sb.toString();
    }
    
    /**
     * Send message to Slack webhook
     */
    private static void sendToSlack(String webhookUrl, String jsonMessage) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        // Use the MediaType-first overload to ensure correct API usage across OkHttp versions
        RequestBody body = RequestBody.create(JSON, jsonMessage);
        System.out.println("→ Sending Slack webhook to: " + webhookUrl);
        System.out.println("→ Payload: " + jsonMessage);
        
        Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            int code = response.code();
            String respBody = response.body() != null ? response.body().string() : "";
            System.out.println("← Slack response: HTTP " + code + " - " + respBody);
            if (!response.isSuccessful()) {
                throw new IOException("Slack webhook request failed: " + code + " - " + response.message() + " - " + respBody);
            }
        }
    }
    
    /**
     * Format duration from milliseconds to human-readable format
     */
    private static String formatDuration(long durationMs) {
        long seconds = durationMs / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        } else {
            return seconds + "s";
        }
    }
    
    /**
     * Send simple text message to Slack (for testing)
     */
    public static void sendSimpleMessage(String message) {
        String webhookUrl = ConfigReader.getProperty("slack.webhook.url");
        if (webhookUrl == null || webhookUrl.trim().isEmpty()) {
            System.err.println("❌ Slack webhook URL not configured");
            return;
        }
        
        try {
            String json = "{\"text\":\"" + escapeJson(message) + "\"}";
            sendToSlack(webhookUrl, json);
            System.out.println("✅ Message sent to Slack!");
        } catch (Exception e) {
            System.err.println("❌ Failed to send Slack message: " + e.getMessage());
        }
    }
}
