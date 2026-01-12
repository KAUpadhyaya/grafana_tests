package com.grafana.testData;

import com.grafana.api.pojo.dashboard.CreateFolder;

public class ForDashboard {
    public CreateFolder createFolderInDashboard(String folderName){
        CreateFolder createFolder=new CreateFolder();
        createFolder.setTitle(folderName);
        return createFolder;
    }
}
