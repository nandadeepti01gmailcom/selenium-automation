# Test Tags Usage Guide

## Overview
This project uses a centralized tagging system for test organization and selective test execution. All tags are defined in `Tags.java` for consistency and reusability.

## Available Tags

### 🎯 Test Suite Categories
- `smoke` - Critical fast tests for core functionality (< 5 min)
- `regression` - Comprehensive test suite for all features
- `sanity` - Quick verification tests after builds

### ✅ Test Types
- `positive` - Happy path scenarios with valid inputs
- `negative` - Error handling and invalid input tests
- `edge` - Edge cases and boundary conditions

### 📦 Functional Areas
- `login` - Authentication and login tests
- `dashboard` - Dashboard/home page tests
- `profile` - User profile and account tests
- `navigation` - Menu and navigation tests
- `search` - Search functionality tests

### 🔧 Technology/Layer
- `ui` - Web UI tests (Selenium)
- `api` - API/backend tests
- `database` - Database tests
- `integration` - External service integration tests

### ⚡ Priority Levels
- `p0` - Critical, blocks release
- `p1` - High priority, important for release
- `p2` - Medium priority, standard tests
- `p3` - Low priority, non-critical

### 🌍 Environment Specific
- `dev-only` - Only for dev environment
- `staging-only` - Only for staging
- `prod-safe` - Safe to run in production

### 🏷️ Special Tags
- `wip` - Work in progress / unstable tests
- `slow` - Long-running tests
- `manual` - Requires manual verification

## Usage in Test Classes

### Using Tags in Tests
```java
import static com.example.tests.Tags.*;

@Tag(SMOKE)
@Tag(P0)
@Test
public void testLoginPageTitle() {
    // Test code
}

@Tag(REGRESSION)
@Tag(NEGATIVE)
@Tag(P1)
@Test
public void testLoginWithInvalidCredentials() {
    // Test code
}
```

### Using Tags at Class Level
```java
@Tag(REGRESSION)
@Tag(LOGIN)
@Tag(UI)
public class LoginTest extends BaseClass {
    // All tests in this class inherit these tags
}
```

## Running Tests by Tags

### Maven Command Line

#### Run tests with specific tag
```bash
# Run only smoke tests
mvn test -DincludeTags=smoke

# Run only regression tests
mvn test -DincludeTags=regression

# Run integration tests
mvn test -DincludeTags=integration
```

#### Run multiple tags (OR logic)
```bash
# Run smoke OR p0 tests
mvn test -DincludeTags="smoke | p0"

# Run login OR dashboard tests
mvn test -DincludeTags="login | dashboard"
```

#### Run tests matching all tags (AND logic)
```bash
# Run tests that are BOTH smoke AND login
mvn test -DincludeTags="smoke & login"

# Run tests that are BOTH regression AND negative
mvn test -DincludeTags="regression & negative"
```

#### Exclude specific tags
```bash
# Run all tests except slow ones
mvn test -DexcludeTags=slow

# Run all tests except WIP and slow
mvn test -DexcludeTags="wip,slow"

# Run all tests except integration
mvn test -DexcludeTags=integration
```

#### Combine include and exclude
```bash
# Run smoke tests but exclude slow ones
mvn test -DincludeTags=smoke -DexcludeTags=slow

# Run regression but exclude integration tests
mvn test -DincludeTags=regression -DexcludeTags=integration
```

### PowerShell Scripts (Windows)

Create a `run-tests.ps1` script:
```powershell
# Run smoke tests
mvn test -DincludeTags=smoke

# Run by priority
mvn test -DincludeTags=p0
mvn test -DincludeTags="p0 | p1"

# Run by functional area
mvn test -DincludeTags=login
mvn test -DincludeTags=dashboard

# Run negative tests
mvn test -DincludeTags=negative
```

### Common Test Execution Scenarios

#### 1. Quick Smoke Test (Pre-commit)
```bash
mvn test -DincludeTags=smoke -DexcludeTags=slow
```

#### 2. Full Regression Before Release
```bash
mvn test -DincludeTags=regression
```

#### 3. Critical Tests Only
```bash
mvn test -DincludeTags=p0
```

#### 4. Feature-Specific Tests
```bash
# All login tests
mvn test -DincludeTags=login

# All dashboard tests
mvn test -DincludeTags=dashboard
```

#### 5. Negative Testing Suite
```bash
mvn test -DincludeTags="negative | edge"
```

#### 6. Integration Tests Only
```bash
mvn test -DincludeTags=integration
```

#### 7. UI Tests Only (exclude API/DB)
```bash
mvn test -DincludeTags=ui -DexcludeTags="api,database"
```

## Current Test Coverage

| Test Class | Tags Applied |
|------------|--------------|
| **LoginTest** | `regression`, `login`, `ui` |
| └─ testLoginPageTitle | `smoke`, `p0` |
| └─ testLoginPageURL | `smoke`, `p0` |
| └─ testLoginWithValidCredentials | `smoke`, `positive`, `p0` |
| └─ testLoginWithInvalidCredentials | `regression`, `negative`, `p1` |
| └─ testLoginWithEmptyCredentials | `regression`, `edge`, `p2` |
| **DashboardTest** | `regression`, `dashboard`, `ui`, `navigation` |
| └─ testDashboardMenus | `smoke`, `p0` |
| **SlackIntegrationTest** | `integration`, `smoke` |
| └─ testSendSimpleSlackMessage | `p1` |

## CI/CD Integration

### GitHub Actions Example
```yaml
- name: Run Smoke Tests
  run: mvn test -DincludeTags=smoke

- name: Run Regression Tests
  run: mvn test -DincludeTags=regression -DexcludeTags=slow
```

### Jenkins Pipeline Example
```groovy
stage('Smoke Tests') {
    steps {
        sh 'mvn test -DincludeTags=smoke'
    }
}

stage('Regression Tests') {
    steps {
        sh 'mvn test -DincludeTags=regression'
    }
}
```

## Best Practices

1. **Tag Every Test** - Always use at least 2-3 tags per test
2. **Use Priority Tags** - Assign p0/p1/p2/p3 to all tests
3. **Combine Functional + Type Tags** - e.g., `login` + `positive`
4. **Keep Smoke Suite Small** - Only critical tests with `smoke` tag
5. **Mark Unstable Tests** - Use `wip` tag for flaky tests
6. **Document New Tags** - Update Tags.java and this guide

## Adding New Tags

1. Open `src/test/java/com/example/tests/Tags.java`
2. Add new constant with Javadoc:
```java
/**
 * Tests for checkout functionality.
 */
public static final String CHECKOUT = "checkout";
```
3. Update this documentation
4. Use in test classes:
```java
@Tag(CHECKOUT)
@Test
public void testCheckoutFlow() { ... }
```

## Tag Naming Conventions

- Use lowercase for tag values
- Use descriptive, clear names
- Avoid special characters
- Keep tags short and memorable
- Use hyphens for multi-word tags (e.g., `dev-only`)

## Troubleshooting

### No tests executed
```bash
# Check if tag exists and is spelled correctly
mvn test -DincludeTags=smokes  # Wrong!
mvn test -DincludeTags=smoke   # Correct
```

### All tests run despite tag filter
- Ensure surefire plugin version is 2.22.0+ in pom.xml
- Check that @Tag annotation is imported from `org.junit.jupiter.api.Tag`

### Test shows in multiple runs
- Tests inherit all tags from class level AND method level
- This is expected behavior for comprehensive coverage
