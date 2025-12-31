package pages;
import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.ExtentLogger;

import java.util.List;

public class PLPPage extends BasePage{
    private By stockOutBadge = By.xpath("//button[@class='button button--small button--soldout']/ancestor::li[contains(@class,'grid__item')]//a[contains(@class,'card-information__text')]");
    public PLPPage(WebDriver driver) {super(driver);}


    public int printSoldOutProducts() {
        int soldOutCount = 0;
        try {
            List<WebElement> soldOutProducts = driver.findElements(stockOutBadge);
             soldOutCount = soldOutProducts.size();
            if (soldOutCount == 0) {
                ExtentLogger.info("No Sold Out products found on this page.");
                return soldOutCount;
            }

            ExtentLogger.info("Sold Out Products Name List:");
            for (int i = 0; i < soldOutProducts.size(); i++) {
                String productName = soldOutProducts.get(i).getText().trim();
                System.out.println((i + 1) + ". " + productName);
            }

        } catch (Exception e) {
            System.err.println("❌ Error while fetching Sold Out products: " + e.getMessage());
            e.printStackTrace();
        }
        return soldOutCount;
    }

}
