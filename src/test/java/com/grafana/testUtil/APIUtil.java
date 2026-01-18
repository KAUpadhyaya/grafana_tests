package com.grafana.testUtil;

import com.grafana.APITestBase;
import com.grafana.data.APIResources;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIUtil extends APITestBase{

    public void deleteFolderFromDashboard(String folderName){
        Response response = RestAssured
                .given().spec(defineRequestSpec())
                .queryParam("limit","1000")
                .queryParam("sort","alpha-desc")
                .queryParam("deleted","false")
                .when().get(APIResources.foldersPath())
                .then().log().all().spec(verifyResponseSpec("JSON", 200))
                        .extract().response();

        String folderUid = response.jsonPath().getString("find { it.title == '"+folderName+"' }.uid");

        RestAssured
                .given().spec(defineRequestSpec())
                .when().delete(APIResources.foldersPath(folderUid))
                .then().log().all().spec(verifyResponseSpec("JSON", 200));
    }




}
