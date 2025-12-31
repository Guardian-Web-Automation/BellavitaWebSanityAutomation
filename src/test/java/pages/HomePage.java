package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.ExtentLogger;

import java.util.List;

public class HomePage extends BasePage {
    private final By crazyDealCategoryTextElement = By.xpath("//span[contains(text(),'Crazy Deals')]");
    private final By shopAllCategoryTextElement = By.xpath("//h1[@class='collection-hero__title h2' and contains(normalize-space(.), 'Shop All Products')]");
    private final By bestSellerCategoryTextElement = By.xpath("//h1[@class='collection-hero__title h2' and contains(normalize-space(.), 'Bestsellers')]");
    private final By perfumesCategoryTextElement = By.xpath("//h1[@class='collection-hero__title h2' and contains(normalize-space(.), 'All Perfumes')]");
    private final By bathAndBodyCategoryTextElement = By.xpath("//h1[@class='collection-hero__title h2' and contains(normalize-space(.), 'Bath And Body')]");
    private final By cosmeticsCategoryTextElement = By.xpath("//h1[@class='collection-hero__title h2' and contains(normalize-space(.), 'Cosmetics')]");
    private final By newArrivalsCategoryTextElement = By.xpath("//h1[@class='collection-hero__title h2' and contains(normalize-space(.), 'New Arrivals')]");
    private final By skinCareCategoryTextElement = By.xpath("//h1[@class='collection-hero__title h2' and contains(normalize-space(.), 'Skincare Products')]");
    private final By giftingCategoryTextElement = By.xpath("//h2[contains(text(),'BESTSELLERS')]");
    private final By profileButtonElement = By.xpath("//a[@class='header__icon header__icon--account focus-inset small-hide']");
    private final By cartButtonElement = By.xpath("//details[@class='cart-drawer-container']");
    private final By loginButtonAtProfilePageElement = By.id("login");
    private final By cartTitleOnCartPageElement = By.xpath("//div[@class='title h4' and contains(text(),'Cart')]");
    private final By searchBarElement = By.cssSelector("input.wizzy-search-input:not([style*='display: none'])");
    // Use a flexible selector for product items to match both 'grid_item' and grid__item' variants
    private final By productItems = By.cssSelector("ul.wizzy-search-results-list li[class*='grid'] a.full-unstyled-link");



    public HomePage(WebDriver driver){
        super(driver);
    }


    public String checkTitleOfPage() {
        waitForPageLoad();
        return driver.getTitle();
    }

    public boolean clickCategoryNewWay(String categoryName) {
        try {
            By categoryLocator = By.xpath("//span[@class='label' and contains(text(),'" + categoryName + "')]");
            WebElement categoryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(categoryLocator));
            categoryElement.click();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to click category: " + categoryName, e);
        }
    }

    public boolean clickCategory(String categoryName) {
        try {
             By textElement;

             switch (categoryName.toLowerCase()) {
                 case "crazydeals":
                     textElement = crazyDealCategoryTextElement;
                     break;

                 case "shopall":
                     textElement=shopAllCategoryTextElement;
                     break;

                 case "bestseller":
                     textElement=bestSellerCategoryTextElement;
                     break;

                 case "perfumes":
                     textElement=perfumesCategoryTextElement;
                     break;

                 case "bathandbody":
                     textElement=bathAndBodyCategoryTextElement;
                     break;

                 case "cosmetics":
                     textElement=cosmeticsCategoryTextElement;
                     break;

                 case "newarrivals":
                     textElement=newArrivalsCategoryTextElement;
                     break;

                 case "skincare":
                     textElement=skinCareCategoryTextElement;
                     break;

                 case "gifting":
                     textElement=giftingCategoryTextElement;
                     break;

                 default:
                     throw new IllegalArgumentException("Invalid category: " + categoryName);
             }

           return wait.until(ExpectedConditions.visibilityOfElementLocated(textElement)).isDisplayed();

        } catch (Exception e) {
            throw new RuntimeException("Failed to validate category: " + categoryName, e);
        }
    }


    public void clickProfileButton() {
        safeClick(profileButtonElement);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButtonAtProfilePageElement));
    }

    public void clickCartButton() {
        safeClick(cartButtonElement);
    }

    public boolean verifyCartPage() {
       return wait.until(ExpectedConditions.visibilityOfElementLocated(cartTitleOnCartPageElement)).isDisplayed();
    }

    public void SearchBar(String text){
        // Log the search action here (centralized) and perform the search
        ExtentLogger.info("Searching for: " + text);
        // Use robust typing helper and explicit ENTER
        safeType(searchBarElement, text);
        WebElement searchbarElement = waitForVisibility(searchBarElement);
        searchbarElement.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.urlContains("/search?q="));
    }

    public void clickCategoryNew(String categoryName) {
        // Click subcategory (by visible text)
        WebElement Category = driver.findElement(
                By.xpath("//nav[@class='header__inline-menu']//a//span[contains(text(),'" + categoryName + "')]"));
       wait.until(ExpectedConditions.elementToBeClickable(Category)).click();
    }

    public void clickSubCategory(String categoryName, String subCategoryName) {
        Actions actions = new Actions(driver);

        // Hover over category (by visible text)
        WebElement category = driver.findElement(
                By.xpath("//nav[@class='header__inline-menu']//a//span[contains(text(),'" + categoryName + "')]"));
        actions.moveToElement(category).perform();

        // Click subcategory (by visible text)
        WebElement subCategory = driver.findElement(
                By.xpath("//ul[@class='list-menu']//span[contains(text(),'" + subCategoryName + "')]"));
        wait.until(ExpectedConditions.elementToBeClickable(subCategory)).click();
    }

    public boolean verifySubCatpage(String subCatName){
        WebElement category = driver.findElement(
                By.xpath("//h1[@class='collection-hero__title h2' and contains(normalize-space(.), '" + subCatName + "')]"));
       return wait.until(ExpectedConditions.visibilityOf(category)).isDisplayed();
    }


    public void clickSwipperBanner(int i) {
        WebElement bannerSwipe = driver.findElement(
                By.xpath("//span[@aria-label='Go to slide " + i + "']"));
        wait.until(ExpectedConditions.elementToBeClickable(bannerSwipe)).click();
    }

    public int getProductCount() {
        ExtentLogger.info("Fetching number of products from search result...");

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productItems));
        List<WebElement> list = driver.findElements(productItems);

        int count = list.size();
        ExtentLogger.info("Total products listed: " + count);
        return count;
    }

    public void clickFirstProduct() {

        // retry loop to handle potential StaleElementReferenceException when the results update
        int attempts = 0;
        final int maxAttempts = 3;
        while (attempts < maxAttempts) {
            try {
                List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productItems));
                if (products == null || products.isEmpty()) {
                    throw new RuntimeException("No product found in search results");
                }

                WebElement firstProduct = products.get(0);

                // read href before clicking (may throw StaleElementReferenceException)
                String href = null;
                try {
                    href = firstProduct.getAttribute("href");
                } catch (StaleElementReferenceException ser) {
                    throw ser; // trigger outer retry
                } catch (Exception ignored) {}

                // Scroll into view and click (robustly)
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstProduct);
                try {
                    if (href != null && !href.isEmpty()) {
                        // Navigate directly to href to avoid stale element during click
                        driver.get(href);
                    } else {
                        firstProduct.click();
                    }
                } catch (Exception e) {
                    ExtentLogger.info("Element click failed, using JS click fallback");
                    try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstProduct);
                    } catch (Exception ex) {
                        // final fallback: navigate by href if available
                        if (href != null && !href.isEmpty()) {
                            driver.get(href);
                        } else {
                            throw ex;
                        }
                    }
                }

                if (href != null) ExtentLogger.info("First product clicked successfully: " + href);
                else ExtentLogger.info("First product clicked successfully");

                return; // success
            } catch (StaleElementReferenceException ser) {
                attempts++;
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                if (attempts >= maxAttempts) {
                    ExtentLogger.fail("Failed to click first product due to stale element after retries");
                    throw new RuntimeException("Failed to click first product (stale element)", ser);
                }
            }
        }
    }
    private By categoryTab(String categoryName) {
        return By.xpath("//button[contains(@class,'tab-')]//h2[text()='" + categoryName + "']");
    }
    private By productName(String productName) {
        // Ancestor li that has a class containing 'grid' (covers grid_item and grid__item)
        return By.xpath("//span[contains(text(),'"+productName+"')]/ancestor::li[contains(@class,'grid')]");
    }
    private By addToCartInsideProduct(String productName) {
        // Locate add-to-cart area inside a product list item; rely on ancestor li having 'grid' in class
        return By.xpath("//span[contains(., '"+productName+"')]/ancestor::li[contains(@class,'grid')]//div[contains(@class,'add-to-cart') or contains(@class,'product__actions') or contains(@class,'product-actions') or div]");
    }

    public void scrollToCategory(String categoryName){
        WebElement cat = wait.until(ExpectedConditions.visibilityOfElementLocated(categoryTab(categoryName)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cat);
    }
    public void clickHomeCategory(String categoryName){
        safeClick(categoryTab(categoryName));
    }
    public boolean isProductDisplayed(String productName){
        return !driver.findElements(productName(productName)).isEmpty();
    }
    public void clickAddToCart(String productName){
        safeClick(addToCartInsideProduct(productName));
    }

    // Simple method: click the 'View all' link for a category using the HTML pattern from your screenshot.
    // This looks for an anchor inside a div.view-all and clicks the first visible one.
    public void clickViewAllForCategory(String categoryName,String categoryId) {
        WebElement cat = wait.until(ExpectedConditions.visibilityOfElementLocated(categoryTab(categoryName)));
        cat.click();
        // We intentionally keep this simple: find the anchor inside .view-all and click it.
        By viewAllLocator = By.xpath("//div[@data-id='"+categoryId+"']/div/a");
        ExtentLogger.info("Clicking View All for category: " + categoryName);
        WebElement viewAll = wait.until(ExpectedConditions.elementToBeClickable(viewAllLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewAll);
        viewAll.click();
    }

    // Simple PLP product count: prefer the product-grid ul (id=product-grid) then fallback to generic li under it
    public int getPLPProductCountSimple() {
        try {
            // Primary: count direct li children of the product-grid ul
            By productGridItems = By.cssSelector("#product-grid > li");
            List<WebElement> items = driver.findElements(productGridItems);
            if (items != null && !items.isEmpty()) {
                // keep this method quiet (no info log) to avoid duplicate output
                return items.size();
            }

            // Fallback: any li under an element with id product-grid
            By fallback1 = By.cssSelector("#product-grid li");
            items = driver.findElements(fallback1);
            if (items != null && !items.isEmpty()) {
                return items.size();
            }

            // Generic fallback: any li whose class contains 'grid'
            By fallback2 = By.cssSelector("li[class*='grid']");
            items = driver.findElements(fallback2);
            return items == null ? 0 : items.size();
        } catch (Exception e) {
            ExtentLogger.info("Error counting PLP products: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Try to load products until we see at least expectedCount items.
     * - Repeatedly scrolls the last visible item to trigger lazy-load.
     * - Attempts to click any obvious "load more" controls.
     * - If scrolling doesn't reach the expected count, falls back to simple pagination (?page=N) accumulation.
     * Returns the number of items loaded/accumulated.
     */
    public int loadAllProductsUntil(int expectedCount) {
        By productItems = By.xpath("//ul[@id='product-grid']//li");
        int previousCount = 0;
        int attempts = 0;
        int maxAttempts = 10;
        while (attempts < maxAttempts) {
            List<WebElement> products = driver.findElements(productItems);
            int currentCount = products.size();
            if (currentCount >= expectedCount) {
                return currentCount;
            }
            if (currentCount == previousCount) {
                attempts++;
            } else {
                attempts = 0;
                previousCount = currentCount;
            }
            if (currentCount > 0) {
                WebElement lastProduct = products.get(currentCount - 1);
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView(true);", lastProduct);
            }
            try {
                Thread.sleep(800);
            } catch (InterruptedException ignored) {}
        }
        return previousCount;
    }



    // Read the product count from the PLP header (id ProductCountDesktop or .product-count__text)
    public int getProductCountFromHeader() {
        try {
            By headerById = By.id("ProductCountDesktop");
            WebElement el = driver.findElement(headerById);
            String text = el.getText().trim(); // e.g. "35 products"
            String digits = text.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) return 0;
            return Integer.parseInt(digits);
        } catch (Exception e) {
            ExtentLogger.info("Unable to read product count from header: " + e.getMessage());
            return 0;
        }
    }

}
