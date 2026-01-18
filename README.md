# Grafana Test Framework

A comprehensive test automation framework for Grafana built with Java, TestNG, Selenium, and REST Assured. This framework supports both **API-level** and **UI-level** testing with detailed reporting and logging capabilities.

## Overview

This project is designed to perform automated testing of Grafana dashboards, alerts, and APIs. It provides a robust foundation for smoke tests and regression tests with parallel execution capabilities, thread-safe driver management, and integrated test reporting.

---

## Framework Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                      GRAFANA TEST FRAMEWORK                         │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                          TEST EXECUTION LAYER                       │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────┐        ┌──────────────────────┐          │
│  │   API Smoke Tests    │        │   UI Smoke Tests     │          │
│  │  (APISmokeTests)     │        │  (UISmokeTests)      │          │
│  └──────────────────────┘        └──────────────────────┘          │
│          │                                │                         │
│          └────────────┬────────────────────┘                        │
│                       │                                             │
└───────────────────────┼─────────────────────────────────────────────┘
                        │
        ┌───────────────┴────────────────┐
        │                                │
        ▼                                ▼
┌──────────────────────┐      ┌──────────────────────┐
│   APITestBase        │      │   UITestBase         │
│  - Setup/Teardown    │      │  - Setup/Teardown    │
│  - Request/Response  │      │  - Browser Launch    │
│    Specs             │      │  - Driver Management │
│  - JSON Extraction   │      │  - Implicit Waits    │
└──────────────────────┘      └──────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                      TEST UTILITIES & LISTENERS                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐               │
│  │  APITestListener     │  │  UITestListener      │               │
│  │  - Test Results      │  │  - Test Results      │               │
│  │  - Extent Reports    │  │  - Extent Reports    │               │
│  └──────────────────────┘  └──────────────────────┘               │
│                                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐               │
│  │  ExtentManager       │  │  APIUtil             │               │
│  │  - Report Config     │  │  - API Helpers       │               │
│  │  - Report Flush      │  │  - Assertions        │               │
│  └──────────────────────┘  └──────────────────────┘               │
│                                                                     │
│  ┌──────────────────────┐                                          │
│  │  Util (Common)       │                                          │
│  │  - Env Setup         │                                          │
│  │  - Shared Helpers    │                                          │
│  └──────────────────────┘                                          │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                      DATA & CONFIGURATION LAYER                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐               │
│  │  Properties          │  │  PropertyReader      │               │
│  │  - Base URI          │  │  - Config Loading    │               │
│  │  - Credentials       │  │  - Env Properties    │               │
│  │  - Browser Type      │  │  - Fallback Values   │               │
│  └──────────────────────┘  └──────────────────────┘               │
│                                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐               │
│  │  APIResources        │  │  ForDashboard        │               │
│  │  - API Endpoints     │  │  - Dashboard Test    │               │
│  │  - Resource URLs     │  │    Data              │               │
│  └──────────────────────┘  └──────────────────────┘               │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                        DOMAIN MODEL LAYER (POJOs)                   │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────────────────────────────┐                 │
│  │  Dashboard POJOs                             │                 │
│  │  - Dashboard Objects                         │                 │
│  │  - Panel Objects                             │                 │
│  │  - Serialization/Deserialization (JSON)      │                 │
│  └──────────────────────────────────────────────┘                 │
│                                                                     │
│  ┌──────────────────────────────────────────────┐                 │
│  │  Alert POJOs                                 │                 │
│  │  - Alert Objects                             │                 │
│  │  - Alert Rule Objects                        │                 │
│  └──────────────────────────────────────────────┘                 │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                      UI PAGE OBJECTS & UTILITIES                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐               │
│  │  Page Objects        │  │  UI Utilities        │               │
│  │  - Login Page        │  │  - Element Finders   │               │
│  │  - Dashboard Page    │  │  - Interactions      │               │
│  │  - Alert Pages       │  │  - Waits/Retries     │               │
│  └──────────────────────┘  └──────────────────────┘               │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                     EXTERNAL DEPENDENCIES                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐               │
│  │  REST Assured        │  │  Selenium WebDriver  │               │
│  │  - REST API Testing  │  │  - Browser Control   │               │
│  │  - Request Builders  │  │  - Element Location  │               │
│  │  - Response Parsing  │  │  - Page Interaction  │               │
│  └──────────────────────┘  └──────────────────────┘               │
│                                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐               │
│  │  TestNG             │  │  Extent Reports      │               │
│  │  - Test Execution    │  │  - Report Generation │               │
│  │  - Listeners         │  │  - Test Logging      │               │
│  │  - Groups/Suites     │  │  - Dashboard View    │               │
│  └──────────────────────┘  └──────────────────────┘               │
│                                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐               │
│  │  WebDriver Manager   │  │  Log4j2              │               │
│  │  - Driver Setup      │  │  - Logging           │               │
│  │  - Version Mgmt      │  │  - Log Config        │               │
│  └──────────────────────┘  └──────────────────────┘               │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Project Structure

```
grafana_tests/
├── pom.xml                          # Maven configuration
├── setupnotes                       # Setup instructions
├── README.md                        # This file
│
├── src/
│   ├── main/
│   │   ├── java/com/grafana/
│   │   │   ├── Util.java                           # Common utilities
│   │   │   ├── api/
│   │   │   │   └── pojo/                           # API Response/Request POJOs
│   │   │   │       ├── dashboard/
│   │   │   │       └── alerts/
│   │   │   ├── data/
│   │   │   │   ├── Properties.java                 # Configuration properties
│   │   │   │   ├── PropertyReader.java             # Property file loader
│   │   │   │   └── APIResources.java               # API endpoint definitions
│   │   │   ├── testData/
│   │   │   │   └── ForDashboard.java               # Dashboard test data
│   │   │   └── ui/
│   │   │       ├── Util.java                       # UI utilities
│   │   │       └── pages/                          # Page Object Model
│   │   │           ├── LoginPage.java
│   │   │           └── DashboardPage.java
│   │   └── resources/                              # Main resources
│   │
│   └── test/
│       ├── java/com/grafana/
│       │   ├── APITestBase.java                    # Base class for API tests
│       │   ├── UITestBase.java                     # Base class for UI tests
│       │   ├── reporting/
│       │   │   ├── APITestListener.java            # API test listener
│       │   │   ├── UITestListener.java             # UI test listener
│       │   │   └── ExtentManager.java              # Extent report manager
│       │   ├── suites/
│       │   │   ├── APISmokeTests.xml               # API test suite
│       │   │   └── UISmokeTests.xml                # UI test suite
│       │   ├── tests/
│       │   │   ├── api/                            # API test classes
│       │   │   │   └── DashboardTests.java
│       │   │   └── ui/                             # UI test classes
│       │   │       ├── LoginTests.java
│       │   │       └── DashboardTests.java
│       │   └── testUtil/
│       │       └── APIUtil.java                    # API test utilities
│       └── resources/
│           ├── log4j2.xml                          # Logging configuration
│           ├── env/
│           │   └── qc.properties                   # Environment properties
│           └── requestBodies/
│               └── createFolderInDashboard.json    # Sample JSON request body
│
└── target/                          # Build output
    ├── classes/                     # Compiled main code
    ├── test-classes/                # Compiled test code
    └── surefire-reports/            # Test execution reports
```

---

## Key Components

### Test Base Classes

**APITestBase.java**
- Sets up REST Assured request and response specifications
- Configures basic authentication with Grafana credentials
- Provides methods to extract JSON bodies from request files
- Manages test environment setup before suite execution

**UITestBase.java**
- Initializes WebDriver (Chrome/Firefox) with WebDriver Manager
- Sets up ThreadLocal driver management for parallel execution
- Configures implicit waits for element interactions
- Handles browser launch and teardown operations

### Data Layer

**Properties.java**
- Centralized configuration holder
- Contains BASE_URI, USERNAME, PASSWORD, BROWSER type
- Extends PropertyReader for dynamic property loading

**PropertyReader.java**
- Loads properties from `env/qc.properties`
- Supports environment-specific configurations
- Provides fallback default values

**APIResources.java**
- Defines all API endpoints and resource URLs
- Centralized API path management

### Reporting & Listeners

**ExtentManager.java**
- Manages Extent Report creation and configuration
- Handles report instantiation and flushing

**APITestListener.java**
- Listens to API test execution events
- Logs results to Extent Reports
- Tracks test pass/fail status

**UITestListener.java**
- Monitors UI test execution
- Captures screenshots on failure
- Records results in Extent Reports

### Test Suites

**APISmokeTests.xml**
- Defines API smoke test suite
- Controls test execution groups and parameters

**UISmokeTests.xml**
- Defines UI smoke test suite
- Controls browser type and parallel execution settings

---

## Technologies Used

| Component | Technology | Version |
|-----------|-----------|---------|
| **Test Framework** | TestNG | 7.11.0 |
| **Web Automation** | Selenium WebDriver | 4.39.0 |
| **API Testing** | REST Assured | 6.0.0 |
| **Driver Management** | WebDriver Manager | 6.3.3 |
| **Reporting** | Extent Reports | 5.1.2 |
| **Logging** | Log4j2 | 2.25.3 |
| **Build Tool** | Maven | 3.x |
| **Java** | JDK | 22 |
| **JSON Processing** | org.json | 20250517 |
| **Utilities** | Lombok | 1.18.42 |
| **HTML Parsing** | jsoup | latest |

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK 22)
- Maven 3.6.0 or higher
- Grafana instance running (local or remote)
- Chrome/Firefox browser (for UI tests)

### Setup Instructions

1. **Clone/Download the project**
   ```bash
   cd grafana_tests
   ```

2. **Configure Environment Properties**
   - Edit `src/test/resources/env/qc.properties`
   - Set your Grafana BASE_URI, USERNAME, and PASSWORD
   - Example:
     ```properties
     BASE_URI=http://localhost:3000
     USERNAME=admin
     PASSWORD=admin@123
     BROWSER=chrome
     ```

3. **Install Dependencies**
   ```bash
   mvn clean install
   ```

4. **Run Grafana Locally (Optional)**
   - Using Docker:
     ```bash
     docker run -d -p 3000:3000 --name grafana grafana/grafana-oss
     ```
   - Access at `http://localhost:3000`
   - Default credentials: `admin` / `admin`

### Running Tests

**Execute API Smoke Tests**
```bash
mvn clean test -DsuiteXmlFiles="src/test/java/com/grafana/suites/APISmokeTests.xml"
```

**Execute UI Smoke Tests**
```bash
mvn clean test -DsuiteXmlFiles="src/test/java/com/grafana/suites/UISmokeTests.xml"
```

**Execute All Tests**
```bash
mvn clean test
```

---

## Test Reports

Test reports are generated in the `target/surefire-reports/` directory:

- **HTML Report**: `target/surefire-reports/index.html`
- **Extent Report**: Generated by ExtentManager for detailed test insights
- **JUnit XML Reports**: Machine-readable test results in `junitreports/` folder

---

## Features

✅ **Dual Testing Support**
- REST API testing with REST Assured
- UI testing with Selenium WebDriver

✅ **Thread-Safe Execution**
- ThreadLocal WebDriver management for parallel tests
- Prevents driver conflicts in multi-threaded execution

✅ **Comprehensive Reporting**
- Extent Reports integration
- Log4j2 logging for detailed test logs
- Screenshot capture on UI test failures

✅ **Configuration Management**
- Environment-specific properties
- Dynamic property loading
- Fallback default values

✅ **Test Organization**
- TestNG suite definitions (APISmokeTests, UISmokeTests)
- Support for test groups (smoke, regression)
- Easy test filtering and execution control

✅ **Scalability**
- Maven-based build system
- Support for parallel test execution
- Modular architecture for easy extension

---

## Best Practices Implemented

1. **Page Object Model (POM)** - Separates UI elements from test logic
2. **Base Classes** - Reusable setup/teardown and configuration
3. **Data Externalization** - Properties and test data in external files
4. **Listener Pattern** - Test event monitoring and reporting
5. **DRY Principle** - Utility classes for shared functionality
6. **ThreadLocal Pattern** - Thread-safe driver management for parallel execution
7. **Logging** - Comprehensive logging at all levels using Log4j2

---

## Troubleshooting

### WebDriver Issues
- Ensure WebDriver Manager has internet access for driver download
- Verify browser version compatibility with Selenium version

### Connection Issues
- Confirm Grafana is running and accessible at BASE_URI
- Verify USERNAME and PASSWORD are correct
- Check firewall rules for port access

### Test Execution Issues
- Clean build: `mvn clean install`
- Check TestNG suite XML files for correct test class references
- Verify Java version: `java -version` should show JDK 22

---

## Contributing

Guidelines for extending the framework:

1. **Adding API Tests**
   - Create new test class extending `APITestBase`
   - Add corresponding POJOs for request/response objects
   - Update `APISmokeTests.xml` with new test class

2. **Adding UI Tests**
   - Create new Page Object class in `ui/pages/`
   - Create test class extending `UITestBase`
   - Update `UISmokeTests.xml` with new test class

3. **Adding Utilities**
   - Extend existing `Util` classes or create specialized utilities
   - Ensure thread-safety for shared utilities
   - Document utility methods clearly

---

## License

This project is internal QA test automation framework.

---

## Contact & Support

For questions or issues, refer to the setupnotes file or consult the framework documentation.
