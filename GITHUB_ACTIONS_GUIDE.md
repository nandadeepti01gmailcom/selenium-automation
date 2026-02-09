# GitHub Actions Test Execution Guide

## Overview
This project has two GitHub Actions workflows for running tests:
1. **Java CI - Maven Tests** (`maven.yml`) - Automatic tests on push/PR/tags
2. **Run Tests by Tags** (`test-tags.yml`) - Manual test execution with custom inputs + automated scheduled runs

### Automated Scheduled Runs
The **Run Tests by Tags** workflow automatically executes:
- **Schedule**: Daily at 5:10 AM UTC (10:40 AM IST)
- **Test Suite**: Regression (full test coverage)
- **Environment**: Dev
- **Browser**: Chrome
- **Slack Notifications**: Enabled
- **Excluded Tags**: wip (work in progress tests)

## Run Tests by Tags Workflow

### Accessing the Workflow
1. Go to your GitHub repository
2. Click on **Actions** tab
3. Select **Run Tests by Tags** from the left sidebar
4. Click **Run workflow** button (top right) for manual runs
5. View scheduled run history in the workflow runs list (marked with "schedule" trigger)

### Input Parameters

#### 1. Test Suite (Required)
Choose which test suite to run:
- **smoke** - Quick critical tests (< 5 min)
- **regression** - Full comprehensive test suite
- **sanity** - Quick verification tests
- **all** - Run all tests
- **custom** - Use custom tag combinations

#### 2. Include Tags (Optional - for custom suite)
Specify tags to include using JUnit tag syntax:
- Single tag: `smoke`
- Multiple tags (OR): `smoke | p0`
- Multiple tags (AND): `smoke & login`
- Complex: `(smoke | regression) & !slow`

Examples:
```
smoke
smoke & login
p0 | p1
login | dashboard
negative & edge
```

#### 3. Exclude Tags (Optional)
Comma-separated tags to exclude:
- Default: `wip` (work in progress tests)
- Example: `wip,slow,manual`

#### 4. Environment (Required)
Select test environment:
- **dev** - Development environment
- **staging** - Staging environment
- **prod** - Production environment

#### 5. Browser (Required)
Choose browser for test execution:
- **chrome** (default)
- **firefox**
- **edge**

#### 6. Send Slack Notification (Optional)
- **true** - Send test results to Slack
- **false** - Skip Slack notification

## Usage Examples

### Example 1: Run Smoke Tests in Dev
```yaml
Test Suite: smoke
Include Tags: (leave empty)
Exclude Tags: wip
Environment: dev
Browser: chrome
Send Slack Notification: ✓
```

### Example 2: Run Login Tests Only
```yaml
Test Suite: custom
Include Tags: login
Exclude Tags: wip,slow
Environment: staging
Browser: chrome
Send Slack Notification: ✓
```

### Example 3: Run Critical Priority Tests
```yaml
Test Suite: custom
Include Tags: p0
Exclude Tags: wip
Environment: dev
Browser: chrome
Send Slack Notification: ✓
```

### Example 4: Run Negative Tests
```yaml
Test Suite: custom
Include Tags: negative | edge
Exclude Tags: wip
Environment: staging
Browser: firefox
Send Slack Notification: ✓
```

### Example 5: Run All Dashboard Tests
```yaml
Test Suite: custom
Include Tags: dashboard
Exclude Tags: wip
Environment: dev
Browser: chrome
Send Slack Notification: ✓
```

### Example 6: Full Regression Suite
```yaml
Test Suite: regression
Include Tags: (leave empty)
Exclude Tags: wip,slow
Environment: staging
Browser: chrome
Send Slack Notification: ✓
```

## Workflow Features

### ✅ Automatic Features
- Maven dependency caching for faster builds
- Test report generation (Extent Reports)
- JUnit XML reports
- GitHub Actions summary with test results
- Artifact upload (reports available for 30 days)
- Slack notifications with detailed results
- Workflow fails if tests fail

### 📊 Test Results
After execution, you can:
1. View summary in GitHub Actions run page
2. Download test report artifacts
3. Check Slack notification (if enabled)
4. Review detailed logs in workflow steps

### 📁 Artifacts
Each run produces artifacts named:
```
test-reports-{test_suite}-{environment}-{run_id}
```

Contents:
- HTML test reports (Extent Reports)
- JUnit XML reports
- Screenshots (if any)

## Setting Up Slack Notifications

### Prerequisites
1. Create a Slack Incoming Webhook
2. Add webhook URL to GitHub Secrets

### Steps:
1. Go to repository **Settings** → **Secrets and variables** → **Actions**
2. Click **New repository secret**
3. Name: `SLACK_WEBHOOK_URL`
4. Value: Your Slack webhook URL (e.g., `https://hooks.slack.com/services/...`)
5. Click **Add secret**

### Slack Notification Format
The notification includes:
- ✅ / ❌ Status indicator
- Test suite name
- Environment and browser
- Test counts (Total, Passed, Failed, Skipped)
- Triggered by (GitHub username)
- Link to view the workflow run

## GitHub CLI Usage

You can also trigger workflows from command line using GitHub CLI:

### Install GitHub CLI
```bash
# Windows (using winget)
winget install GitHub.cli

# Or download from https://cli.github.com/
```

### Run Workflow via CLI

#### Smoke Tests
```bash
gh workflow run test-tags.yml \
  -f test_suite=smoke \
  -f environment=dev \
  -f browser=chrome
```

#### Custom Tags
```bash
gh workflow run test-tags.yml \
  -f test_suite=custom \
  -f include_tags="login & positive" \
  -f exclude_tags="wip,slow" \
  -f environment=staging \
  -f browser=firefox \
  -f send_slack_notification=true
```

#### Regression Tests
```bash
gh workflow run test-tags.yml \
  -f test_suite=regression \
  -f exclude_tags=wip \
  -f environment=staging \
  -f browser=chrome
```

## Monitoring Test Runs

### View Active Runs
1. Go to **Actions** tab
2. Click on **Run Tests by Tags** workflow
3. See list of all runs with status

### View Run Details
1. Click on any run
2. View:
   - Input parameters used
   - Build logs
   - Test execution logs
   - Test summary
   - Artifacts (download reports)

### Download Reports
1. Scroll to bottom of run page
2. Click on artifact name to download
3. Extract and open HTML reports

## Best Practices

### 🎯 Quick Feedback
Use smoke tests for rapid feedback:
```yaml
Test Suite: smoke
Exclude Tags: wip,slow
```

### 🔍 Targeted Testing
Test specific features after changes:
```yaml
Test Suite: custom
Include Tags: login | dashboard
```

### 🚀 Pre-Release
Run full regression before releases:
```yaml
Test Suite: regression
Environment: staging
```

### 🐛 Bug Verification
Test specific scenarios:
```yaml
Test Suite: custom
Include Tags: negative & login
```

## Scheduled Test Runs

### Default Schedule
The workflow runs automatically every day at:
- **UTC Time**: 5:10 AM (05:10)
- **IST Time**: 10:40 AM
- **Cron Expression**: `10 5 * * *`

### Scheduled Run Configuration
When triggered by schedule, the workflow uses these defaults:
- **Test Suite**: `regression` - Full comprehensive test coverage
- **Environment**: `dev` - Development environment
- **Browser**: `chrome` - Chrome browser
- **Exclude Tags**: `wip` - Skip work-in-progress tests
- **Slack Notifications**: `enabled` - Always sends results to Slack

### Identifying Scheduled Runs
Scheduled runs are identified by:
1. **Trigger**: Shows "schedule" instead of "workflow_dispatch"
2. **Log Output**: Displays "This is an automated scheduled run (5:10 AM UTC daily)"
3. **Run List**: GitHub shows a clock icon ⏰ for scheduled runs

### Customizing Schedule Time
To change the schedule time, edit `.github/workflows/test-tags.yml`:

```yaml
on:
  schedule:
    # Change the cron expression
    # Format: 'minute hour day-of-month month day-of-week'
    - cron: '10 5 * * *'  # Current: 5:10 AM UTC (10:40 AM IST)
    
# Examples:
# - cron: '0 0 * * *'   # Midnight UTC (5:30 AM IST)
# - cron: '30 15 * * *' # 3:30 PM UTC (9:00 PM IST)
# - cron: '0 12 * * 1'  # Noon UTC every Monday
# - cron: '0 9 * * 1-5' # 9 AM UTC on weekdays
```

### Customizing Scheduled Test Suite
To change what tests run on schedule, edit the `env` section:

```yaml
env:
  TEST_SUITE: ${{ inputs.test_suite || 'regression' }}  # Change 'regression' to 'smoke' for faster runs
  TEST_ENVIRONMENT: ${{ inputs.environment || 'dev' }}  # Change to 'staging' for staging tests
  BROWSER: ${{ inputs.browser || 'chrome' }}            # Change to 'firefox' if needed
  EXCLUDE_TAGS: ${{ inputs.exclude_tags || 'wip' }}     # Add more tags: 'wip,slow'
```

### Disabling Scheduled Runs
To temporarily disable scheduled runs:
1. Comment out or remove the `schedule` section in the workflow file
2. Or keep the schedule but add a condition to skip:
```yaml
jobs:
  test-execution:
    # Skip scheduled runs temporarily
    if: github.event_name != 'schedule'
```

### Viewing Scheduled Run History
1. Go to **Actions** → **Run Tests by Tags**
2. Look for runs with ⏰ clock icon
3. Click on any run to see:
   - Trigger type (schedule)
   - Test results
   - Execution time
   - Slack notification status

## Troubleshooting

### Tests Not Running
- Verify tag names match Tags.java constants
- Check syntax in include_tags field
- Ensure JUnit 5 surefire plugin is configured

### No Slack Notification
- Verify SLACK_WEBHOOK_URL secret is set
- Check "Send Slack Notification" is enabled
- Verify webhook URL is valid in Slack

### Workflow Fails Immediately
- Check Maven compile step for errors
- Verify Java version compatibility
- Review dependency issues in build logs

### No Test Results
- Check if tests match the specified tags
- Verify test classes have @Tag annotations
- Review surefire configuration in pom.xml

## Tag Reference

See [TEST_TAGS_GUIDE.md](TEST_TAGS_GUIDE.md) for complete tag documentation.

Common tags:
- **Suite:** smoke, regression, sanity
- **Priority:** p0, p1, p2, p3
- **Type:** positive, negative, edge
- **Area:** login, dashboard, navigation
- **Layer:** ui, api, integration

## Additional Resources
- [Test Tags Guide](TEST_TAGS_GUIDE.md) - Complete tagging documentation
- [Secure Config Setup](SECURE_CONFIG.md) - Local configuration guide
- [Slack Integration](SLACK_INTEGRATION.md) - Slack setup details
