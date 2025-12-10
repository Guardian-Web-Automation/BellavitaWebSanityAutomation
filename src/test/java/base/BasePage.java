package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;
    protected final WebDriverWait wait;
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());
   // protected final WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

    public BasePage(WebDriver driver){
        this.driver=driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected void click(By locator){
        LOGGER.info("Clicking on {}",locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text){
        LOGGER.info("Typing '{}' into {}", text, locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).sendKeys(text);
    }

    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    public void waitForPageLoad() {
                wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
        LOGGER.info("Page fully loaded");
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            System.out.println("Unable to scroll to element: " + e.getMessage());
        }
    }

}
