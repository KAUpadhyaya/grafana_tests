package com.grafana.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Login {
    WebDriver driver;
    public Login(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.driver=driver;
    }

    @FindBy(id = ":r0:")
    protected WebElement username_textbox;

    @FindBy(id = ":r1:")
    protected WebElement password_textbox;

    @FindBy(xpath = "//button[@data-testid='data-testid Login button']")
    protected WebElement logIn_button;

    public void performLogin(String username, String password){
        username_textbox.sendKeys(username);
        password_textbox.sendKeys(password);
        logIn_button.click();
    }


}
