package com.example.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import com.example.utils.ConfigReader;

/**
 * BasePage Class
 * Parent/Base class for all page objects in the framework
 * Implements Page Object Model (POM) design pattern
 * Provides common methods for element interactions with built-in waits
 * 
 * All page classes should extend this class to inherit common functionality
 * 
 * Benefits:
 * - Centralized element interaction methods with explicit waits
 * - Consistent error handling across all pages
 * - Easy maintenance and code reusability
 * - Automatic wait handling for all element operations
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private int explicitWait = ConfigReader.getExplicitWait();

    /**
     * Constructor for BasePage
     * Initializes WebDriver and WebDriverWait
     * 
     * @param driver WebDriver instance from BaseTest class
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }

    // ========== ELEMENT CLICK OPERATIONS ==========

    /**
     * Click on an element with explicit wait
     * Waits for the element to be clickable before clicking
     * Handles exceptions gracefully with logging
     * 
     * @param locator By object representing the element to click
     */
    public void clickElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            System.out.println("Element clicked: " + locator);
        } catch (Exception e) {
            System.out.println("Failed to click element: " + locator + " Error: " + e.getMessage());
        }
    }

    // ========== TEXT INPUT OPERATIONS ==========

    /**
     * Send keys to an element with explicit wait and clear functionality
     * Clears any existing text before sending new keys
     * Waits for the element to be visible before interaction
     * 
     * @param locator By object representing the element to input text
     * @param text Text to send to the element
     */
    public void sendKeys(By locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
            System.out.println("Text sent to element: " + locator + " Text: " + text);
        } catch (Exception e) {
            System.out.println("Failed to send keys to element: " + locator + " Error: " + e.getMessage());
        }
    }

    // ========== TEXT RETRIEVAL OPERATIONS ==========

    /**
     * Get text content from an element with explicit wait
     * Waits for the element to be visible before retrieving text
     * 
     * @param locator By object representing the element to get text from
     * @return Text content of the element, or empty string if operation fails
     */
    public String getText(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String text = element.getText();
            System.out.println("Text retrieved from element: " + locator + " Text: " + text);
            return text;
        } catch (Exception e) {
            System.out.println("Failed to get text from element: " + locator + " Error: " + e.getMessage());
            return "";
        }
    }

    // ========== ELEMENT STATE CHECKING OPERATIONS ==========

    /**
     * Check if an element is displayed/visible
     * Waits for the element to be visible before checking
     * 
     * @param locator By object representing the element to check
     * @return true if element is displayed, false otherwise
     */
    public boolean isElementDisplayed(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.isDisplayed();
        } catch (Exception e) {
            System.out.println("Element not displayed: " + locator);
            return false;
        }
    }

    /**
     * Check if an element is present in DOM
     * Doesn't require element to be visible
     * 
     * @param locator By object representing the element to check
     * @return true if element is present, false otherwise
     */
    public boolean isElementPresent(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ========== ELEMENT WAIT OPERATIONS ==========

    /**
     * Wait for an element to become invisible/disappear
     * Useful for waiting for loading indicators or popups to close
     * 
     * @param locator By object representing the element to wait for disappearance
     */
    public void waitForElementToDisappear(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            System.out.println("Element disappeared: " + locator);
        } catch (Exception e) {
            System.out.println("Element did not disappear: " + locator);
        }
    }

    // ========== PAGE INFORMATION OPERATIONS ==========

    /**
     * Get the title of the current page
     * 
     * @return Page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get the current URL of the page
     * 
     * @return Current URL
     */
    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    // ========== WAIT UTILITY OPERATIONS ==========

    /**
     * Wait for default page load time
     * Uses the page wait time configured in config.properties
     * Primarily used for page load waits between actions
     */
    public void waitForPageLoad() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Custom wait for specified milliseconds
     * Provides flexibility for custom wait scenarios
     * 
     * @param milliseconds Duration to wait in milliseconds
     */
    public void customWait(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
