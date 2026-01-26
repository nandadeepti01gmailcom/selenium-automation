package com.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import com.example.base.BasePage;
import com.example.utils.LocatorParser;
import com.example.utils.ConfigReader;

/**
 * Login Page Object
 * Represents the login page and encapsulates all login-related operations
 * Follows Page Object Model (POM) design pattern by extending BasePage
 * 
 * All locators are loaded dynamically from config.properties
 * Page methods represent user actions (login, verify, etc.)
 * 
 * Usage:
 *   Login loginPage = new Login(driver);
 *   loginPage.login(username, password);
 *   boolean isLoggedIn = loginPage.isLoginSuccessful();
 */
public class Login extends BasePage {

    // ========== PAGE LOCATORS - Loaded from config file ==========
    private By usernameField;
    private By passwordField;
    private By submitButton;
    private By successMessage;
    private By errorMessage;

    /**
     * Constructor for Login page
     * Calls parent constructor and initializes all locators
     * 
     * @param driver WebDriver instance
     */
    public Login(WebDriver driver) {
        super(driver);
        initializeLocators();
    }

    /**
     * Initialize all page locators from config.properties
     * Uses LocatorParser to convert string locators to Selenium By objects
     * Called once during page object initialization
     */
    private void initializeLocators() {
        usernameField = LocatorParser.parseLocator(ConfigReader.getUsernameLocator());
        passwordField = LocatorParser.parseLocator(ConfigReader.getPasswordLocator());
        submitButton = LocatorParser.parseLocator(ConfigReader.getSubmitLocator());
        successMessage = LocatorParser.parseLocator(ConfigReader.getSuccessLocator());
        errorMessage = LocatorParser.parseLocator(ConfigReader.getErrorLocator());
        System.out.println("Locators initialized for Login page");
    }

    // ========== PAGE ACTION METHODS ==========

    /**
     * Enter username in the username field
     * Uses BasePage's sendKeys method which includes wait and clear
     * 
     * @param username Username to enter
     */
    public void enterUsername(String username) {
        sendKeys(usernameField, username);
    }

    /**
     * Enter password in the password field
     * Uses BasePage's sendKeys method which includes wait and clear
     * 
     * @param password Password to enter
     */
    public void enterPassword(String password) {
        sendKeys(passwordField, password);
    }

    /**
     * Click the submit/login button
     * Uses BasePage's clickElement method which includes wait for clickable
     */
    public void clickSubmitButton() {
        clickElement(submitButton);
    }

    /**
     * Get success message text from the page
     * Returns empty string if element is not found
     * 
     * @return Success message text
     */
    public String getSuccessMessage() {
        return getText(successMessage);
    }

    /**
     * Get error message text from the page
     * Returns empty string if element is not found
     * 
     * @return Error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    // ========== COMBINED ACTION METHODS ==========

    /**
     * Perform complete login action
     * Enters username, password, and clicks submit button
     * This is a high-level action method combining multiple steps
     * 
     * @param username Username to login with
     * @param password Password to login with
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSubmitButton();
        System.out.println("Login performed with username: " + username);
    }

    // ========== PAGE VERIFICATION/ASSERTION METHODS ==========

    /**
     * Check if login was successful
     * Verifies if the success message element is displayed
     * 
     * @return true if login successful (success message visible), false otherwise
     */
    public boolean isLoginSuccessful() {
        return isElementDisplayed(successMessage);
    }

    /**
     * Check if error message is displayed
     * Indicates that login failed or validation error occurred
     * 
     * @return true if error message is displayed, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    /**
     * Verify if currently on login page
     * Checks if both username and password fields are present
     * Useful for page verification at start of test
     * 
     * @return true if on login page, false otherwise
     */
    public boolean isOnLoginPage() {
        return isElementPresent(usernameField) && isElementPresent(passwordField);
    }
}
