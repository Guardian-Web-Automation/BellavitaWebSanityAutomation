package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.ExtentLogger;

import java.util.List;

public class CartPage extends BasePage {
    // FREE item title
    private By freeItemTitle = By.cssSelector(".tier-product-info .link.product-title");
    private By normalCartItems = By.cssSelector("cart-items ul li .link.product-title");
    private By quantityInput = By.cssSelector("input.quantity__input");
    private By checkoutButton = By.cssSelector("button.button[onclick*='preCheckoutCheck']");
    private By orderTotalAmount = By.cssSelector("#mini-cart-subtotal .money");





    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getFreeItemName() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(freeItemTitle));
            return driver.findElement(freeItemTitle).getText().trim();
        } catch (Exception e) {
            return null; // No free item added
        }
    }


    public String getNormalItemName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(normalCartItems));
        List<WebElement> items = driver.findElements(normalCartItems);

        // The first normal item is your actual purchased product
        return items.get(0).getText().trim();
    }


    public int getNormalItemQuantity() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInput));
        String qty = driver.findElement(quantityInput).getAttribute("value");
        return Integer.parseInt(qty);
    }

    public String getOrderAmount() {
        ExtentLogger.info("Fetching order amount from cart...");
        WebElement amountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(orderTotalAmount));
        String amountText = amountElement.getText().trim();
        ExtentLogger.info("Order Amount: " + amountText);
        return amountText;
    }

    public void clickCheckout() {
        ExtentLogger.info("Clicking on the Checkout button...");
        WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
        checkoutBtn.click();
        ExtentLogger.info("Checkout button clicked successfully!");
    }





}