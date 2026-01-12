package com.grafana;

import com.grafana.data.PropertyReader;

public class Util {
    public String setTestEnvironment(){
        String ENV = System.getProperty("env");
        if(ENV==null)ENV="qc";
        PropertyReader.fetchFrom(ENV);
        return ENV;
    }


}
