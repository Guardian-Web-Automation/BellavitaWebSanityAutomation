package utils;

import base.BaseTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import static base.BaseTest.driver;

public class TestListener implements ITestListener {

    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static ExtentReports extent;

    @Override
    public void onStart(ITestContext context) {
        if (extent == null) {
            extent = ExtentManager.getInstance();
        }
        System.out.println("Test Suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite finished: " + context.getName());
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

        test.info("Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = testThread.get();
        if (test != null) {
            test.pass("Test passed");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = testThread.get();
        if (test != null) {
            test.fail("Test failed: " + result.getThrowable());

            try {
                BaseTest base = (BaseTest) result.getInstance();
                String screenshotPath =
                        ScreenshotUtil.capture(driver, result.getMethod().getMethodName());

                if (screenshotPath != null) {
                    test.addScreenCaptureFromPath(screenshotPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = testThread.get();
        if (test != null) {
            test.skip("Test skipped: " + result.getMethod().getMethodName());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
}
