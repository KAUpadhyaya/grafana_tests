package com.grafana;

import com.grafana.data.Properties;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class APITestBase extends Properties {

    private final Logger LOG = LogManager.getLogger(APITestBase.class);

    public String ENV;
    Util fromCommonTestUtil = new Util();

    @BeforeSuite(groups = {"smoke", "regression"})
    public void setUp(){
        ENV = fromCommonTestUtil.setTestEnvironment();
        LOG.info("***************************************************");
        LOG.info("Starting API test in {}", ENV);
        LOG.info("***************************************************");
    }

    public RequestSpecification defineRequestSpec(){
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .setAuth(RestAssured.preemptive().basic(USERNAME, PASSWORD))
                .build();
    }

    public ResponseSpecification verifyResponseSpec(String contentType, int statusCode){
        if(contentType.equalsIgnoreCase("JSON"))
            return new ResponseSpecBuilder().expectContentType(ContentType.JSON).expectStatusCode(statusCode).build();
        return null;
    }

    protected String extractJSONBody(String jsonFilePath){
        String jsonBody=null;
        try {
            jsonBody = Files.readString(Paths.get(
                            Objects.requireNonNull(
                                    getClass()
                                            .getClassLoader()
                                            .getResource(jsonFilePath)
                            ).toURI()
                    )
            );
        }
        catch (IOException _) {}
        catch (URISyntaxException e) {throw new RuntimeException(e);}
        return jsonBody;
    }


}
