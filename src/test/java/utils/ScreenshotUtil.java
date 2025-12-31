package utils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.io.IOException;

public class ScreenshotUtil {
    public static String capture(WebDriver driver, String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            // ensure reports/screenshots directory exists
            Path dir = Path.of("reports", "screenshots");
            if (!Files.exists(dir)) {
                try { Files.createDirectories(dir); } catch (IOException ignored) {}
            }

            String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS").withZone(ZoneOffset.UTC).format(Instant.now());
            String fileName = screenshotName + "_" + timestamp + ".png";
            Path dest = dir.resolve(fileName);
            Files.copy(src.toPath(), dest);
            return dest.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
