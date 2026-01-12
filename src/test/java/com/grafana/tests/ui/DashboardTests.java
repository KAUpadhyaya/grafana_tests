package com.grafana.tests.ui;

import com.grafana.UITestBase;
import com.grafana.testUtil.APIUtil;
import com.grafana.ui.pages.Home;
import com.grafana.ui.pages.Login;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DashboardTests extends UITestBase {

    String folderName = "Autom_Test_1";
    APIUtil apiUtil=new APIUtil();

    @BeforeClass(groups = {"smoke"})
    void loginToGrafana(){
        new LoginTests().verifySuccessfulLogin();
    }

    @BeforeMethod(groups = {"smoke"})
    void restToHomePage(){
        getDriver().navigate().to(BASE_URI);
    }

    @Test(groups = {"smoke"}, testName = "Verify user is able to add new folder to dashboard.")
    void verifyAddingNewFolderInDashboard(){
        SoftAssert softAssert = new SoftAssert();
        Home inHomePage = new Home(getDriver());

        inHomePage.addNewFolderToDashboard(folderName);

        boolean alertState = inHomePage.isAlertDisplayedAfterFolderIsCreated();
        softAssert.assertTrue(alertState, "Success Alert not displayed after folder is created.");

        boolean newFolderState = inHomePage.isNewlyAddedFolderDisplayedInDashboard(folderName);
        softAssert.assertTrue(newFolderState, "Newly added folder is not displayed in dashboard.");

        softAssert.assertAll();
    }

    @AfterClass(groups = {"smoke"})
    void cleanUp(){
        //Remove the added test folder from dashboard.
       apiUtil.deleteFolderFromDashboard(folderName);
    }


}
