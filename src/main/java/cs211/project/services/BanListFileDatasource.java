package cs211.project.services;

import cs211.project.models.Staff;
import cs211.project.models.Team;
import cs211.project.models.collections.StaffList;
import cs211.project.models.collections.TeamList;
import javafx.fxml.FXML;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BanListFileDatasource implements Datasource<StaffList>{
    private String directoryName;
    private String fileName;
    public BanListFileDatasource(String directoryName, String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public StaffList readData() {
        return new StaffList();
    }

    @Override
    public void writeData(StaffList data) {


    }
}
