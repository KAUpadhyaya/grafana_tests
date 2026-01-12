package com.grafana.tests.ui;

import com.grafana.UITestBase;
import com.grafana.ui.pages.Home;
import com.grafana.ui.pages.Login;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends UITestBase {

    @Test(groups = {"smoke"}, testName = "Verify user is able to login to grafana with valid credentials.")
    public void verifySuccessfulLogin(){
        Login inLoginPage = new Login(getDriver());
        Home inHomePage = new Home(getDriver());
        inLoginPage.performLogin(USERNAME,PASSWORD);
        Assert.assertTrue(inHomePage.isNavigationMegaMenuDisplayed(), "Navigation menu not visible, login may not be suceessful.");
    }
}
