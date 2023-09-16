package cs211.project.controllers;

import cs211.project.models.Activity;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.ActivityListFileDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalTime;

public class ParticipantScheduleController {
    @FXML private TableView<Activity> activityTableView;
    @FXML private ComboBox chooseEvent;
    private ActivityList activityList;
    private ActivityList list;
    private Datasource<ActivityList> datasource;
    private String eventName;
    @FXML
    public void initialize() {
        datasource = new ActivityListFileDatasource("data", "activity-list.csv");
        activityList = datasource.readData();
        String userId = "UADG";
        for(Activity activity:activityList.getAllActivities()){
            if(activity.userIsParticipant(userId)){
                System.out.println(activity.getEventName());
                chooseEvent.getItems().add(activity.getEventName());
            }
        }

    }
    private void showTable(ActivityList activityList) {
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, String> dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

        TableColumn<Activity, LocalTime> participantColumn = new TableColumn<>("Participant");
        participantColumn.setCellValueFactory(new PropertyValueFactory<>("participantName"));

        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);
        activityTableView.getColumns().add(participantColumn);

        activityTableView.getItems().clear();

        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        for (Activity activity: activityList.getActivities()) {
            activityTableView.getItems().add(activity);
        }
    }


    @FXML
    protected void onSearchClick() {
        eventName = (String) chooseEvent.getValue();
        updateSchedule();
        showTable(activityList);
    }
    private void updateSchedule(){
        activityList = datasource.readData();
        activityList.findActivityInEvent(eventName);
    }

    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
