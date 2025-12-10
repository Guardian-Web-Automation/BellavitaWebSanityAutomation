package utils;


import com.aventstack.extentreports.Status;

public class ExtentLogger {

    private static ThreadLocal<com.aventstack.extentreports.ExtentTest> extentTest = new ThreadLocal<>();

    public static void setTest(com.aventstack.extentreports.ExtentTest test) {
        extentTest.set(test);
    }

    public static void info(String message) {
        System.out.println("[INFO] " + message); // normal console log
        if (extentTest.get() != null)
            extentTest.get().log(Status.INFO, message);
    }

    public static void pass(String message) {
        System.out.println("[PASS] " + message);
        if (extentTest.get() != null)
            extentTest.get().log(Status.PASS, message);
    }

    public static void fail(String message) {
        System.out.println("[FAIL] " + message);
        if (extentTest.get() != null)
            extentTest.get().log(Status.FAIL, message);
    }

    public static com.aventstack.extentreports.ExtentTest getTest() {
        return extentTest.get();
    }
}
