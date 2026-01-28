package com.example.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import com.example.base.BaseClass;
import com.example.pages.Login;
import com.example.utils.TestDataManager;
import com.example.utils.ExtentReportListener;

/**
 * Login Test Class
 * Contains test cases for login functionality
 * Uses Page Object Model pattern through Login page class
 * Generates Extent Report for test execution
 * 
 * Test Execution Order (Priority):
 * 1. testLoginPageTitle - Verify page loads correctly
 * 2. testLoginPageURL - Verify correct URL
 * 3. testLoginWithValidCredentials - Test positive scenario
 * 4. testLoginWithInvalidCredentials - Test negative scenario
 * 5. testLoginWithEmptyCredentials - Test edge case
 * 
 * Tests are ordered to verify basic functionality first, then proceed to login tests
 */
@ExtendWith(ExtentReportListener.class)
@Tag("regression")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest extends BaseClass {

    /**
     * Test Priority: 1 (HIGHEST)
     * Verify that login page loads with correct title
     * This is the first test to verify the page is accessible
     * Foundation test - if this fails, others may fail due to page access issues
     */
    @Tag("smoke")
    @Test
    @Order(1)
    public void testLoginPageTitle() {
        // Launch the browser with URL from test data manager
        launchBrowser(BASE_URL);
        System.out.println("\n=== Test Priority: 1 - Verify Login Page Title ===");
        System.out.println("Environment: " + TestDataManager.getEnvironment());
        System.out.println("URL launched: " + BASE_URL);

        // Create Login page object
        Login loginPage = new Login(driver);

        // Get credentials from test data manager
        String validUsername = TestDataManager.getValidUsername();
        String validPassword = TestDataManager.getValidPassword();

        // Perform login with credentials
        loginPage.login(validUsername, validPassword);

        // Wait for page to load
        loginPage.customWait(TestDataManager.getPageWait());

        // Get page title
        String pageTitle = loginPage.getPageTitle();
        System.out.println("Page Title: " + pageTitle);

        // Assert that page title is not empty
        assertNotNull(pageTitle, "Page title should not be null");
        assertFalse(pageTitle.isEmpty(), "Page title should not be empty");
        System.out.println("✓ Test PASSED: Login page loaded successfully with title: " + pageTitle);
    }

    @Tag("smoke")
    @Test
    @Order(2)
    public void testLoginPageURL() {
        // Launch the browser with URL from test data manager
        launchBrowser(BASE_URL);
        System.out.println("\n=== Test Priority: 2 - Verify Login Page URL ===");
        System.out.println("Environment: " + TestDataManager.getEnvironment());
        System.out.println("URL launched: " + BASE_URL);

        // Create Login page object
        Login loginPage = new Login(driver);

        // Get credentials from test data manager
        String validUsername = TestDataManager.getValidUsername();
        String validPassword = TestDataManager.getValidPassword();

        // Perform login with credentials
        loginPage.login(validUsername, validPassword);

        // Wait for page to load
        loginPage.customWait(TestDataManager.getPageWait());

        // Get current URL
        String currentURL = loginPage.getCurrentURL();
        System.out.println("Current URL: " + currentURL);

        // Assert that current URL matches the base URL
        assertTrue(currentURL.contains(BASE_URL.split("/")[2]), "URL should contain base URL domain");
        System.out.println("✓ Test PASSED: Login page URL verified");
    }

    @Tag("smoke")
    @Test
    @Order(3)
    public void testLoginWithValidCredentials() {
        // Launch the browser with URL from test data manager
        launchBrowser(BASE_URL);
        System.out.println("\n=== Test Priority: 3 - Login with Valid Credentials ===");
        System.out.println("Environment: " + TestDataManager.getEnvironment());
        System.out.println("URL launched: " + BASE_URL);

        // Create Login page object
        Login loginPage = new Login(driver);

        // Verify we are on login page
        assertTrue(loginPage.isOnLoginPage(), "Not on login page");

        // Get credentials from test data manager
        String validUsername = TestDataManager.getValidUsername();
        String validPassword = TestDataManager.getValidPassword();

        // Perform login with credentials
        loginPage.login(validUsername, validPassword);

        // Wait for page to load
        loginPage.customWait(TestDataManager.getPageWait());

        // Assert that login was successful
        assertTrue(loginPage.isLoginSuccessful(), "Login failed - success page not displayed");
        String successMessage = loginPage.getSuccessMessage();
        assertNotNull(successMessage, "Success message not found");
        assertFalse(successMessage.isEmpty(), "Success message is empty");
        System.out.println("✓ Test PASSED: Login with valid credentials successful");
    }

    @Tag("negative")
    @Test
    @Order(4)
    public void testLoginWithInvalidCredentials() {
        // Launch the browser with URL from test data manager
        launchBrowser(BASE_URL);
        System.out.println("\n=== Test Priority: 4 - Login with Invalid Credentials ===");
        System.out.println("Environment: " + TestDataManager.getEnvironment());
        System.out.println("URL launched: " + BASE_URL);

        // Create Login page object
        Login loginPage = new Login(driver);

        // Verify we are on login page
        assertTrue(loginPage.isOnLoginPage(), "Not on login page");

        // Get invalid credentials from test data manager
        String invalidUsername = TestDataManager.getInvalidUsername();
        String invalidPassword = TestDataManager.getInvalidPassword();

        // Perform login with invalid credentials
        loginPage.login(invalidUsername, invalidPassword);

        // Wait for page to load
        loginPage.customWait(TestDataManager.getPageWait());

        // Assert that login failed
        assertFalse(loginPage.isLoginSuccessful(), "Login should have failed with invalid credentials");
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        String errorMessage = loginPage.getErrorMessage();
        assertNotNull(errorMessage, "Error message should not be null");
        System.out.println("✓ Test PASSED: Invalid credentials properly rejected");
    }

    @Tag("edge")
    @Test
    @Order(5)
    public void testLoginWithEmptyCredentials() {
        // Launch the browser with URL from test data manager
        launchBrowser(BASE_URL);
        System.out.println("\n=== Test Priority: 5 - Login with Empty Credentials ===");
        System.out.println("Environment: " + TestDataManager.getEnvironment());
        System.out.println("URL launched: " + BASE_URL);

        // Create Login page object
        Login loginPage = new Login(driver);

        // Verify we are on login page
        assertTrue(loginPage.isOnLoginPage(), "Not on login page");

        // Try to click submit without entering credentials
        loginPage.clickSubmitButton();

        // Wait for page to load
        loginPage.customWait(TestDataManager.getPageWait());

        // Assert that login failed
        assertFalse(loginPage.isLoginSuccessful(), "Login should have failed with empty credentials");
        System.out.println("✓ Test PASSED: Empty credentials properly handled");
    }

}
