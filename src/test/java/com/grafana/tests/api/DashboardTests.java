package com.grafana.tests.api;

import com.grafana.APITestBase;
import com.grafana.data.APIResources;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.grafana.data.APIResources.*;
import static org.hamcrest.Matchers.hasSize;

public class DashboardTests extends APITestBase {
    private final Logger LOG = LogManager.getLogger(DashboardTests.class);
    String FOLDER_UID;

    @Test(groups = {"smoke"}, priority = 0, testName = "Verify empty folder list in Dashboard screen")
    public void getAllFolderTest() {
        LOG.info("Verifying API: get empty folder list");
        RestAssured
                .given().log().all().spec(defineRequestSpec())
                .queryParam("limit","1000")
                .queryParam("sort","alpha-desc")
                .queryParam("deleted","false")
                .when().get(APIResources.foldersPath())
                .then().log().all().spec(verifyResponseSpec("JSON", 200))
                .body("", hasSize(0));
    }

    @Test(groups = {"smoke"}, priority = 1, testName = "Create a folder in Dashboard screen")
    void createFolderTest() {
        LOG.info("Verifying API: create folder");
        Response response = RestAssured
                .given().log().all().spec(defineRequestSpec())
                .body(extractJSONBody(CREATE_FOLDER_IN_DASHBOARD))

                //TODO - this is my second approach for injecting JSON body into the request using pojo classes.
                //.body(new ForDashboard().createFolderInDashboard("test123"))

                .when().post(APIResources.foldersPath())
                .then().log().all().spec(verifyResponseSpec("JSON", 200))
                .extract().response();
        FOLDER_UID = response.jsonPath().getString("uid");
        System.out.println(FOLDER_UID);
    }

    @Test(groups = {"smoke"}, priority = 2, testName = "Get folder by UID in Dashboard screen", dependsOnMethods = "createFolderTest")
    void getFolderByUIDTest() {
        LOG.info("Verifying API: get folder by UID in dashboard");
        Response response = RestAssured
                .given().log().all().spec(defineRequestSpec())
                .when().get(APIResources.foldersPath(FOLDER_UID))
                .then().log().all().spec(verifyResponseSpec("JSON", 200))
                .extract().response();
        Assert.assertEquals(response.jsonPath().getString("uid"), FOLDER_UID);
        Assert.assertEquals(response.jsonPath().getString("createdBy"), USERNAME);
    }

    @Test(groups = {"smoke"}, priority = 3, testName = "Get all folders in Dashboard screen", dependsOnMethods = "createFolderTest")
    void getAllFoldersTest() {
        LOG.info("Verifying API: get all folders in dashboard");
        Response response = RestAssured
                .given().log().all().spec(defineRequestSpec())
                .when().get(APIResources.apiSearchPath())
                .then().log().all().spec(verifyResponseSpec("JSON", 200))
                .extract().response();
        Assert.assertEquals(response.jsonPath().getString("[0].uid"), FOLDER_UID);
        Assert.assertEquals(response.jsonPath().getString("[0].isDeleted"), "false");
        Assert.assertEquals(response.jsonPath().getString("[0].title"), "test234");
    }

    @Test(groups = {"smoke"}, priority = 4, testName = "Delete folder by UID in Dashboard screen", dependsOnMethods = "createFolderTest")
    void deleteFolderByUIDTest() {
        LOG.info("Verifying API: delete folder by UID in dashboard");
        Response response = RestAssured
                .given().log().all().spec(defineRequestSpec())
                .when().delete(APIResources.foldersPath(FOLDER_UID))
                .then().log().all().spec(verifyResponseSpec("JSON", 200))
                .extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), "Folder deleted");
    }

    @Test(groups = {"smoke"}, priority = 5, testName = "Delete the deleted folder by UID in Dashboard screen", dependsOnMethods = "deleteFolderByUIDTest")
    public void deleteDeletedFolderByUIDTest() {
        LOG.info("Verifying API: delete deleted folder by UID in dashboard");
        Response response = RestAssured
                .given().log().all().spec(defineRequestSpec())
                .when().delete(APIResources.foldersPath(FOLDER_UID))
                .then().log().all().spec(verifyResponseSpec("JSON", 404))
                .extract().response();
        Assert.assertEquals(response.jsonPath().getString("message"), "folder not found");
    }






}
