# Slack Integration Setup Guide

## Overview
Your Selenium test project now sends automated test results to Slack after each test execution.

## Features
- ✅ Automated test summary notifications
- 📊 Test statistics (passed, failed, skipped counts)
- ⏱️ Execution time tracking
- 🎨 Color-coded messages (green for pass, red for fail)
- 📝 Direct link to HTML test reports
- 🔧 Easy enable/disable toggle

## Setup Instructions

### 1. Create a Slack Webhook

1. Go to your Slack workspace
2. Visit: https://api.slack.com/messaging/webhooks
3. Click **"Create an App"** (or use existing app)
4. Choose **"From scratch"**
5. Name your app (e.g., "Selenium Test Bot")
6. Select your workspace
7. Under **"Features"**, click **"Incoming Webhooks"**
8. Toggle **"Activate Incoming Webhooks"** to **ON**
9. Click **"Add New Webhook to Workspace"**
10. Select the channel where you want notifications
11. Click **"Allow"**
12. Copy the **Webhook URL** (looks like: `https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXX`)

### 2. Configure Your Project

Edit `src/main/resources/config.properties`:

```properties
# Enable Slack notifications
slack.notifications.enabled=true

# Paste your webhook URL here
slack.webhook.url=https://hooks.slack.com/services/YOUR/WEBHOOK/URL
```

### 3. Run Your Tests

```bash
mvn test
```

After tests complete, you'll see a formatted message in your Slack channel!

## Message Format

The Slack notification includes:
- **Status**: Overall test result (PASSED/FAILED)
- **Environment**: Current test environment (DEV/STAGING/PROD)
- **Total Tests**: Number of tests executed
- **Passed**: ✅ Number of passing tests
- **Failed**: ❌ Number of failing tests
- **Skipped**: ⏭️ Number of skipped tests
- **Duration**: ⏱️ Total execution time
- **Timestamp**: When tests completed
- **Report Path**: Location of HTML test report

## Disable Notifications

To temporarily disable Slack notifications without removing the webhook:

```properties
slack.notifications.enabled=false
```

## Testing the Integration

You can test Slack integration independently:

```java
// In any test or main method
SlackNotifier.sendSimpleMessage("🧪 Testing Slack integration!");
```

## Troubleshooting

### No messages appearing in Slack?

1. **Check webhook URL**: Make sure it's copied correctly
2. **Verify enabled flag**: `slack.notifications.enabled=true`
3. **Check console output**: Look for error messages
4. **Test webhook manually**: Use curl to test:
   ```bash
   curl -X POST -H 'Content-type: application/json' \
   --data '{"text":"Test message"}' \
   YOUR_WEBHOOK_URL
   ```

### Common Errors

**Error: "Slack webhook URL not configured"**
- Add `slack.webhook.url` to config.properties

**Error: "Slack webhook request failed: 404"**
- Your webhook URL is invalid or expired
- Create a new webhook in Slack

**Error: "Slack webhook request failed: 403"**
- Webhook was revoked
- Recreate the webhook with proper permissions

## Best Practices

1. **Security**: Don't commit webhook URLs to public repositories
   - Use environment variables for CI/CD
   - Add config.properties to .gitignore (if not already)

2. **Channels**: Create a dedicated #test-results channel

3. **Notifications**: Enable only for important test runs (CI/CD, nightly builds)

4. **Rate Limits**: Slack has rate limits (1 message per second per webhook)

## Advanced Usage

### Custom Messages

You can send custom messages from your tests:

```java
@Test
public void criticalTest() {
    // Your test logic
    if (someCriticalCondition) {
        SlackNotifier.sendSimpleMessage("🚨 Critical test condition detected!");
    }
}
```

### Environment-Specific Configuration

Use different webhooks for different environments:

```properties
# DEV webhook
dev.slack.webhook.url=https://hooks.slack.com/services/...

# PROD webhook  
prod.slack.webhook.url=https://hooks.slack.com/services/...
```

Then update SlackNotifier.java to read environment-specific URLs.

## Support

For more information:
- Slack Webhooks Documentation: https://api.slack.com/messaging/webhooks
- OkHttp Documentation: https://square.github.io/okhttp/
