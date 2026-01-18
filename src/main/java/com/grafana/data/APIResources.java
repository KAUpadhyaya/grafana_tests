package com.grafana.data;

public interface APIResources {
    String CREATE_FOLDER_IN_DASHBOARD = "requestBodies/createFolderInDashboard.json";

    static String foldersPath(){return "/api/folders";}

    static String foldersPath(String uid){
        return "/api/folders/".concat(uid);
    }

    static String apiSearchPath(){
        return "/api/search/";
    }


}
