package com.example.utils;

/**
 * TestDataManager Class
 * Centralized manager for all test data retrieval
 * Acts as a wrapper/abstraction layer over ConfigReader
 * Provides a clean API for accessing test data throughout the test suite
 * 
 * Benefits:
 * - Single point of change for test data management
 * - Easy to extend with new data methods
 * - Abstraction from ConfigReader implementation
 */
public class TestDataManager {
    
    // ========== VALID CREDENTIALS SECTION ==========
    
    /**
     * Get valid username for successful login tests
     * @return Valid username from config for current environment
     */
    public static String getValidUsername() {
        return ConfigReader.getValidUsername();
    }

    /**
     * Get valid password for successful login tests
     * @return Valid password from config for current environment
     */
    public static String getValidPassword() {
        return ConfigReader.getValidPassword();
    }

    // ========== INVALID CREDENTIALS SECTION ==========
    
    /**
     * Get invalid username for negative login tests
     * @return Invalid username from config for current environment
     */
    public static String getInvalidUsername() {
        return ConfigReader.getInvalidUsername();
    }

    /**
     * Get invalid password for negative login tests
     * @return Invalid password from config for current environment
     */
    public static String getInvalidPassword() {
        return ConfigReader.getInvalidPassword();
    }

    // ========== URL SECTION ==========
    
    /**
     * Get base URL of application under test
     * @return Application URL for current environment
     */
    public static String getBaseURL() {
        return ConfigReader.getBaseURL();
    }

    /**
     * Get Myntra URL for current environment
     * @return Myntra application URL
     */
    public static String getMyntraURL() {
        return ConfigReader.getMyntraURL();
    }

    // ========== BROWSER CONFIGURATION SECTION ==========
    
    /**
     * Get browser type for current environment
     * @return Browser type (chrome, firefox, edge)
     */
    public static String getBrowserType() {
        return ConfigReader.getBrowser();
    }

    // ========== WAIT TIME SECTION ==========
    
    /**
     * Get implicit wait time in seconds
     * Used for driver.manage().timeouts().implicitlyWait()
     * @return Implicit wait duration in seconds
     */
    public static int getImplicitWait() {
        return ConfigReader.getImplicitWait();
    }

    /**
     * Get explicit wait time in seconds
     * Used for WebDriverWait initialization
     * @return Explicit wait duration in seconds
     */
    public static int getExplicitWait() {
        return ConfigReader.getExplicitWait();
    }

    /**
     * Get page load wait time in milliseconds
     * Used for Thread.sleep() calls
     * @return Page wait duration in milliseconds
     */
    public static long getPageWait() {
        return ConfigReader.getPageWait();
    }

    // ========== ENVIRONMENT SECTION ==========
    
    /**
     * Get currently active environment
     * @return Environment name (dev, staging, prod)
     */
    public static String getEnvironment() {
        return ConfigReader.getEnvironment();
    }

    /**
     * Set/change environment at runtime
     * Useful for running same tests in different environments
     * @param environment Environment name to switch to (dev, staging, prod)
     */
    public static void setEnvironment(String environment) {
        ConfigReader.setEnvironment(environment);
    }

    // ========== CUSTOM DATA SECTION ==========
    
    /**
     * Get any custom test data from config file
     * @param key Property key to retrieve
     * @return Property value or null if not found
     */
    public static String getTestData(String key) {
        return ConfigReader.getProperty(key);
    }

    /**
     * Get any custom test data from config file with default fallback
     * @param key Property key to retrieve
     * @param defaultValue Default value if property is not found
     * @return Property value or default value if not found
     */
    public static String getTestData(String key, String defaultValue) {
        return ConfigReader.getProperty(key, defaultValue);
    }
}
