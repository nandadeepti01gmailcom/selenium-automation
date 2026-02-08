package com.example.tests;

import org.junit.jupiter.api.Test;
import com.example.utils.SlackNotifier;

public class SlackIntegrationTest {

    @Test
    public void testSendSimpleSlackMessage() {
        // This test calls the Slack notifier to verify webhook connectivity and logs
        SlackNotifier.sendSimpleMessage("Automated test message from SlackIntegrationTest");
    }
}
