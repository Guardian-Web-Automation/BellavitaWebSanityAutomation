package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.ExtentLogger;

import java.util.List;

public class HomePage extends BasePage {
    private final By crazyDealCategoryTextElement = By.xpath("//h2[contains(text(),'Crazy Deals')]");
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
    private final By productItems = By.cssSelector("ul.wizzy-search-results-list li.grid__item a.full-unstyled-link");



    public HomePage(WebDriver driver){
        super(driver);
    }


    public String checkTitleOfPage() {
        waitForPageLoad();
        String pageTitle= driver.getTitle();
        return pageTitle;
    }

    public boolean clickCategory(String categoryName) {
        try {
            By buttonElement;
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
        click(profileButtonElement);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButtonAtProfilePageElement));
    }

    public void clickCartButton() {
        click(cartButtonElement);
    }

    public boolean verifyCartPage() {
       return wait.until(ExpectedConditions.visibilityOfElementLocated(cartTitleOnCartPageElement)).isDisplayed();
    }

    public void SearchBar(String text){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement searchbarElement= wait.until(ExpectedConditions.elementToBeClickable(searchBarElement));
      searchbarElement.clear();
      searchbarElement.sendKeys(text);
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
//        WebElement banner = driver.findElement(
//                By.xpath("//div[@data-swiper-slide-index='" + (i - 1) + "']//a"));
//        wait.until(ExpectedConditions.elementToBeClickable(banner)).click();
//        String pageUrl= banner.getAttribute("href");
//        System.out.println("The banner url is :"+pageUrl);
//        waitForPageLoad();
//        String currentUrl=driver.getCurrentUrl();
//        System.out.println("The current url is :"+currentUrl);
//        Assert.assertTrue(currentUrl.contains(pageUrl),"The url is not matched with the page:"+currentUrl);
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
        ExtentLogger.info("Waiting for search results to load...");

        List<WebElement> products = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.cssSelector("ul.wizzy-search-results-list li.grid__item a.full-unstyled-link")
                )
        );

        if (products.isEmpty()) {
            ExtentLogger.fail("No products found for search keyword!");
            Assert.fail("No products found!");
        }

        WebElement firstProduct = products.get(0);

        // Scroll into view before clicking
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstProduct);

        ExtentLogger.info("Clicking first product using JS Click: " + firstProduct.getAttribute("href"));

        // JS Click — avoids visual overlays blocking click
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstProduct);

        ExtentLogger.info("First product clicked successfully!");
    }

}
