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

        // ✅ Force IST timezone (VERY IMPORTANT)
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

        ExtentSparkReporter reporter = new ExtentSparkReporter(fileName);
        reporter.config().setDocumentTitle("Bellavita Organic Automation Test Report");
        reporter.config().setReportName("Regression Suite Test Results");
        reporter.config().setTheme(Theme.STANDARD);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);

        extent.setSystemInfo("Framework", "Selenium Java + TestNG");
        extent.setSystemInfo("Author", "Gourav Kumar");
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("Timezone", "Asia/Kolkata (IST)");

        return extent;
    }
}
