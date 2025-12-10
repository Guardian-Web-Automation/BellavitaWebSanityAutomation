package tests;

import base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.PDPPage;
import pages.CartPage;
import utils.ExtentLogger;
import utils.TestListener;


@Listeners(TestListener.class)
public class HomePageTests extends BaseTest {
    private static final Logger LOGGER = LogManager.getLogger(HomePageTests.class);

    @Test(priority = 0,enabled = false)
    public void titleValidationtest(){
        ExtentLogger.info("Starting first Test case: titleValidationtest" );
        HomePage Hp = new HomePage(driver);
        String pageTitle= Hp.checkTitleOfPage();
        Assert.assertTrue(pageTitle.contains("Buy Luxury Perfume for Men and Women"),"Expected page title is not correct:"+pageTitle);
    }

    @Test(priority = 1,enabled = false)
    public void searchAnyKeyWordTest(){
        ExtentLogger.info("Starting first Test case: searchAnyKeyWordTest" );
        HomePage Hp = new HomePage(driver);
        Hp.SearchBar("men perfume");
    }

    @Test(dataProvider = "CategoryData", dataProviderClass = data.TestData.class,priority = 2,enabled = false)
    public void ClickCategoryTest(String category, String categoryTitle){
        ExtentLogger.info("Starting subCategoryClickTest for Category: " + category);
        HomePage Hp = new HomePage(driver);
        Hp.clickCategoryNew(category);
        boolean isFound = Hp.clickCategory(categoryTitle);
        Assert.assertTrue(isFound, "The category '" + category + "' is not clicked");
    }

    @Test(dataProvider = "PerfumesSubCategoryData", dataProviderClass = data.TestData.class,priority = 3,enabled = false)
    public void PerfumeSubCategoriesTest(String category, String subCategory, String subCategoryTitle){
        ExtentLogger.info("Starting PerfumeSubCategoriesTest for Category: " + category +
                ", SubCategory: " + subCategory);
        HomePage Hp = new HomePage(driver);
        Hp.clickSubCategory(category, subCategory);
        boolean isFound = Hp.verifySubCatpage(subCategoryTitle);
        Assert.assertTrue(isFound, "The Sub category '" + subCategory + "' is not clicked");
    }

    @Test(dataProvider = "BathAndBodySubCategoryData", dataProviderClass = data.TestData.class,priority = 4,enabled = false)
    public void BathAndBodySubCategoriesTest(String category, String subCategory, String subCategoryTitle){
        ExtentLogger.info("Starting BathAndBodySubCategoriesTest for Category: " + category +
                ", SubCategory: " + subCategory);
        HomePage Hp = new HomePage(driver);
        Hp.clickSubCategory(category, subCategory);
        boolean isFound = Hp.verifySubCatpage(subCategoryTitle);
        Assert.assertTrue(isFound, "The Sub category '" + subCategory + "' is not clicked");
    }

    @Test(dataProvider = "SkincareSubCategoryData", dataProviderClass = data.TestData.class,priority = 5,enabled = false)
    public void SkincareSubCategoriesTest(String category, String subCategory, String subCategoryTitle){
        ExtentLogger.info("Starting SkincareSubCategoriesTest for Category: " + category +
                ", SubCategory: " + subCategory);
        HomePage Hp = new HomePage(driver);
        Hp.clickSubCategory(category, subCategory);
        boolean isFound = Hp.verifySubCatpage(subCategoryTitle);
        Assert.assertTrue(isFound, "The Sub category '" + subCategory + "' is not clicked");
    }

    @Test(dataProvider = "CosmeticsSubCategoryData", dataProviderClass = data.TestData.class,priority = 6,enabled = false)
    public void CosmeticsSubCategoriesTest(String category, String subCategory, String subCategoryTitle){
        ExtentLogger.info("Starting CosmeticsSubCategoriesTest for Category: " + category +
                ", SubCategory: " + subCategory);
        HomePage Hp = new HomePage(driver);
        Hp.clickSubCategory(category, subCategory);
        boolean isFound = Hp.verifySubCatpage(subCategoryTitle);
        Assert.assertTrue(isFound, "The Sub category '" + subCategory + "' is not clicked");
    }

    @Test(priority = 7, enabled = false)
    public void profilePageTest(){
        ExtentLogger.info("The test case is starting: profilePageTest" );
        HomePage Hp = new HomePage(driver);
        Hp.clickProfileButton();
        String pageURL=driver.getCurrentUrl();
        Assert.assertTrue(pageURL.contains("login"),"The url is not matched with the page:"+pageURL);
    }

    @Test(priority = 8,enabled = false)
    public void cartPageTest(){
        ExtentLogger.info("The test case has started: cartPageTest" );
        HomePage Hp = new HomePage(driver);
        Hp.clickCartButton();
        boolean cartTitleText= Hp.verifyCartPage();
        Assert.assertTrue(cartTitleText,"Cart title is not visible i.e cart is not opened");
    }

    @Test(priority = 8,enabled = false)
    public void SwipperBanner(){
        ExtentLogger.info("Starting the test case: addToCartFromBestSeller" );
        HomePage Hp = new HomePage(driver);
        Hp.clickSwipperBanner(2);
    }

    @Test(dataProvider = "ProductAndQuantityAddToCart", dataProviderClass = data.TestData.class,priority = 8,enabled = true)
    public void UserEndToEndFlow(String productName, int expectedQty){
        ExtentLogger.info("===== Test Started: User End-to-End Flow for Product: " + productName + " =====");
        HomePage Hp = new HomePage(driver);
        ExtentLogger.info("Searching for "+productName+"...");
        Hp.SearchBar(productName);

        int productCount = Hp.getProductCount();
        ExtentLogger.info("Products found for the search keyword: " + productCount);
        Assert.assertTrue(productCount > 0, "❌ No products listed for the search keyword!");

        ExtentLogger.info("Opening first product from search listing...");
        Hp.clickFirstProduct();
        PDPPage pp = new PDPPage(driver);
        pp.setQuantity(expectedQty);  // dynamic quantity change
        ExtentLogger.info("Selected Quantity: " + expectedQty);
        pp.clickAddToCart();

        ExtentLogger.info("Validating if product moved to cart drawer...");
        CartPage cp = new CartPage(driver);

        // ASSERT NORMAL PRODUCT
        String normalProduct = cp.getNormalItemName();
        ExtentLogger.info("Normal Item in Cart: " + normalProduct);
        Assert.assertTrue(normalProduct.contains(productName), "Normal product is incorrect!");

        int actualQty = cp.getNormalItemQuantity();
        ExtentLogger.info("Normal Item Quantity: " + actualQty);

        Assert.assertEquals(actualQty, expectedQty, "Quantity mismatch for main product!");

        String freeItem = cp.getFreeItemName();
        if (freeItem != null) {
            ExtentLogger.info("Free Item in Cart: " + freeItem);
        } else {
            ExtentLogger.fail("No free item added — cart threshold not reached.");
        }
        String amount = cp.getOrderAmount();
        ExtentLogger.info("Final Order Amount Before Checkout: " + amount);

        cp.clickCheckout();
        ExtentLogger.info("User navigated to Checkout page.");

        ExtentLogger.info("===== Test Passed: Product Added With Correct Quantity =====");
    }


}
