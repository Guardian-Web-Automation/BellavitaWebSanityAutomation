package tests;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import utils.ExtentLogger;
import utils.TestListener;
@Listeners(TestListener.class)
public class CrazyDealsTests extends BaseTest{
    @Test(description = "Verify Crazy Deals Product Buy 3 for 1298 Flow",dataProvider = "crazyDealName", dataProviderClass = data.TestData.class)
    public void testCrazDealsUltimatePerfumeBox(String actualProductName ) {
        CrazyDealPage cdp = new CrazyDealPage(driver);
        HomePage hp = new HomePage(driver);
        hp.clickCategoryNewWay("Crazy Deals");
        cdp.scrollToCrazyDealAndClick(actualProductName);
        cdp.addAnyThreeAvailableProductsToBox();
        cdp.CrazyDealBuyNowButtonClick();
        String expectedProductName=cdp.verifyProductNameInGoKwikCheckout();
        // 3️⃣ Assertion
        Assert.assertEquals(actualProductName, expectedProductName, "❌ Product name mismatch in GoKwik checkout");
        ExtentLogger.pass("✅ Product name verified successfully: " + actualProductName+" Crazy Deals Purchase Flow Test completed successfully");

    }
}
