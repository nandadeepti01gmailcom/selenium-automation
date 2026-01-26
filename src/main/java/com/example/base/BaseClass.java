package com.example.base;

import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import com.example.utils.WebDriverFactory;
import com.example.utils.TestDataManager;

/**
 * BaseClass - Base Test Class
 * Provides setup and teardown for all test classes
 * Initializes WebDriver, browser, and test data before each test
 * Cleans up resources after each test
 * 
 * All test classes should extend this class
 * 
 * Features:
 * - Automatic browser initialization using WebDriverFactory
 * - Test data management through TestDataManager
 * - Implicit and explicit wait configuration
 * - Browser navigation utilities
 * - Resource cleanup after each test
 */
public class BaseClass {
    protected WebDriver driver;
    protected String BASE_URL;

    /**
     * Setup method - Runs before each test
     * Initializes browser, loads URL, and configures waits
     * Uses WebDriverFactory for browser creation
     * Uses TestDataManager for configuration retrieval
     */
    @BeforeEach
    public void setUp() {
        // Initialize BASE_URL from test data manager
        BASE_URL = TestDataManager.getBaseURL();
        
        // Initialize browser using factory pattern
        String browserType = TestDataManager.getBrowserType();
        driver = WebDriverFactory.createDriver(browserType);
        
        // Maximize window for better visibility
        driver.manage().window().maximize();
        
        // Set implicit wait for all element operations
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(TestDataManager.getImplicitWait()));
        
        System.out.println("Browser setup completed - Environment: " + TestDataManager.getEnvironment());
    }

    /**
     * Teardown method - Runs after each test
     * Cleans up and closes the browser
     * Ensures resources are properly released
     */
    @AfterEach
    public void tearDown() {
        // Close the browser
        quitDriver();
    }

    /**
     * Launch browser with URL
     * Uses the initialized driver to navigate to specified URL
     * 
     * @param url URL to navigate to
     */
    public void launchBrowser(String url) {
        if (driver != null) {
            driver.get(url);
            System.out.println("Browser launched successfully with URL: " + url);
        } else {
            System.out.println("Driver is not initialized. Call setUp() first.");
        }
    }

    /**
     * Launch browser with specific browser type and URL
     * Useful for running same test on different browsers
     * Creates new driver instance with specified browser type
     * 
     * @param browserType Type of browser (chrome, firefox, edge)
     * @param url URL to navigate to
     */
    public void launchBrowser(String browserType, String url) {
        // Create new driver with specified browser type
        driver = WebDriverFactory.createDriver(browserType);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(TestDataManager.getImplicitWait()));
        driver.get(url);
        System.out.println("Browser launched with " + browserType + " and URL: " + url);
    }

    /**
     * Get current driver instance
     * Provides access to WebDriver for direct operations if needed
     * 
     * @return WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Quit driver - Closes all windows and ends session
     * Should be called in tearDown() or when test is complete
     * Resets driver reference to null
     */
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            System.out.println("Browser closed successfully");
        }
    }

    /**
     * Close current window only
     * Useful when working with multiple windows/tabs
     * Does not end the entire session
     */
    public void closeDriver() {
        if (driver != null) {
            driver.close();
            System.out.println("Current window closed");
        }
    }

    /**
     * Setup and Login - Launches browser and performs login
     * Should be called in @BeforeEach method of test classes
     * Logs in with valid credentials from TestDataManager
     * Returns Login page object for test usage
     * 
     * @return Login page object after successful login
     */
    public com.example.pages.Login setupAndLogin() {
        // Initialize BASE_URL if not already initialized
        if (BASE_URL == null) {
            BASE_URL = TestDataManager.getBaseURL();
        }
        
        // Initialize driver if not already initialized
        if (driver == null) {
            String browserType = TestDataManager.getBrowserType();
            driver = WebDriverFactory.createDriver(browserType);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(TestDataManager.getImplicitWait()));
        }

        // Launch browser with BASE_URL
        launchBrowser(BASE_URL);
        System.out.println("\n=== Setting up test - Logging in ===");
        System.out.println("Environment: " + TestDataManager.getEnvironment());
        System.out.println("URL launched: " + BASE_URL);

        // Create Login page object
        com.example.pages.Login loginPage = new com.example.pages.Login(driver);

        // Get credentials from test data manager
        String validUsername = TestDataManager.getValidUsername();
        String validPassword = TestDataManager.getValidPassword();

        // Perform login with credentials
        loginPage.login(validUsername, validPassword);

        // Wait for page to load
        loginPage.customWait(TestDataManager.getPageWait());

        System.out.println("Login completed successfully");
        return loginPage;
    }
}