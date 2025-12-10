package utils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.nio.file.Files;

public class ScreenshotUtil {
    public static String capture(WebDriver driver, String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            String path = "reports/screenshots/" + screenshotName + ".png";
            File dest = new File(path);
            Files.copy(src.toPath(), dest.toPath());
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
