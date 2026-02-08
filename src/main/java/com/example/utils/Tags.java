package com.example.utils;

/**
 * Centralized Test Tags
 * =====================
 * This class defines all test tag constants used throughout the test suite.
 * Use these constants with @Tag annotation for consistent test categorization.
 * 
 * Usage Example:
 * @Tag(Tags.SMOKE)
 * @Tag(Tags.REGRESSION)
 * public void testLoginWithValidCredentials() { ... }
 * 
 * Running tests by tag:
 * mvn test -DincludeTags=smoke
 * mvn test -DincludeTags=regression
 * mvn test -DincludeTags=smoke,regression
 * mvn test -DexcludeTags=integration
 */
public final class Tags {
    
    // ========== Test Suite Categories ==========
    /**
     * Critical tests that must pass before any release.
     * Quick tests covering core functionality.
     * Should run in < 5 minutes.
     */
    public static final String SMOKE = "smoke";
    
    /**
     * Comprehensive test suite covering all features.
     * Run before major releases or merges.
     * May take longer to execute.
     */
    public static final String REGRESSION = "regression";
    
    /**
     * Sanity tests for quick verification after builds.
     * Subset of smoke tests for rapid feedback.
     */
    public static final String SANITY = "sanity";
    
    // ========== Test Types ==========
    /**
     * Tests validating positive/happy path scenarios.
     * Testing with valid inputs and expected user flows.
     */
    public static final String POSITIVE = "positive";
    
    /**
     * Tests validating negative scenarios and error handling.
     * Testing with invalid inputs, edge cases, boundary conditions.
     */
    public static final String NEGATIVE = "negative";
    
    /**
     * Tests for edge cases and boundary conditions.
     * Empty inputs, special characters, limits, etc.
     */
    public static final String EDGE_CASE = "edge";
    
    // ========== Functional Areas ==========
    /**
     * Tests for login/authentication functionality.
     */
    public static final String LOGIN = "login";
    
    /**
     * Tests for dashboard/home page functionality.
     */
    public static final String DASHBOARD = "dashboard";
    
    /**
     * Tests for user profile and account management.
     */
    public static final String PROFILE = "profile";
    
    /**
     * Tests for navigation and menu functionality.
     */
    public static final String NAVIGATION = "navigation";
    
    /**
     * Tests for search functionality.
     */
    public static final String SEARCH = "search";
    
    // ========== Technology/Layer ==========
    /**
     * User Interface / Web UI tests using Selenium.
     */
    public static final String UI = "ui";
    
    /**
     * API/Backend service tests.
     */
    public static final String API = "api";
    
    /**
     * Database tests.
     */
    public static final String DATABASE = "database";
    
    /**
     * Integration tests (external services, Slack, email, etc).
     */
    public static final String INTEGRATION = "integration";
    
    // ========== Priority Levels ==========
    /**
     * Critical priority - Must pass, blocks release.
     */
    public static final String P0 = "p0";
    
    /**
     * High priority - Important tests, should pass before release.
     */
    public static final String P1 = "p1";
    
    /**
     * Medium priority - Standard tests.
     */
    public static final String P2 = "p2";
    
    /**
     * Low priority - Nice to have, non-critical.
     */
    public static final String P3 = "p3";
    
    // ========== Environment Specific ==========
    /**
     * Tests that should only run in dev environment.
     */
    public static final String DEV_ONLY = "dev-only";
    
    /**
     * Tests that should only run in staging environment.
     */
    public static final String STAGING_ONLY = "staging-only";
    
    /**
     * Tests safe to run in production environment.
     */
    public static final String PROD_SAFE = "prod-safe";
    
    // ========== Special Tags ==========
    /**
     * Tests that are currently failing or unstable (flaky).
     * Excluded from regular runs until fixed.
     */
    public static final String WIP = "wip";
    
    /**
     * Slow-running tests that may be excluded from quick runs.
     */
    public static final String SLOW = "slow";
    
    /**
     * Tests that require manual verification or intervention.
     */
    public static final String MANUAL = "manual";
    
    // Private constructor to prevent instantiation
    private Tags() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
