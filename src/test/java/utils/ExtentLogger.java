package utils;


import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtentLogger {

    private static ThreadLocal<com.aventstack.extentreports.ExtentTest> extentTest = new ThreadLocal<>();
    private static final Logger LOG = LogManager.getLogger(ExtentLogger.class);

    public static void setTest(com.aventstack.extentreports.ExtentTest test) {
        extentTest.set(test);
    }

    public static void info(String message) {
        // single, consistent log via Log4j and Extent
        LOG.info(message);
        if (extentTest.get() != null) {
            extentTest.get().log(Status.INFO, message);
        }
    }

    public static void pass(String message) {
        LOG.info("PASS: {}", message);
        if (extentTest.get() != null) {
            extentTest.get().log(Status.PASS, message);
        }
    }

    public static void fail(String message) {
        LOG.error(message);
        if (extentTest.get() != null) {
            extentTest.get().log(Status.FAIL, message);
        }
    }

    public static com.aventstack.extentreports.ExtentTest getTest() {
        return extentTest.get();
    }

    private ExtentLogger() {}
}
