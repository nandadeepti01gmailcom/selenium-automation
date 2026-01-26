package com.example.utils;

import org.openqa.selenium.By;

/**
 * LocatorParser Class
 * Utility class to parse and convert locator strings to Selenium By objects
 * Supports multiple locator strategies (id, name, xpath, css, className, etc.)
 * Locator format: "strategy:value" (e.g., "id:username", "xpath://button[@id='submit']")
 */
public class LocatorParser {
    
    /**
     * Parse locator string and convert to Selenium By object
     * Supports formats: "id:value", "className:value", "xpath://expression", etc.
     * 
     * @param locatorString Locator string in format "locatorType:locatorValue"
     *                      Examples: "id:username", "className:post-title", "xpath://button[@id='submit']"
     * @return By object representing the locator
     * @throws IllegalArgumentException if locator string format is invalid or type is unsupported
     */
    public static By parseLocator(String locatorString) {
        if (locatorString == null || locatorString.isEmpty()) {
            throw new IllegalArgumentException("Locator string cannot be null or empty");
        }

        String[] parts = locatorString.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid locator format. Expected 'type:value'. Got: " + locatorString);
        }

        String locatorType = parts[0].trim().toLowerCase();
        String locatorValue = parts[1].trim();

        switch (locatorType) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "classname":
            case "className":
                return By.className(locatorValue);
            case "tagname":
            case "tagName":
                return By.tagName(locatorValue);
            case "css":
            case "cssselector":
                return By.cssSelector(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            case "linktext":
                return By.linkText(locatorValue);
            case "partiallinktext":
                return By.partialLinkText(locatorValue);
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        }
    }
}
