package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.PDPPage;
import pages.CartPage;
import pages.PLPPage;
import utils.ExtentLogger;
import utils.TestListener;


@Listeners(TestListener.class)
public class HomePageTests extends BaseTest {

    @Test(priority = 0,enabled = true)
    public void titleValidationtest(){
        HomePage Hp = new HomePage(driver);
        String pageTitle= Hp.checkTitleOfPage();
        Assert.assertTrue(pageTitle.contains("Buy Luxury Perfume for Men and Women"),
                "Expected page title is not correct:"+pageTitle);
    }

    @Test(priority = 1,enabled = true)
    public void searchAnyKeyWordTest(){
        HomePage Hp = new HomePage(driver);
        Hp.SearchBar("men perfume");
    }

    @Test(dataProvider = "CategoryData", dataProviderClass = data.TestData.class,priority = 2,enabled = true)
    public void ClickCategoryTest(String category, String categoryTitle){
        ExtentLogger.info("Starting subCategoryClickTest for Category: " + category);
        HomePage Hp = new HomePage(driver);
        Hp.clickCategoryNew(category);
        boolean isFound = Hp.clickCategory(categoryTitle);
        Assert.assertTrue(isFound, "The category '" + category + "' is not clicked");
    }

    @Test(dataProvider = "PerfumesSubCategoryData", dataProviderClass = data.TestData.class,priority = 3,enabled = true)
    public void PerfumeSubCategoriesTest(String category, String subCategory, String subCategoryTitle){
        ExtentLogger.info("Starting PerfumeSubCategoriesTest for Category: " + category +
                ", SubCategory: " + subCategory);
        HomePage Hp = new HomePage(driver);
        Hp.clickSubCategory(category, subCategory);
        boolean isFound = Hp.verifySubCatpage(subCategoryTitle);
        Assert.assertTrue(isFound, "The Sub category '" + subCategory + "' is not clicked");
    }

    @Test(dataProvider = "BathAndBodySubCategoryData", dataProviderClass = data.TestData.class,priority = 4,enabled = true)
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

    @Test(dataProvider = "CosmeticsSubCategoryData", dataProviderClass = data.TestData.class,priority = 6,enabled = true)
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

    @Test(priority = 8,enabled = true)
    public void cartPageTest(){
        ExtentLogger.info("The test case has started: cartPageTest" );
        HomePage Hp = new HomePage(driver);
        Hp.clickCartButton();
        boolean cartTitleText= Hp.verifyCartPage();
        Assert.assertTrue(cartTitleText,"Cart title is not visible i.e cart is not opened");
    }

    @Test(priority = 9,enabled = false)
    public void SwipperBanner(){
        ExtentLogger.info("Starting the test case: addToCartFromBestSeller" );
        HomePage Hp = new HomePage(driver);
        Hp.clickSwipperBanner(2);
    }

    @Test(dataProvider = "ProductAndQuantityAddToCart", dataProviderClass = data.TestData.class,priority = 8,enabled = true)
    public void UserEndToEndFlow(String productName, int expectedQty){
        ExtentLogger.info("===== Test Started: User End-to-End Flow for Product: " + productName + " =====");
        HomePage Hp = new HomePage(driver);
        Hp.SearchBar(productName);
        int productCount = Hp.getProductCount();
        Assert.assertTrue(productCount > 0, "❌ No products listed for the search keyword!");
        ExtentLogger.info("Opening first product from search listing...");
        Hp.clickFirstProduct();
        PDPPage pp = new PDPPage(driver);
        pp.setQuantity(expectedQty);  // dynamic quantity change
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
//        String freeItem = cp.getFreeItemName();
//        if (freeItem != null) {
//            ExtentLogger.info("Free Item in Cart: " + freeItem);
//        } else {
//            ExtentLogger.fail("No free item added — cart threshold not reached.");
//        }
        String amount = cp.getOrderAmount();
        ExtentLogger.info("Final Order Amount Before Checkout: " + amount);
        cp.clickCheckout();
        ExtentLogger.info("User navigated to Checkout page.");
        ExtentLogger.info("===== Test Passed: Product Added With Correct Quantity =====");
    }

    @Test(priority = 11, dataProvider = "CategoryAndProduct", dataProviderClass = data.TestData.class, enabled = true)
    public void AddToCartFromHomePageBestsellerNewArrivalCategory(String categoryName, String productName) {
        HomePage hp = new HomePage(driver);
        ExtentLogger.info("Scrolling to category: " + categoryName);
        hp.scrollToCategory(categoryName);
        hp.clickHomeCategory(categoryName);
        Assert.assertTrue(hp.isProductDisplayed(productName), "❌ Product not found in " + categoryName + " section!");
        ExtentLogger.info("Adding product: " + productName + " to cart...");
        hp.clickAddToCart(productName);
        PDPPage pp = new PDPPage(driver);
        pp.clickAddToCart();
        ExtentLogger.info("Validating product in cart...");
        CartPage cp = new CartPage(driver);
        String cartProduct = cp.getNormalItemName();
        Assert.assertEquals(cartProduct, productName, "❌ Product not added correctly to Cart!");
        ExtentLogger.pass("✔ Successfully added product to cart from: " + categoryName);
    }

    @Test(priority = 12, enabled = true)
    public void bestSellerViewAllTest() {
        HomePage hp = new HomePage(driver);
        // Scroll to the BESTSELLERS category on the homepage
        hp.scrollToCategory("BESTSELLERS");
        // Click the View All link/button for the BESTSELLERS section (uses simple XPath targeting div.view-all anchor)
        hp.clickViewAllForCategory("BESTSELLERS","tab1");
        // Verify the Bestsellers collection page header is visible
        boolean pageOpened = hp.verifySubCatpage("Bestsellers");
        Assert.assertTrue(pageOpened, "Bestsellers collection page did not open as expected");
        ExtentLogger.info("Bestsellers page opened successfully. Now validating product count on PLP...");
        // Read product count from the PLP header (e.g. '35 products')
        int headerCount = hp.getProductCountFromHeader()-1;
        ExtentLogger.info("Product count from header: " + headerCount);
        Assert.assertTrue(headerCount > 0, "Header product count should be greater than zero");
        // Try to load all products (lazy load / pagination). Method returns number of items loaded/accumulated.
        int loadedCount = hp.loadAllProductsUntil(headerCount);
        // Read product count from the PLP grid/listing as a cross-check (items visible on this page)
        int gridCount = hp.getPLPProductCountSimple();
        ExtentLogger.info("Product count from PLP grid (visible items on this page): " + gridCount);
        PLPPage plp = new PLPPage(driver);
        int soldoutProductCount= plp.printSoldOutProducts();
        ExtentLogger.info("Total Sold Out Products in BestSeller Category: " + soldoutProductCount);
        // Pass if either the visible grid equals header OR loader accumulated at least the header number (pagination case)
        Assert.assertTrue(gridCount == headerCount || loadedCount >= headerCount, String.format("❌ Product count " +
                "mismatch | Header=%d | Grid=%d | Loaded=%d", headerCount, gridCount, loadedCount)
        );
        ExtentLogger.pass(String.format("✅ Product count validated | Header=%d | Grid=%d | Loaded=%d", headerCount, gridCount, loadedCount));
    }

    @Test(priority = 13, enabled = true)
    public void newArrivalViewAllTest() {
        HomePage hp = new HomePage(driver);
        // Scroll to the BESTSELLERS category on the homepage
        hp.scrollToCategory("NEW ARRIVALS");
        // Click the View All link/button for the BESTSELLERS section (uses simple XPath targeting div.view-all anchor)
        hp.clickViewAllForCategory("NEW ARRIVALS","tab2");
        boolean pageOpened = hp.verifySubCatpage("New Arrivals");
        Assert.assertTrue(pageOpened, "New Arrivals collection page did not open as expected");
        ExtentLogger.info("New Arrivals page opened successfully. Now validating product count on PLP...");
        // Read product count from the PLP header (e.g. '216 products')
        int headerCount = hp.getProductCountFromHeader()-1;
        ExtentLogger.info("Product count from header: " + headerCount);
        Assert.assertTrue(headerCount > 0, "Header product count should be greater than zero");
        // Try to load all products (lazy load / pagination). Method returns number of items loaded/accumulated.
        int loadedCount = hp.loadAllProductsUntil(headerCount);
        // Read product count from the PLP grid/listing as a cross-check (items visible on this page)
        int gridCount = hp.getPLPProductCountSimple();
        ExtentLogger.info("Product count from PLP grid (visible items on this page): " + gridCount);
        // Pass if either the visible grid equals header OR loader accumulated at least the header number (pagination case)
        PLPPage plp = new PLPPage(driver);
        int soldoutProductCount= plp.printSoldOutProducts();
        ExtentLogger.info("Total Sold Out Products in New Arrival Category: " + soldoutProductCount);
        Assert.assertTrue(gridCount == headerCount || loadedCount >= headerCount, String.format("❌ Product count " +
                "mismatch | Header=%d | Grid=%d | Loaded=%d", headerCount, gridCount, loadedCount)
        );
        ExtentLogger.pass(String.format("✅ Product count validated | Header=%d | Grid=%d | Loaded=%d", headerCount, gridCount, loadedCount));
    }



}
