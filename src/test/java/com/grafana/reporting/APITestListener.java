package com.grafana.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class APITestListener  implements ITestListener {

        private static ExtentReports extent = ExtentManager.getExtent("API");

        // Thread-safe for parallel execution
        private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

        @Override
        public void onTestStart(ITestResult result) {
        ExtentTest test =
                extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

        @Override
        public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test passed");
    }

        @Override
        public void onTestFailure(ITestResult result) {
        extentTest.get().log(Status.FAIL, result.getThrowable());
    }

        @Override
        public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "Test skipped");
    }

        @Override
        public void onFinish(ITestContext context) {
        extent.flush();
    }
}
