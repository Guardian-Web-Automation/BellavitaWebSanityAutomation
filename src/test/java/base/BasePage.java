package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;
    protected final WebDriverWait wait;
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());
   // protected final WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));

    private static final int RETRY_COUNT = 3;
    private static final long RETRY_DELAY_MS = 500;

    public BasePage(WebDriver driver){
        this.driver=driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected void click(By locator){
        LOGGER.info("Clicking on {}", locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /**
     * Robust click with retries and JS fallback.
     */
    protected void safeClick(By locator) {
        int attempts = 0;
        while (attempts < RETRY_COUNT) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                el.click();
                return;
            } catch (Exception e) {
                LOGGER.warn("Click attempt {} failed for {}: {}", attempts + 1, locator, e.getMessage());
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ignored) {}
                // try JS click as fallback
                try {
                    WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    return;
                } catch (Exception jsEx) {
                    LOGGER.warn("JS click failed for {}: {}", locator, jsEx.getMessage());
                }
                // try actions click as last fallback
                try {
                    WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    new Actions(driver).moveToElement(el).click().perform();
                    return;
                } catch (Exception actEx) {
                    LOGGER.warn("Actions click failed for {}: {}", locator, actEx.getMessage());
                }
            }
            attempts++;
        }
        throw new RuntimeException("Unable to click element: " + locator);
    }

    protected void type(By locator, String text){
        LOGGER.info("Typing '{}' into {}", text, locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).sendKeys(text);
    }

    /**
     * Robust typing: waits for visibility, clears field, then sends keys. Retries on failure.
     */
    protected void safeType(By locator, String text) {
        int attempts = 0;
        while (attempts < RETRY_COUNT) {
            try {
                WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                el.click();
                el.clear();
                el.sendKeys(text);
                return;
            } catch (Exception e) {
                LOGGER.warn("Type attempt {} failed for {}: {}", attempts + 1, locator, e.getMessage());
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ignored) {}
                // attempt JS set value
                try {
                    WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", el, text);
                    return;
                } catch (Exception jsEx) {
                    LOGGER.warn("JS set value failed for {}: {}", locator, jsEx.getMessage());
                }
            }
            attempts++;
        }
        throw new RuntimeException("Unable to type into element: " + locator);
    }

    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
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
