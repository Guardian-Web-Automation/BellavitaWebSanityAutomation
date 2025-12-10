package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.ExtentLogger;

public class PDPPage extends BasePage {
    private By qtyIncreaseBtn = By.xpath("//button[contains(@class,'quantity__button') and @name='plus']");
    private By qtyDecreaseBtn = By.xpath("//button[contains(@class,'quantity__button') and @name='minus']");
    private By qtyInput = By.xpath("//input[@id='Quantity-buy_buttons' and @class='quantity__input']");
    private By addToCartBtn = By.cssSelector("button[name='add']");



    public PDPPage(WebDriver driver) {
        super(driver);
    }


    public void setQuantity(int targetQty) {
        ExtentLogger.info("Setting quantity to: " + targetQty);

        WebElement qtyBox = wait.until(ExpectedConditions.visibilityOfElementLocated(qtyInput));

        // Get current quantity
        int currentQty = Integer.parseInt(qtyBox.getAttribute("value"));
        ExtentLogger.info("Current quantity: " + currentQty);

        while (currentQty != targetQty) {

            if (currentQty < targetQty) {
                wait.until(ExpectedConditions.elementToBeClickable(qtyIncreaseBtn)).click();
            }
            else {
                wait.until(ExpectedConditions.elementToBeClickable(qtyDecreaseBtn)).click();
            }

            // Re-read quantity after each click (important!)
            currentQty = Integer.parseInt(
                    wait.until(ExpectedConditions.visibilityOfElementLocated(qtyInput))
                            .getAttribute("value")
            );

            ExtentLogger.info("Updated quantity: " + currentQty);
        }

        Assert.assertEquals(currentQty, targetQty, "Quantity selector did not update correctly!");
    }

    public void clickAddToCart() {
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));

        ExtentLogger.info("Clicking Add to Cart button...");

        try {
            addBtn.click();
        } catch (Exception e) {
            ExtentLogger.fail("Normal click failed -> Using JS click");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
        }

        ExtentLogger.info("Product added to cart successfully!");
    }



}
