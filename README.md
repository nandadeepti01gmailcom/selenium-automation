# Selenium Testing Project

## Overview
This project is a Maven-based Selenium testing framework designed for automated web application testing. It provides a structured approach to writing and executing tests using Selenium WebDriver.

## Project Structure
```
selenium-testing-project
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── example
│   │               └── utils
│   │                   └── WebDriverUtils.java
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── tests
│                       └── BaseTest.java
├── pom.xml
└── README.md
```

## Setup Instructions
1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd selenium-testing-project
   ```

2. **Build the project**:
   Use Maven to build the project and download the necessary dependencies.
   ```
   mvn clean install
   ```

3. **Run the tests**:
   You can run the tests using Maven:
   ```
   mvn test
   ```

## Usage
- The `WebDriverUtils` class provides utility methods for initializing and managing the Selenium WebDriver instance.
- The `BaseTest` class serves as a foundation for your test classes, ensuring that the WebDriver is set up and torn down correctly.

## Contributing
Feel free to submit issues or pull requests if you have suggestions or improvements for the project.

## License
This project is licensed under the MIT License. See the LICENSE file for details.