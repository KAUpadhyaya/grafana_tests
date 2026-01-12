package com.grafana;

import com.grafana.data.Properties;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public class UITestBase extends Properties {
    private final Logger LOG = LogManager.getLogger(UITestBase.class);

    /*
    Note:
        I'm using the ThreadLocal to ensure that we store one WebDriver per thread.
        If there is requirement for parallel execution, we can ensure there is no overwriting between threads.
     */
     protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public String ENV;
    Util fromCommonTestUtil = new Util();

    @BeforeClass(groups = {"smoke", "regression"})
    public void setUp() {
        ENV = fromCommonTestUtil.setTestEnvironment();
        LOG.info("***************************************************");
        LOG.info("Starting UI test in {}", ENV);
        LOG.info("***************************************************");
        launchBrowser(BROWSER);
        getDriver().get(BASE_URI);

        /*
        Note:
            Im setting the implicit wait within Base class to ensure
            that we need not set it all the time in test case classes
            this is prevent multiple waits inteferring with each other
     */
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterClass(groups = {"smoke"})
    public void tearDown(){
        getDriver().quit();
    }

    /*
            Browser Utilities.
     */
    protected void setBrowserProperties(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(
                        "--start-maximized",
                        "--disable-notifications",
                        "--disable-infobars",
                        "--disable-extensions",
                        "--remote-allow-origins=*"
                );
                chromeOptions.setAcceptInsecureCerts(true);

                driver.set(new ChromeDriver(chromeOptions));
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                firefoxOptions.setAcceptInsecureCerts(true);

                driver.set(new FirefoxDriver(firefoxOptions));
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
    protected void launchBrowser(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                setBrowserProperties(browser);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                setBrowserProperties(browser);
                break;

            default:
                throw new IllegalArgumentException("Invalid browser name: " + browser
                        +" ; at present the supported browsers are chrome and firefox...");
        }
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        getDriver().manage().deleteAllCookies();
    }
    protected WebDriver getDriver() {return driver.get();}


}
