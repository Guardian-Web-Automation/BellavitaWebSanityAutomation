package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.ExtentLogger;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxRetryCount = 2; // retry up to 2 times

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            ExtentLogger.info("Retrying test " + result.getMethod().getMethodName() + " (attempt " + (retryCount+1) + ")");
            return true;
        }
        return false;
    }
}

