package utils;

import base.BaseTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import static base.BaseTest.driver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import com.aventstack.extentreports.Status;
import utils.ExtentLogger;

public class TestListener implements ITestListener, IAnnotationTransformer {

    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static ExtentReports extent;

    @Override
    public void onStart(ITestContext context) {
        if (extent == null) {
            extent = ExtentManager.getInstance();
        }
        ExtentLogger.info("Test Suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentLogger.info("Test Suite finished: " + context.getName());
        if (extent != null) {
            extent.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        testThread.set(test);

        // register this extent test for logging
        ExtentLogger.setTest(test);

        ExtentLogger.info("Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = testThread.get();
        if (test != null) {
            ExtentLogger.pass("Test passed: " + result.getMethod().getMethodName());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentLogger.fail("Test failed: " + result.getMethod().getMethodName() + " - " + result.getThrowable());
        ExtentTest test = testThread.get();
        try {
            String screenshotPath = ScreenshotUtil.capture(driver, result.getMethod().getMethodName());
            if (screenshotPath != null && test != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
        } catch (Exception e) {
            ExtentLogger.info("Error capturing screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentLogger.info("Test skipped: " + result.getMethod().getMethodName());
        ExtentTest test = testThread.get();
        if (test != null) test.skip("Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    // Automatically attach retry analyzer to all @Test annotations if not already set
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Some TestNG versions don't expose a getter for the retry analyzer; to be
        // compatible we simply set our RetryAnalyzer here. This may override a
        // previously-configured analyzer, but ensures the retry behavior is applied.
        try {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        } catch (Throwable t) {
            ExtentLogger.info("Could not set RetryAnalyzer on annotation: " + t.getMessage());
        }
    }
}
