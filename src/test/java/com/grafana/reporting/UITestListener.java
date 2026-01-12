package com.grafana.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.grafana.UITestBase;
import com.grafana.ui.Util;
import org.testng.*;

public class UITestListener extends UITestBase implements ITestListener {

    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest =
                ExtentManager.getExtent("UI").createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String path = Util.takeScreenshot(getDriver(), result.getMethod().getMethodName());
        test.get().fail(result.getThrowable());

        if (path != null) {
            test.get().addScreenCaptureFromPath(path);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getExtent("UI").flush();
    }
}
