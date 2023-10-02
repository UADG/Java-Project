package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.ActivityListFileDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class CreateScheduleController {
    @FXML TextField activityTextField;
    @FXML ComboBox chooseHourTimeStart;
    @FXML ComboBox chooseMinTimeStart;
    @FXML ComboBox chooseHourTimeStop;
    @FXML ComboBox chooseMinTimeStop;
    @FXML DatePicker startDate;
    @FXML DatePicker endDate;
    @FXML private Label activityNameLabel;
    @FXML private Label dateLabel;
    @FXML private Label timeStartLabel;
    @FXML private Label timeStopLabel;
    @FXML private Label errorActivityNameLabel;
    @FXML private TableView<Activity> activityTableView;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private Button adminButton;
    @FXML private BorderPane bPane;
    private String eventName;
    private ActivityList activityList;
    private Activity selectedActivity;
    private Event event = (Event) FXRouter.getData();
    private Datasource<ActivityList> datasource;
    private Datasource<AccountList> accountListDatasource = new AccountListDatasource("data", "user-info.csv");
    private AccountList accountList = accountListDatasource.readData();
    private Account account = accountList.findAccountByUsername(event.getEventManager());


    @FXML
    public void initialize() {
        clearActivityInfo();
        errorActivityNameLabel.setText("");
        datasource = new ActivityListFileDatasource("data", "activity-list.csv");
        eventName = event.getEventName();
        updateSchedule();

        addComboBox(event);
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
        bPane.setVisible(false);
        slide.setTranslateX(-200);
        adminButton.setVisible(false);
        if(account.isAdmin(account.getRole())){
            adminButton.setVisible(true);
        }
    }

    private void showTable(ActivityList activityList) {
        // กำหนด column ให้มี title ว่า ID และใช้ค่าจาก attribute id ของ object Student
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, LocalDate> startDateActivityColumn = new TableColumn<>("startDate");
        startDateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Activity, LocalDate> endDateActivityColumn = new TableColumn<>("endDate");
        endDateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        // กำหนด column ให้มี title ว่า Name และใช้ค่าจาก attribute name ของ object Student
        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        // กำหนด column ให้มี title ว่า Score และใช้ค่าจาก attribute score ของ object Student
        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(startDateActivityColumn);
        activityTableView.getColumns().add(endDateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);

        activityTableView.getItems().clear();

        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        for (Activity activity: activityList.getActivities()) {
            activityTableView.getItems().add(activity);
        }
    }
    private void addComboBox(Event event){
        chooseHourTimeStart.getItems().addAll(event.getArrayHour());
        chooseMinTimeStart.getItems().addAll(event.getArrayMinute());
        chooseHourTimeStop.getItems().addAll(event.getArrayHour());
        chooseMinTimeStop.getItems().addAll(event.getArrayMinute());
    }
    private void showActivityInfo(Activity activity) {

        activityNameLabel.setText(activity.getActivityName());
        dateLabel.setText(activity.getStartDate()+"     "+activity.getEndDate());
        timeStartLabel.setText(activity.getStartTimeActivity());
        timeStopLabel.setText(activity.getEndTimeActivity());
    }
    private void clearActivityInfo() {
        activityNameLabel.setText("");
        dateLabel.setText("");
        timeStartLabel.setText("");
        timeStopLabel.setText("");
    }
    private void updateSchedule(){
        activityList = datasource.readData();
        activityList.findActivityInEvent(eventName);
    }

    @FXML
    protected void addActivityOnClick(){
        try {
            String activityName = activityTextField.getText();
            String hourStartStr = (String) chooseHourTimeStart.getValue();
            String minStartStr = (String) chooseMinTimeStart.getValue();
            LocalDate selectedStartDate =  startDate.getValue();
            LocalDate selectedEndDate = endDate.getValue();
            String hourEndStr = (String) chooseHourTimeStop.getValue();
            String minEndStr = (String) chooseMinTimeStop.getValue();
        if (!activityName.isEmpty() && selectedStartDate != null && selectedEndDate != null&& hourStartStr != null && minStartStr != null && hourEndStr != null && minEndStr != null) {
            int hourStart = Integer.parseInt(hourStartStr);
            int minStart = Integer.parseInt(minStartStr);
            int hourEnd = Integer.parseInt(hourEndStr);
            int minEnd = Integer.parseInt(minEndStr);

            LocalTime startTimeActivity = LocalTime.of(hourStart, minStart);
            LocalTime endTimeActivity = LocalTime.of(hourEnd, minEnd);
            if (activityList.checkActivity(activityName, selectedStartDate, selectedEndDate, startTimeActivity, endTimeActivity)) {
                activityList.addActivity(activityName, selectedStartDate, selectedEndDate, startTimeActivity, endTimeActivity, null, "", "0", eventName);
                datasource.writeData(activityList);
                if(activityList.getActivities().isEmpty()){
                        activityList.findActivityInEvent(eventName);
                }
                updateSchedule();
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
    protected void deleteOnClick(){
        System.out.println(selectedActivity.getActivityName());
        selectedActivity.deleteActivity();
        System.out.println(selectedActivity.getActivityName());
        datasource.writeData(activityList);
        updateSchedule();
        System.out.println(selectedActivity.getActivityName());
        showTable(activityList);
    }

    @FXML
    protected void backOnClick(){
        try {
            FXRouter.goTo("event-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void nextOnClick(){
        try {
            FXRouter.goTo("create-team", event.getEventName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void OnMenuBarClick() throws IOException {
        TranslateTransition slideAnimate = new TranslateTransition();
        slideAnimate.setDuration(Duration.seconds(0.5));
        slideAnimate.setNode(slide);
        slideAnimate.setToX(0);
        slideAnimate.play();
        menuButton.setVisible(false);
        slide.setTranslateX(0);
        bPane.setVisible(true);
    }
    @FXML
    public void closeMenuBar() throws IOException {
        TranslateTransition slideAnimate = new TranslateTransition();
        slideAnimate.setDuration(Duration.seconds(0.5));
        slideAnimate.setNode(slide);
        slideAnimate.setToX(-200);
        slideAnimate.play();
        slide.setTranslateX(-200);
        slideAnimate.setOnFinished(event-> {
            menuButton.setVisible(true);
            bPane.setVisible(false);
        });
    }
    @FXML
    public void onHomeClick() throws IOException {
        FXRouter.goTo("events-list", account);
    }
    @FXML
    public void onProfileClick() throws IOException {
        FXRouter.goTo("profile-setting", account);
    }
    @FXML
    public void onCreateEvent() throws IOException {
        FXRouter.goTo("create-event", account);
    }
    @FXML
    public void onJoinHistory() throws IOException {
        FXRouter.goTo("joined-history", account);
    }
    @FXML
    public void onEventHis() throws IOException {
        FXRouter.goTo("event-history", account);
    }
    @FXML
    public void onPartiSchedule() throws IOException {
        FXRouter.goTo("participant-schedule", account);
    }
    @FXML
    public void onTeamSchedule() throws IOException {
        FXRouter.goTo("team-schedule", account);
    }
    @FXML
    public void onComment() throws IOException {
        FXRouter.goTo("comment-activity", account);
    }
    @FXML
    public void onUserClick() throws IOException {
        FXRouter.goTo("user-status", account);
    }
}
