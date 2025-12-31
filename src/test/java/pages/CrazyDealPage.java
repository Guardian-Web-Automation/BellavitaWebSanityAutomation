package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.ExtentLogger;

import java.time.Duration;
import java.util.List;

public class CrazyDealPage extends BasePage{

    public CrazyDealPage(WebDriver driver) {super(driver);}


    public void scrollToCrazyDealAndClick(String dealName) {
        try {
            String dealXpath = "//p[contains(@class,'product-title') and contains(normalize-space(),'" + dealName + "')]"
                            + "/ancestor::li//a[contains(@class,'card-link')]";

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement dealElement = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath(dealXpath))
            );

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", dealElement);

            dealElement.click();

            ExtentLogger.info("Clicked on Crazy Deal: " + dealName);

        } catch (Exception e) {
            ExtentLogger.fail("❌ Failed to click Crazy Deal → " + dealName + " | Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void addAnyThreeAvailableProductsToBox() {
        try {
            List<WebElement> availableProducts = driver.findElements(
                    By.xpath("//li[contains(@class,'collection-item')]  //input[@type='checkbox' and contains(@class,'button')]/ancestor::div[@id='checkboxes']")
            );

            if (availableProducts.size() < 3) {
                ExtentLogger.fail("Less than 3 available products found. Available count: " + availableProducts.size());
                throw new RuntimeException("Not enough available products to add");
            }

            ExtentLogger.info("Total Available Products Found: " + availableProducts.size());

            JavascriptExecutor js = (JavascriptExecutor) driver;
            int addedCount = 0;

            for (WebElement product : availableProducts) {
                if (addedCount == 3) {
                    break;
                }

                js.executeScript("arguments[0].scrollIntoView({block:'center'});", product);

                if (!product.isSelected()) {
                    product.click();
                    addedCount++;

                    String productName = product.getAttribute("value");
                    ExtentLogger.info("Added Product " + addedCount);
                }
            }

            ExtentLogger.pass("Successfully added " + addedCount + " available products to the box");

        } catch (Exception e) {
            ExtentLogger.fail("❌ Failed to add available products to box: " + e.getMessage());
            throw e;
        }
    }

    public void CrazyDealBuyNowButtonClick() {
        try {
            By buyNowButtonLocator = By.xpath("//button[@id='submit-btn']");

            ExtentLogger.info("Waiting for Buy Now button to become enabled...");

            WebElement buyNowButton = wait.until(ExpectedConditions.elementToBeClickable(buyNowButtonLocator));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", buyNowButton);

            buyNowButton.click();

            ExtentLogger.pass("Buy Now button is enabled and clicked successfully");


        } catch (Exception e) {
            ExtentLogger.fail("❌ Failed to click Buy Now button: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public String verifyProductNameInGoKwikCheckout() {

        String actualProductName;
        try {
            // 1️⃣ Switch to GoKwik iframe
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("gokwik-iframe")));
            ExtentLogger.info("Switched to GoKwik iframe");
            WebElement arrowElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[contains(@class,'globalImg arrow rotate')]"))
            );
            arrowElement.click();

            // 2️⃣ Locate product name
            WebElement productNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'product-details')]/div/div/div[contains(@class,'title')]"))
            );


            actualProductName = productNameElement.getText().trim();
            ExtentLogger.info("Product name found in checkout: " + actualProductName);

            // 4️⃣ Switch back to main page
            driver.switchTo().defaultContent();

        } catch (Exception e) {
            ExtentLogger.fail("❌ Failed to verify product name in GoKwik checkout: " + e.getMessage());
            throw e;
        }
        return actualProductName;
    }




}
