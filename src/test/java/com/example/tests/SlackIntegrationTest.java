package com.example.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import com.example.utils.SlackNotifier;
import com.example.utils.ExtentReportListener;
import static com.example.utils.Tags.*;

/**
 * Slack Integration Test
 * Tests Slack webhook notification functionality
 * Verifies that test reports can be sent to Slack channel
 */
@ExtendWith(ExtentReportListener.class)
@Tag(INTEGRATION)
@Tag(SMOKE)
public class SlackIntegrationTest {

    /**
     * Test Slack webhook integration
     * Sends a test message to verify connectivity
     */
    @Tag(P1)
    @Test
    public void testSendSimpleSlackMessage() {
        // This test calls the Slack notifier to verify webhook connectivity and logs
        SlackNotifier.sendSimpleMessage("Automated test message from SlackIntegrationTest");
    }
}
