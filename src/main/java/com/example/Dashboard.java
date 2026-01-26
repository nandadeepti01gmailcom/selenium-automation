package com.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import com.example.base.BasePage;
import com.example.utils.LocatorParser;
import com.example.utils.ConfigReader;

/**
 * Dashboard Page Object
 * Represents the dashboard page and encapsulates all login-related operations
 * Follows Page Object Model (POM) design pattern by extending BasePage
 * 
 * All locators are loaded dynamically from config.properties
 * Page methods represent user actions (verify, etc.)
 * 
 * Usage:
 *   Login loginPage = new Login(driver);
 *   loginPage.login(username, password);
 *   boolean isLoggedIn = loginPage.isLoginSuccessful();
 */
public class Dashboard extends BasePage {

    // ========== PAGE LOCATORS - Loaded from config file ==========

    /**
     * Constructor for Dashboard page
     * Calls parent constructor and initializes all locators
     * 
     * @param driver WebDriver instance
     */
    public Dashboard(WebDriver driver) {
        super(driver);
    }

    /**
     * Get list of all menu items on the dashboard
     * Returns a list of text values for all menu elements
     * 
     * @return List of menu text values
     */
    public java.util.List<String> getMenuList() {
        java.util.List<String> menuTextList = new java.util.ArrayList<>();
        try {
            java.util.List<org.openqa.selenium.WebElement> menus = driver.findElements(By.xpath("//ul[@id='menu-primary-items']//a"));
            for (org.openqa.selenium.WebElement menu : menus) {
                String menuText = menu.getText().trim();
                if (!menuText.isEmpty()) {
                    menuTextList.add(menuText);
                    System.out.println("Menu found: " + menuText);
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving menu items: " + e.getMessage());
        }
        return menuTextList;
    }

    /**
     * Validate if a specific menu item exists in the dashboard
     * 
     * @param menuName Menu name to validate
     * @return true if menu exists, false otherwise
     */
    public boolean isMenuPresent(String menuName) {
        java.util.List<String> menus = getMenuList();
        return menus.contains(menuName);
    }
}
