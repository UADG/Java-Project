package cs211.project.controllers;

import cs211.project.models.Activity;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.EventList;
import cs211.project.services.ActivityListFileDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.EventHardCode;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalTime;

public class CreateScheduleController {
    @FXML TextField activityTextField;
    @FXML ComboBox chooseDate;
    @FXML ComboBox chooseHourTimeStart;
    @FXML ComboBox chooseMinTimeStart;
    @FXML ComboBox chooseHourTimeStop;
    @FXML ComboBox chooseMinTimeStop;
    @FXML private Label activityNameLabel;
    @FXML private Label dateLabel;
    @FXML private Label timeStartLabel;
    @FXML private Label timeStopLabel;
    @FXML private Label errorActivityNameLabel;
    @FXML private TableView<Activity> activityTableView;
    private String eventName;
    private ActivityList activityList;
    private Activity selectedActivity;
    private EventList eventList;
    private Datasource<ActivityList> datasource;


    @FXML
    public void initialize() {
        clearActivityInfo();
        errorActivityNameLabel.setText("");
        datasource = new ActivityListFileDatasource("data", "activity-list.csv");
        activityList = datasource.readData();
        EventHardCode datasource = new EventHardCode();
        eventList = datasource.readData();
        eventName = eventList.findEventByEventName("Fes").getEventName().trim();
        chooseDate.getItems().addAll(eventList.findEventByEventName("Fes").getArrayDate());
        chooseHourTimeStart.getItems().addAll(eventList.findEventByEventName("Fes").getArrayHour());
        chooseMinTimeStart.getItems().addAll(eventList.findEventByEventName("Fes").getArrayMinute());
        chooseHourTimeStop.getItems().addAll(eventList.findEventByEventName("Fes").getArrayHour());
        chooseMinTimeStop.getItems().addAll(eventList.findEventByEventName("Fes").getArrayMinute());
        activityList.addActivityInEvent(eventName);
        showTable(activityList);
        activityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Activity>() {
            @Override
            public void changed(ObservableValue observable, Activity oldValue, Activity newValue) {
                if (newValue == null) {
                    clearActivityInfo();
                    selectedActivity = null;
                } else {
                    showActivityInfo(newValue);
                    selectedActivity = newValue;
                }
            }
        });
    }

    private void showTable(ActivityList activityList) {
        // กำหนด column ให้มี title ว่า ID และใช้ค่าจาก attribute id ของ object Student
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, String> dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // กำหนด column ให้มี title ว่า Name และใช้ค่าจาก attribute name ของ object Student
        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        // กำหนด column ให้มี title ว่า Score และใช้ค่าจาก attribute score ของ object Student
        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);

        activityTableView.getItems().clear();

        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        for (Activity activity: activityList.getActivities()) {
            activityTableView.getItems().add(activity);
        }
    }

    private void showActivityInfo(Activity activity) {

        activityNameLabel.setText(activity.getActivityName());
        dateLabel.setText(activity.getDate());
        timeStartLabel.setText(activity.getStartTimeActivity());
        timeStopLabel.setText(activity.getEndTimeActivity());
    }
    private void clearActivityInfo() {
        activityNameLabel.setText("");
        dateLabel.setText("");
        timeStartLabel.setText("");
        timeStopLabel.setText("");
    }
    @FXML
    protected void addActivityOnClick(){
        try {
            String activityName = activityTextField.getText();
            String date = (String) chooseDate.getValue();
            String hourStartStr = (String) chooseHourTimeStart.getValue();
            String minStartStr = (String) chooseMinTimeStart.getValue();
            String hourEndStr = (String) chooseHourTimeStop.getValue();
            String minEndStr = (String) chooseMinTimeStop.getValue();
        if (!activityName.isEmpty() && date != null && hourStartStr != null && minStartStr != null && hourEndStr != null && minEndStr != null) {
            int hourStart = Integer.parseInt(hourStartStr);
            int minStart = Integer.parseInt(minStartStr);
            int hourEnd = Integer.parseInt(hourEndStr);
            int minEnd = Integer.parseInt(minEndStr);

            LocalTime startTimeActivity = LocalTime.of(hourStart, minStart);
            LocalTime endTimeActivity = LocalTime.of(hourEnd, minEnd);
            if (activityList.checkActivity(activityName, date, startTimeActivity, endTimeActivity)) {
                activityList.addActivity(activityName, date, startTimeActivity, endTimeActivity, null, "", "0", eventName);
                datasource.writeData(activityList);
                if(activityList.getActivities().isEmpty()){
                        activityList.addActivityInEvent(eventName);
                }
                showTable(activityList);
            }

            else {
                errorActivityNameLabel.setText("This activity conflicts with an existing activity.");
            }
        }
        else if(activityName.isEmpty()){
            errorActivityNameLabel.setText("Please fill the name");
        }
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void backOnClick(){
        try {
            FXRouter.goTo("create-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void nextOnClick(){
        try {
            FXRouter.goTo("create-team", activityList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
