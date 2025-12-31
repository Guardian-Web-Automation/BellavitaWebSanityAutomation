package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.TimeZone;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance("reports/AutomationReport.html");
        }
        return extent;
    }

    public static ExtentReports createInstance(String fileName) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        ExtentSparkReporter reporter = new ExtentSparkReporter(fileName);
        reporter.config().setDocumentTitle("Bellavita Organic Automation Test Report");
        reporter.config().setReportName("Regression suite Test Results");
        reporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Framework", "Selenium Java TestNG");
        extent.setSystemInfo("Author", "Gaurav Kumar");
        extent.setSystemInfo("Browser", "Chrome");

        return extent;
    }
}
