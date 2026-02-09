# Selenium Testing Project - AI Coding Agent Instructions

## Architecture Overview

This is a **Selenium WebDriver** test automation framework using **Java 11**, **JUnit 5**, and **Maven**. The codebase follows the **Page Object Model (POM)** pattern with a layered architecture:

- **Base Layer** ([src/main/java/com/example/base](src/main/java/com/example/base)): `BaseClass` (test setup/teardown) and `BasePage` (common element interactions with explicit waits)
- **Page Layer** ([src/main/java/Login.java](src/main/java/Login.java), [src/main/java/com/example/Dashboard.java](src/main/java/com/example/Dashboard.java)): Page objects encapsulating UI interactions
- **Utils Layer** ([src/main/java/com/example/utils](src/main/java/com/example/utils)): Configuration, reporting, WebDriver management, Slack notifications
- **Test Layer** ([src/test/java](src/test/java)): JUnit 5 test classes extending `BaseClass`

## Critical Patterns & Conventions

### 1. Multi-Environment Configuration System
All test data, credentials, and locators are **environment-specific** via [config.properties](src/main/resources/config.properties):
```properties
environment=dev  # Switch between dev/staging/prod

# Pattern: {environment}.{property}
dev.url=https://practicetestautomation.com/practice-test-login/
dev.valid.username=student
dev.username.locator=id:username  # See Locator Parser below
```

**Access via**: `TestDataManager.getValidUsername()` → wraps `ConfigReader` for cleaner API

### 2. String-Based Locator System
Locators are stored as **strings** in config.properties using format `strategy:value`:
```properties
dev.username.locator=id:username
dev.submit.locator=xpath://button[@id='submit']
```

**Parse with**: `LocatorParser.parseLocator("id:username")` → returns `By.id("username")`  
**Supported**: id, name, className, xpath, css, linkText, partialLinkText

### 3. Factory Pattern for WebDriver
`WebDriverFactory.createDriver(browserType)` handles browser initialization:
- Uses **WebDriverManager** for automatic driver management (no manual downloads)
- Returns configured ChromeDriver/FirefoxDriver/EdgeDriver with pre-set options
- Default: Chrome with `--disable-notifications` and `--disable-popup-blocking`

### 4. Test Structure & Execution
All tests extend `BaseClass` which provides:
- `@BeforeEach setUp()`: Initializes driver, maximizes window, sets implicit waits
- `@AfterEach tearDown()`: Calls `quitDriver()` to clean up resources
- Protected `driver` and `BASE_URL` fields for test access

**Test Organization**:
- Use `@Tag` for categorization: `smoke`, `regression`, `negative`, `edge`
- Use `@Order` with `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)` for execution order
- Use `@ExtendWith(ExtentReportListener.class)` on test classes for HTML reporting

### 5. Reporting & Notifications
**Extent Reports**: Automatically generated in `test-reports/` with timestamp (e.g., `CombinedTestReport_2026-02-03_00-32-25.html`)
- Dark theme, system info (OS, browser, environment), execution times
- Custom logging: `ExtentReportListener.logInfo("message")`

**Slack Integration**: Post-execution notifications to Slack via webhook
- Configure in [config.properties](src/main/resources/config.properties): `slack.webhook.url` and `slack.notifications.enabled=true`
- Sends test statistics, duration, pass/fail status with color coding
- See [SLACK_INTEGRATION.md](SLACK_INTEGRATION.md)

## Developer Workflows

### Build & Test Execution
```bash
# Full clean build + tests
mvn clean install

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=LoginTest

# Run tests by tag
mvn test -DincludeTags=smoke
mvn test -DincludeTags=regression
```

### Adding New Tests
1. Create test class extending `BaseClass` in [src/test/java](src/test/java)
2. Add `@ExtendWith(ExtentReportListener.class)` for reporting
3. Use `@Tag` for categorization, `@Order` for sequencing
4. Access `driver` and `BASE_URL` from parent class
5. Retrieve test data via `TestDataManager.getValidUsername()`, etc.

### Adding New Page Objects
1. Create class in [src/main/java/com/example](src/main/java/com/example) extending `BasePage`
2. Constructor: `public MyPage(WebDriver driver) { super(driver); }`
3. Define locators as `private By` fields using `LocatorParser.parseLocator(ConfigReader.getProperty("..."))`
4. Use inherited methods: `clickElement(locator)`, `sendKeys(locator, text)`, `getText(locator)`, `isElementDisplayed(locator)`
5. All element interactions have built-in `WebDriverWait` (explicit waits from config)

### Changing Test Environment
Edit [config.properties](src/main/resources/config.properties):
```properties
environment=staging  # dev, staging, or prod
```
OR programmatically: `ConfigReader.setEnvironment("staging")`

## Integration Points & Dependencies

- **WebDriverManager** (io.github.bonigarcia:webdrivermanager:5.6.0): Auto-downloads browser drivers
- **Extent Reports** (com.aventstack:extentreports:5.1.1): HTML report generation
- **OkHttp** (com.squareup.okhttp3:okhttp:4.12.0): Slack webhook HTTP client
- **JUnit 5**: Test framework with `@Tag`, `@ExtendWith`, `@TestMethodOrder`
- **Selenium 4.25.0**: WebDriver API

## Common Pitfalls

1. **Always call `super(driver)` in Page Object constructors** to initialize `BasePage`
2. **Never interact with elements directly** - use `BasePage` methods which handle waits
3. **Locator format must be `strategy:value`** - missing colon causes `IllegalArgumentException`
4. **Test classes must extend `BaseClass`** to get driver setup/teardown lifecycle
5. **Reports require `@ExtendWith(ExtentReportListener.class)`** on test class, not individual methods
6. **Maven Surefire looks for `**/*Test.java`** - test class names must end with `Test`

## Project-Specific Context

- **Default Test URL**: https://practicetestautomation.com/practice-test-login/ (dev environment)
- **Alternative App**: Myntra e-commerce site (configured in properties)
- **WebDriver waits**: Implicit (10s) + Explicit (15s) configured per environment
- **Java Version**: 11 (source/target compatibility)
- **Test Execution Order**: Tests use `@Order` to run page load checks before login tests
- **Resource Location**: `src/main/resources/config.properties` accessed from classpath after `mvn compile`
