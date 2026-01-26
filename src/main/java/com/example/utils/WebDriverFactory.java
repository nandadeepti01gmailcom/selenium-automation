package com.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * WebDriverFactory Class
 * Factory class to create and configure WebDriver instances
 * Implements the Factory Design Pattern for browser initialization
 * Supports Chrome, Firefox, and Edge browsers with configurable options
 * 
 * Usage:
 *   WebDriver driver = WebDriverFactory.createDriver("chrome");
 *   WebDriver driver = WebDriverFactory.createDriver("firefox");
 */
public class WebDriverFactory {
    
    private static final String BROWSER_CHROME = "chrome";
    private static final String BROWSER_FIREFOX = "firefox";
    private static final String BROWSER_EDGE = "edge";

    /**
     * Create WebDriver instance based on browser type
     * If unsupported browser type is provided, Chrome is used as default
     * 
     * @param browserType Type of browser to create (chrome, firefox, edge)
     * @return WebDriver instance initialized with appropriate browser and options
     */
    public static WebDriver createDriver(String browserType) {
        String browser = browserType.toLowerCase();
        
        switch (browser) {
            case BROWSER_CHROME:
                return createChromeDriver();
            case BROWSER_FIREFOX:
                return createFirefoxDriver();
            case BROWSER_EDGE:
                return createEdgeDriver();
            default:
                System.out.println("Browser not supported: " + browserType + ". Using Chrome as default.");
                return createChromeDriver();
        }
    }

    /**
     * Create Chrome WebDriver with options
     * Disables notifications and popup blocking for stability
     * Uncomment headless option for headless mode
     * 
     * @return ChromeDriver instance with configured options
     */
    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Uncomment for headless mode
        // options.addArguments("--headless");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        System.out.println("Chrome browser initialized");
        return new ChromeDriver(options);
    }

    /**
     * Create Firefox WebDriver with options
     * Uncomment headless option for headless mode
     * 
     * @return FirefoxDriver instance with configured options
     */
    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        // Uncomment for headless mode
        // options.addArguments("--headless");
        System.out.println("Firefox browser initialized");
        return new FirefoxDriver(options);
    }

    /**
     * Create Edge WebDriver with options
     * Uncomment headless option for headless mode
     * 
     * @return EdgeDriver instance with configured options
     */
    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        // Uncomment for headless mode
        // options.addArguments("--headless");
        System.out.println("Edge browser initialized");
        return new EdgeDriver(options);
    }
}
