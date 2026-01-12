package com.grafana.data;

import java.util.ResourceBundle;

public class PropertyReader extends Properties {
    public static void fetchFrom(String propertyFileOrEnvironment){
        ResourceBundle resourceBundle = ResourceBundle.getBundle("env/"+propertyFileOrEnvironment);
        //#############     Common properties
        BASE_URI = resourceBundle.getString("BASE_URI");
        USERNAME = resourceBundle.getString("USERNAME");
        PASSWORD = resourceBundle.getString("PASSWORD");

        //#############     UI Specific
        BROWSER = resourceBundle.getString("BROWSER");
    }


}
