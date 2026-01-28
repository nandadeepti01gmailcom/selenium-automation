package com.example.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import com.example.base.BaseClass;
import com.example.pages.Login;
import com.example.pages.Dashboard;
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
public class DashboardTest extends BaseClass {

    private Login loginPage;
    private Dashboard dashboardPage;

    /**
     * Setup method - runs before each test
     * Calls setupAndLogin() from BaseClass to initialize browser and login
     */
    @BeforeEach
    public void setUp() {
        loginPage = setupAndLogin();
        dashboardPage = new Dashboard(driver);
    }

    /**
     * Test Priority: 1 (HIGHEST)
     * Verify that login page loads with correct title
     * This is the first test to verify the page is accessible
     * Foundation test - if this fails, others may fail due to page access issues
     */
    @Tag("ui")
    @Test
    @Order(1)
    public void testDashboardMenus() {
        System.out.println("\n=== Test Priority: 1 - Verify Dashboard Menu ===");

        // Get all menu items
        List<String> menuList = dashboardPage.getMenuList();

        // Assertions
        assertNotNull(menuList, "Menu list should not be null");
        System.out.println("Total menus found: " + menuList.size());
        
        if (!menuList.isEmpty()) {
            System.out.println("Menu items retrieved successfully:");
            for (String menu : menuList) {
                System.out.println("  - " + menu);
            }
            
            // Validate that menu list contains expected menu items (case-insensitive)
            assertTrue(menuList.stream().anyMatch(menu -> menu.equalsIgnoreCase("Homes")), "Menu should contain 'Home'");
            assertTrue(menuList.stream().anyMatch(menu -> menu.equalsIgnoreCase("Practice")), "Menu should contain 'Practice'");
            assertTrue(menuList.stream().anyMatch(menu -> menu.equalsIgnoreCase("Courses")), "Menu should contain 'Courses'");
            assertTrue(menuList.stream().anyMatch(menu -> menu.equalsIgnoreCase("Blog")), "Menu should contain 'Blog'");
            assertTrue(menuList.stream().anyMatch(menu -> menu.equalsIgnoreCase("Contact")), "Menu should contain 'Contact'");
            
            System.out.println("✓ Test PASSED: All expected menu items are present");
        } else {
            System.out.println("✗ Test FAILED: No menu items found on the page");
            fail("Menu list should not be empty");
        }
    }
}