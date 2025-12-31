package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.PDPPage;
import utils.ExtentLogger;
import utils.TestListener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Listeners(TestListener.class)
public class CartTests extends BaseTest {

    @Test(priority = 10)
    public void addMultipleItemsToCart() {

        HomePage home = new HomePage(driver);

        // Define products to add: search keyword -> {expectedNameInCart, quantity}
        // Adjust expected names to match the site if needed.
        Map<String, Object[]> productsToAdd = new LinkedHashMap<>();
        productsToAdd.put("CEO Man Perfume", new Object[] {"CEO Man Perfume - 100ml", 2});
        productsToAdd.put("WHITE Oud", new Object[] {"WHITE Oud Unisex Perfume", 1});

        // Add each product via search -> open first product -> set qty -> add to cart
        for (Map.Entry<String, Object[]> entry : productsToAdd.entrySet()) {
            String searchKeyword = entry.getKey();
            String expectedCartName = (String) entry.getValue()[0];
            int qty = (int) entry.getValue()[1];

            // Search and open product (SearchBar logs the search action)
            home.SearchBar(searchKeyword);
            ExtentLogger.info("Opening first product from search results");
            home.clickFirstProduct();

            PDPPage pdp = new PDPPage(driver);
            pdp.setQuantity(qty);
            pdp.clickAddToCart();


            ExtentLogger.info("Added to cart: " + expectedCartName + " x" + qty);

            // After adding, navigate back to home page to add next product (site specific)
            // simple way: click site logo or navigate to base URL
            driver.get("https://bellavitaorganic.com/");
        }

        // Open cart drawer/page and verify items
        home.clickCartButton();
        CartPage cart = new CartPage(driver);

        List<String> cartNames = cart.getAllProductNames();
        List<Integer> cartQtys = cart.getAllNormalItemQuantities();

        ExtentLogger.info("Cart contains items: " + cartNames);
        ExtentLogger.info("Cart quantities: " + cartQtys);

        // Verify number of distinct items
        Assert.assertTrue(cartNames.size() >= productsToAdd.size(), "Not all items are present in cart");

        for (Object[] expected : productsToAdd.values()) {
            String expectedName = (String) expected[0];
            int expectedQty = (int) expected[1];

            boolean matched = false;
            for (int j = 0; j < cartNames.size(); j++) {
                if (cartNames.get(j).contains(expectedName) || expectedName.contains(cartNames.get(j))) {
                    matched = true;
                    // check corresponding quantity if available
                    if (j < cartQtys.size()) {
                        Assert.assertEquals(cartQtys.get(j).intValue(), expectedQty,
                                "Quantity mismatch for item: " + expectedName);
                    }
                    break;
                }
            }
            Assert.assertTrue(matched, "Expected item not found in cart: " + expectedName);
        }

        ExtentLogger.pass("Successfully verified multiple items in cart");
    }

    @Test(priority = 20)
    public void addThenRemoveItemAndVerifyCartEmpty() {

        HomePage home = new HomePage(driver);

        // 1) search the product
        String searchKeyword = "CEO"; // change as needed
        // use HomePage.SearchBar which already logs the search action
        home.SearchBar(searchKeyword);

        // 2) add that item into the cart
        ExtentLogger.info("Opening first product from search results");
        home.clickFirstProduct();

        PDPPage pdp = new PDPPage(driver);
        pdp.setQuantity(1);
        pdp.clickAddToCart();

        // 3) remove item from the cart
//        ExtentLogger.info("Opening cart drawer");
//        home.clickCartButton();

        CartPage cart = new CartPage(driver);
        ExtentLogger.info("Removing product by name: " + searchKeyword);
        cart.removeItemFromCart(1);

        // 4) verify that the cart is empty
        boolean empty = cart.isCartEmpty();
        ExtentLogger.info("Cart empty status: " + empty);
        Assert.assertTrue(empty, "Cart should be empty after removing the item");

        ExtentLogger.pass("addThenRemoveItemAndVerifyCartEmpty completed successfully");
    }

    @Test(priority = 30)
    public void addTwoItemsRemoveOneAndVerifyRemaining() {

        HomePage home = new HomePage(driver);

        // Product A
        String productASearch = "CEO";

        // Product B
        String productBSearch = "WHITE Oud";
        String productAExpected="CEO Man Perfume";
        String productBExpected = "WHITE Oud Unisex Perfume - 20ml";

        // Add Product A
        // Add Product A (SearchBar logs search)
        home.SearchBar(productASearch);
        home.clickFirstProduct();
        PDPPage pdp = new PDPPage(driver);
        pdp.setQuantity(1);
        pdp.clickAddToCart();

        // navigate back to home to add next product
        driver.get("https://bellavitaorganic.com/");

        // Add Product B
        // Add Product B (SearchBar logs search)
        home.SearchBar(productBSearch);
        home.clickFirstProduct();
        pdp = new PDPPage(driver);
        pdp.setQuantity(1);
        pdp.clickAddToCart();

        // Open cart and remove only Product A
        ExtentLogger.info("Opening cart and removing product A: " + productASearch);
//        home.clickCartButton();
        CartPage cart = new CartPage(driver);
        cart.removeItemFromCart(1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verify only Product B remains
        List<String> names = cart.getAllProductNames();
        ExtentLogger.info("Cart items after removal: " + names);

        Assert.assertEquals(names.size(), 1, "Cart should contain exactly one item after removing one");
        Assert.assertTrue(names.get(0).contains(productAExpected) || productAExpected.contains(names.get(0)),
                "Remaining cart item should be Product B: " + productAExpected);
    }

}
