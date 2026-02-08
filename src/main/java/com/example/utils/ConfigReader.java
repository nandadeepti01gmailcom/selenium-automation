package com.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader Class
 * Utility class to read configuration properties from config.properties file
 * Supports multiple environments (dev, staging, prod)
 * All properties are loaded once at class initialization
 */
public class ConfigReader {
    private static Properties properties;
    private static String environment;

    // Static block to load properties when class is initialized
    static {
        loadProperties();
    }

    /**
     * Load properties from config.properties file
     * Initializes the Properties object and reads the configuration file
     * Sets the default environment to 'dev' if not specified
     * Also loads config.local.properties if it exists (for sensitive data like Slack webhooks)
     * Local properties override main config properties
     * Handles IO exceptions with appropriate error messages
     */
    private static void loadProperties() {
        properties = new Properties();
        try {
            // Load main config file
            String configFilePath = "src/main/resources/config.properties";
            FileInputStream fis = new FileInputStream(configFilePath);
            properties.load(fis);
            fis.close();
            
            // Load local config file if it exists (not tracked by git, for sensitive data)
            try {
                String localConfigPath = "src/main/resources/config.local.properties";
                FileInputStream localFis = new FileInputStream(localConfigPath);
                properties.load(localFis); // This will override properties from main config
                localFis.close();
                System.out.println("✓ Loaded local configuration overrides from config.local.properties");
            } catch (IOException localEx) {
                // Local config is optional, so just log a note if not found
                System.out.println("ℹ No local config file found (this is optional)");
            }
            
            environment = properties.getProperty("environment", "dev");
            System.out.println("Environment: " + environment);
        } catch (IOException e) {
            System.out.println("Error loading config.properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== URL Methods =====
    public static String getBaseURL() {
        String url = properties.getProperty(environment + ".url");
        if (url == null || url.isEmpty()) {
            System.out.println("URL not found for environment: " + environment);
            return "https://practicetestautomation.com/practice-test-login/";
        }
        return url;
    }

    /**
     * Get the Myntra URL for the current environment
     * @return Myntra URL configured for the active environment
     */
    public static String getMyntraURL() {
        String url = properties.getProperty(environment + ".myntra.url", "https://www.myntra.com/");
        return url;
    }

    // ===== Browser Methods =====
    public static String getBrowser() {
        return properties.getProperty(environment + ".browser", "chrome");
    }

    // ===== Wait Time Methods =====
    public static int getImplicitWait() {
        String wait = properties.getProperty(environment + ".implicitWait", "10");
        return Integer.parseInt(wait);
    }

    public static int getExplicitWait() {
        String wait = properties.getProperty(environment + ".explicitWait", "15");
        return Integer.parseInt(wait);
    }

    public static long getPageWait() {
        String wait = properties.getProperty(environment + ".page.wait", "2000");
        return Long.parseLong(wait);
    }

    // ===== Valid Credentials Methods =====
    public static String getValidUsername() {
        return properties.getProperty(environment + ".valid.username", "student");
    }

    public static String getValidPassword() {
        return properties.getProperty(environment + ".valid.password", "Password123");
    }

    // ===== Invalid Credentials Methods =====
    public static String getInvalidUsername() {
        return properties.getProperty(environment + ".invalid.username", "invalidUser");
    }

    public static String getInvalidPassword() {
        return properties.getProperty(environment + ".invalid.password", "invalidPassword");
    }

    // ===== Locator Methods =====
    public static String getUsernameLocator() {
        return properties.getProperty(environment + ".username.locator", "id:username");
    }

    public static String getPasswordLocator() {
        return properties.getProperty(environment + ".password.locator", "id:password");
    }

    public static String getSubmitLocator() {
        return properties.getProperty(environment + ".submit.locator", "id:submit");
    }

    public static String getSuccessLocator() {
        return properties.getProperty(environment + ".success.locator", "className:post-title");
    }

    public static String getErrorLocator() {
        return properties.getProperty(environment + ".error.locator", "id:error");
    }

    // ===== Environment Methods =====
    public static String getEnvironment() {
        return environment;
    }

    public static void setEnvironment(String env) {
        environment = env;
        System.out.println("Environment changed to: " + environment);
    }

    // ===== Generic Property Methods =====
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
