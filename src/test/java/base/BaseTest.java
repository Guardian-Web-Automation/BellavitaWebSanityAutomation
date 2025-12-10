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
        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://bellavitaorganic.com/");

    }

    @AfterMethod
    public void tearUp(){
        if(driver!=null){
//            driver.quit();
        }
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush(); // generates the report
    }
}
