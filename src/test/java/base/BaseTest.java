package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.ExtentManager;

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
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver= new ChromeDriver();
        LOGGER.info("Launched ChromeDriver instance");
        driver.manage().window().maximize();
        // set page load timeout and small implicit wait as safety net for legacy operations
        try {
            driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(30));
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));
            driver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(20));
        } catch (Exception ignored) {}

        // start from a clean session
        try { driver.manage().deleteAllCookies(); } catch (Exception ignored) {}

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
