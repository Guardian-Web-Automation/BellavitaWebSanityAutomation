package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ExtentLogger;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class CartPage extends BasePage {
    // FREE item title
    private final By freeItemTitle = By.xpath("//*[contains(@class,'tier-product-info')]//a[contains(@class,'product-title')]");
    // normal items under <cart-items> (fallback) — use XPath
    private final By normalCartItems = By.xpath("//cart-items//a[contains(@class,'product-title')]");
    // Robust XPath matching the provided DOM: ul.mini-cart__navigation -> li -> a.link.product-title
    private final By cartProductTitles = By.xpath("//div[@id='main-cart-items']//div[@class='product-container']//a[@class='link product-title']");
    private final By quantityInput = By.xpath("//input[contains(@class,'quantity__input')]");
    private final By checkoutButton = By.xpath("//button[@onclick='onCheckoutClick(this)']");
    private final By orderTotalAmount = By.xpath("//span[@id='mini-cart-subtotal']//*[contains(@class,'money')]");





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

    /**
     * Ensure mini-cart drawer is open. If not open, click the cart drawer trigger.
     */
    public void openCartIfClosed() {
        try {
            // if the main cart items container is visible, we assume cart is open
            List<WebElement> main = driver.findElements(By.xpath("//div[@id='main-cart-items']"));
            if (!main.isEmpty() && main.get(0).isDisplayed()) return;
        } catch (Exception ignored) {}

        // Click the cart drawer trigger (details element)
        try {
            By cartTrigger = By.xpath("//details[contains(@class,'cart-drawer-container')]");
            WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(cartTrigger));
            trigger.click();
            // wait for main cart items to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='main-cart-items']")));
        } catch (Exception e) {
            ExtentLogger.info("Unable to open cart drawer: " + e.getMessage());
        }
    }

    /**
     * Remove the first cart item whose title contains the provided partialName (case-insensitive).
     * Locates the product anchor, finds its ancestor <li>, then clicks the remove control.
     */
    public void removeItemByName(String partialName) {
        openCartIfClosed();

        int attempts = 0;
        final int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartProductTitles));
                List<WebElement> titles = driver.findElements(cartProductTitles);

                for (WebElement title : titles) {
                    String text = title.getText().trim();
                    if (text.toLowerCase().contains(partialName.toLowerCase())) {
                        WebElement li = title.findElement(By.xpath("ancestor::li"));
                        // try custom remove control then fallback
                        try {
                            WebElement removeControl = li.findElement(By.xpath(".//cart-remove-button | .//a[contains(@class,'delete-product')]") );
                            wait.until(ExpectedConditions.elementToBeClickable(removeControl)).click();
                        } catch (Exception e1) {
                            WebElement removeBtn = li.findElement(By.xpath(".//button[contains(@class,'remove') or contains(@class,'cart-remove') or @aria-label='Remove']"));
                            wait.until(ExpectedConditions.elementToBeClickable(removeBtn)).click();
                        }

                        By liLocator = By.xpath("//div[@id='main-cart-items']//li[.//a[contains(normalize-space(.), '" + text + "')]]");
                        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(liLocator)); } catch (Exception ignore) {}
                        return;
                    }
                }

                // if not found, break and throw
                break;
            } catch (StaleElementReferenceException ser) {
                attempts++;
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("Failed to remove item due to stale elements", ser);
                }
            }
        }

        throw new RuntimeException("No cart item matched name: " + partialName);
    }

    public void removeItemFromCart(int itemNumber) {
        openCartIfClosed();
        String xpath = "(//cart-items//li)[" + itemNumber + "]//cart-remove-button//a";

        try {
            ExtentLogger.info("Attempting to remove item number: " + itemNumber);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))
            );

            removeBtn.click();

            // Optional: wait for item count to reduce / loader to disappear
            wait.until(ExpectedConditions.invisibilityOf(removeBtn));
            ExtentLogger.info("Item number " + itemNumber + " removed from cart successfully.");

        } catch (TimeoutException e) {
            ExtentLogger.fail("Timeout: Remove button not clickable for item number: " + itemNumber);
            throw new RuntimeException("Remove button not clickable for item: " + itemNumber, e);

        } catch (NoSuchElementException e) {
            ExtentLogger.fail("No such item found in cart at position: " + itemNumber);
            throw new RuntimeException("Cart item not found at index: " + itemNumber, e);

        } catch (Exception e) {
            ExtentLogger.fail("Unexpected error while removing item number: " + itemNumber
                    + " | Error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Read all product names with retries to handle stale elements.
     */
    public List<String> getAllProductNames() {
        openCartIfClosed();
        int attempts = 0;
        final int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartProductTitles));
                List<WebElement> titleElements = driver.findElements(cartProductTitles);
                List<String> names = new ArrayList<>();
                for (WebElement t : titleElements) {
                    names.add(t.getText().trim());
                }
                return names;
            } catch (StaleElementReferenceException ser) {
                attempts++;
                try { Thread.sleep(250); } catch (InterruptedException ignored) {}
            }
        }
        return new ArrayList<>();
    }

    /**
     * Read quantities robustly with retries.
     */
    public List<Integer> getAllNormalItemQuantities() {
        openCartIfClosed();
        int attempts = 0;
        final int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(quantityInput));
                List<WebElement> qtyElements = driver.findElements(quantityInput);
                List<Integer> quantities = new ArrayList<>();
                for (WebElement q : qtyElements) {
                    try { quantities.add(Integer.parseInt(q.getAttribute("value"))); } catch (Exception e) { quantities.add(0); }
                }
                return quantities;
            } catch (StaleElementReferenceException ser) {
                attempts++;
                try { Thread.sleep(250); } catch (InterruptedException ignored) {}
            }
        }
        return new ArrayList<>();
    }

    /**
     * Returns true if the cart contains no product entries. This method:
     *  - ensures the mini-cart is open,
     *  - looks for known product title selectors (normal/free),
     *  - falls back to checking an "empty cart" message if present,
     *  - is resilient to stale elements.
     */
    public boolean isCartEmpty() {
        openCartIfClosed();
        By emptyMsg = By.xpath("//p[contains(text(),'Your cart is currently empty')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(emptyMsg));

        // Try multiple times to avoid transient stale element issues
        int attempts = 0;
        final int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                // If any product title elements are present and displayed -> not empty
                List<WebElement> titles = driver.findElements(cartProductTitles);
                if (!titles.isEmpty()) {
                    for (WebElement t : titles) {
                        if (t.isDisplayed() && !t.getText().trim().isEmpty()) {
                            return false;
                        }
                    }
                }

                // Check normal cart items (fallback)
                List<WebElement> normal = driver.findElements(normalCartItems);
                if (!normal.isEmpty()) {
                    for (WebElement n : normal) {
                        if (n.isDisplayed() && !n.getText().trim().isEmpty()) return false;
                    }
                }

                // Check free item
                List<WebElement> free = driver.findElements(freeItemTitle);
                if (!free.isEmpty()) {
                    for (WebElement f : free) {
                        if (f.isDisplayed() && !f.getText().trim().isEmpty()) return false;
                    }
                }

                // Fallback: look for common empty-cart text patterns inside the cart container
                try {
                    List<WebElement> empties = driver.findElements(emptyMsg);
                    if (!empties.isEmpty()) return true;
                } catch (Exception ignored) {}

                // If none of the above found any products, treat as empty
                return true;
            } catch (StaleElementReferenceException ser) {
                attempts++;
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            }
        }

        // As a conservative default, say empty if we couldn't confidently find items
        return true;
    }

 }
