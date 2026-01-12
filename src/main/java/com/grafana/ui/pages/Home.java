package com.grafana.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Home {
    WebDriver driver;
    public Home(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.driver=driver;
    }

    @FindBy(css = ".css-1p1wt8c")
    protected WebElement navigationMegaMenu_element;
    public boolean isNavigationMegaMenuDisplayed(){
        try {return navigationMegaMenu_element.isDisplayed();}
        catch (Exception _){}
        return false;
    }

    @FindBy(xpath = "//a[@href='/dashboards']")
    protected WebElement dashboardMenu_button;
    public void selectDashboardMenu(){dashboardMenu_button.click();}


    /*
        Adding new Items to dashboard.
     */
    //@FindBy(xpath = "//button[.='New']")
    @FindBy(css = ".css-qjvyeh-button")
    protected WebElement addNewItem_button;

    //Dashboards
    @FindBy(xpath = "//a[@href='/dashboard/new']")
    protected WebElement addNewDashboard_button;

    //Folders
    @FindBy(xpath = "//button[.='New folder']")
    protected WebElement addNewFolder_button;

    @FindBy(id = "folder-name-input")
    protected WebElement folderName_input;

    @FindBy(css = ".css-3l5qae-button")
    protected WebElement create_button;

    @FindBy(xpath = "//span[.='Folder created']/../..")
    protected WebElement folderCreatedSuccessfully_Alert;

    public void addNewFolderToDashboard(String folderName){
        selectDashboardMenu();
        addNewItem_button.click();
        addNewFolder_button.click();
        folderName_input.sendKeys(folderName);
        create_button.click();
    }
    public boolean isAlertDisplayedAfterFolderIsCreated(){
        boolean state = false;
        try{
            state = folderCreatedSuccessfully_Alert.isDisplayed();
        }catch (Exception _){}
        return state;
    }
    public boolean isNewlyAddedFolderDisplayedInDashboard(String folderName){
        boolean state = false;
        dashboardMenu_button.click();
        try{
            state = driver.findElement(By.xpath("//a[.='"+folderName+"']")).isDisplayed();
        }catch (Exception _){}
        return state;
    }
    /*
        End....      [Adding new Items to dashboard.]
     */




}
