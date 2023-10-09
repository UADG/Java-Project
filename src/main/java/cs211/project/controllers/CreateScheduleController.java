package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CreateScheduleController {
    @FXML TextField activityTextField;
    @FXML TextField infoActivityTextField;
    @FXML ComboBox chooseHourTimeStart;
    @FXML ComboBox chooseMinTimeStart;
    @FXML ComboBox chooseHourTimeStop;
    @FXML ComboBox chooseMinTimeStop;
    @FXML DatePicker startDate;
    @FXML DatePicker endDate;
    @FXML private AnchorPane parent;
    @FXML private Label eventNameLabel;
    @FXML private Label eventDateStart;
    @FXML private Label eventDateEnd;
    @FXML private Label eventTimeStart;
    @FXML private Label eventTimeEnd;

    @FXML private TableView<Activity> activityTableView;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private BorderPane bPane;
    @FXML private AnchorPane parent;
    private Object[] objectsSend;
    private Object[] objects;
    private String eventName;
    private ActivityList activityList;
    private Activity selectedActivity;
    private Event event;
    private Datasource<ActivityList> datasource;
    private Datasource<AccountList> accountListDatasource = new AccountListDatasource("data", "user-info.csv");
    private AccountList accountList = accountListDatasource.readData();
    private Account account;
    private Datasource<TeamList> teamListDatasource;
    private TeamList teams;
<<<<<<< HEAD
    private ThemeDatasource themeDatasource = new ThemeDatasource("data", "theme.csv");
    private String theme = themeDatasource.read();


    @FXML
    public void initialize() {
        loadTheme(theme);
=======
    private Boolean isLightTheme;

    @FXML
    public void initialize() {
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        event = (Event) objects[1];
        isLightTheme = (Boolean) objects[2];

        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;
        loadTheme(isLightTheme);

>>>>>>> 8ab29c07a331938002d3ef6deeeaf29016062bbf
        datasource = new ActivityListFileDatasource("data", "activity-list.csv");
        eventName = event.getEventName();
        updateSchedule();
        teams = teamListDatasource.readData();
        eventNameLabel.setText(eventName);
        addComboBox(event);
        showTable(activityList);
        activityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Activity>() {
            @Override
            public void changed(ObservableValue observable, Activity oldValue, Activity newValue) {
                if (newValue == null) {
                    selectedActivity = null;
                } else {
                    selectedActivity = newValue;
                }
            }
        });
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    private void showTable(ActivityList activityList) {
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, LocalDate> startDateActivityColumn = new TableColumn<>("startDate");
        startDateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Activity, LocalDate> endDateActivityColumn = new TableColumn<>("endDate");
        endDateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

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

    private void updateSchedule(){
        activityList = datasource.readData();
        activityList.findActivityInEvent(eventName);
        teamListDatasource = new TeamListFileDatasource("data","team.csv");
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
            String infoActivity =  infoActivityTextField.getText();
        if (!activityName.isEmpty() && selectedStartDate != null && selectedEndDate != null&& hourStartStr != null && minStartStr != null && hourEndStr != null && minEndStr != null) {
            int hourStart = Integer.parseInt(hourStartStr);
            int minStart = Integer.parseInt(minStartStr);
            int hourEnd = Integer.parseInt(hourEndStr);
            int minEnd = Integer.parseInt(minEndStr);

            LocalTime startTimeActivity = LocalTime.of(hourStart, minStart);
            LocalTime endTimeActivity = LocalTime.of(hourEnd, minEnd);
            LocalDateTime startActivityTime = LocalDateTime.of(selectedStartDate,startTimeActivity);
            LocalDateTime endActivityTime = LocalDateTime.of(selectedEndDate,endTimeActivity);
            if(event.checkTimeActivity(startActivityTime,endActivityTime)){
                if(activityList.checkActivityName(activityName)) {
                    if (activityList.checkActivity(startActivityTime,endActivityTime)) {
                        activityList.addActivity(activityName, selectedStartDate, selectedEndDate, startTimeActivity, endTimeActivity, "", "", "0", eventName, infoActivity, null);
                        datasource.writeData(activityList);
                        if (activityList.getActivities().isEmpty()) {
                            activityList.findActivityInEvent(eventName);
                        }
                        updateSchedule();
                        showTable(activityList);
                    } else {
                        showErrorAlert("Please select other time");
                    }
                }
                else{
                    showErrorAlert("Please select other name");
                }
            }
            else {
                showErrorAlert("Please select time in event \n" + event.getStartDate() + "     "+ event.getStartTime() + "\nto\n"+ event.getEndDate() + "     " + event.getEndTime());
            }
        }
        else if(activityName.isEmpty()){
            showErrorAlert("Please fill the name");
        }
        else {
            showErrorAlert("Please fill the time");
        }
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void deleteOnClick(){
        if(selectedActivity != null){
            if(!selectedActivity.getTeamName().equals("")){
                System.out.println(selectedActivity.getTeamName());
                teams.deleteTeam(event,selectedActivity.getTeamName());
            }
            selectedActivity.deleteActivity();
            teamListDatasource.writeData(teams);
            datasource.writeData(activityList);
            updateSchedule();
            showTable(activityList);
        }
        else{
            showErrorAlert("Please select activity");
        }
    }

    @FXML
    protected void backOnClick(){
        try {
            FXRouter.goTo("event-history", objectsSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    protected void nextOnClick(){
        try {
            FXRouter.goTo("create-team", objects);
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
        FXRouter.goTo("events-list", objectsSend);
    }
    @FXML
    public void onProfileClick() throws IOException {
        FXRouter.goTo("profile-setting", objectsSend);
    }
    @FXML
    public void onCreateEvent() throws IOException {
        FXRouter.goTo("create-event", objectsSend);
    }
    @FXML
    public void onJoinHistory() throws IOException {
        FXRouter.goTo("joined-history", objectsSend);
    }
    @FXML
    public void onEventHis() throws IOException {
        FXRouter.goTo("event-history", objectsSend);
    }
    @FXML
    public void onPartiSchedule() throws IOException {
        FXRouter.goTo("participant-schedule", objectsSend);
    }
    @FXML
    public void onTeamSchedule() throws IOException {
        FXRouter.goTo("team-schedule", objectsSend);
    }
    @FXML
    public void onComment() throws IOException {
        FXRouter.goTo("comment-activity", objectsSend);
    }
    @FXML
    public void onLogOutButton() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = LocalDateTime.now().format(formatter);
        account.setTime(time);
        Datasource<AccountList> dataSource = new AccountListDatasource("data","user-info.csv");
        dataSource.writeData(accountList);
        FXRouter.goTo("login-page");
    }
<<<<<<< HEAD
=======

    private void loadTheme(Boolean theme) {
        if (theme) {
            loadTheme("st-theme.css");
        } else {
            loadTheme("dark-theme.css");
        }
    }

>>>>>>> 8ab29c07a331938002d3ef6deeeaf29016062bbf
    private void loadTheme(String themeName) {
        if (parent != null) {
            String cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
