package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.ExtentManager;

import java.time.Duration;

public class BaseTest {
    public static WebDriver driver;
    public static ExtentReports extent;
    protected ExtentTest test;
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void setupReport() {
        extent = ExtentManager.getInstance();
    }


    @BeforeMethod
    public void setUp() {

        WebDriverManager.chromedriver().setup();

        // 🔹 Read headless value (default = false)
        String headlessValue = System.getProperty("headless", "false");
        boolean isHeadless = headlessValue.equalsIgnoreCase("true");

        ChromeOptions options = new ChromeOptions();

        if (isHeadless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            LOGGER.info("Running tests in HEADLESS mode");
        } else {
            LOGGER.info("Running tests in UI (Browser) mode");
        }

        driver = new ChromeDriver(options);
        LOGGER.info("Launched ChromeDriver instance");

        if (!isHeadless) {
            driver.manage().window().maximize();
        }

        try {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(20));
        } catch (Exception ignored) {}

        try {
            driver.manage().deleteAllCookies();
        } catch (Exception ignored) {}

        driver.get("https://bellavitaorganic.com/");
        LOGGER.info("Navigated to base URL");
    }


    @AfterMethod
    public void tearUp(){
        if(driver!=null){
            // ensure browser is closed after each test
            try {
                driver.quit();
            } catch (Exception e) {
                LOGGER.warn("Error quitting WebDriver: {}", e.getMessage());
            }
        }
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush(); // generates the report
    }
}
