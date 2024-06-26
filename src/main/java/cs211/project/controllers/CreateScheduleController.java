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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CreateScheduleController {
    @FXML
    private TextField activityTextField;
    @FXML
    private TextField infoActivityTextField;
    @FXML
    private ComboBox chooseHourTimeStart;
    @FXML
    private ComboBox chooseMinTimeStart;
    @FXML
    private ComboBox chooseHourTimeStop;
    @FXML
    private ComboBox chooseMinTimeStop;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private AnchorPane parent;
    @FXML
    private Label eventNameLabel;
    @FXML private Label eventStartDate;
    @FXML private Label eventEndDate;
    @FXML private Label eventStartTime;
    @FXML private Label eventEndTime;

    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private AnchorPane slide;
    @FXML
    private Button menuButton;
    @FXML
    private BorderPane bPane;
    @FXML
    private ImageView logoImageView;
    private Alert alert;
    private Object[] objectsSend;
    private Object[] objects;
    private String eventName;
    private ActivityList activityList;
    private Activity selectedActivity;
    private Event event;
    private TranslateTransition slideAnimate;
    private DateTimeFormatter formatter;
    private String time;
    private Datasource<ActivityList> activityListDatasource;
    private Datasource<AccountList> accountListDatasource;
    private AccountList accountList;
    private Account account;
    private Datasource<TeamList> teamListDatasource;
    private TeamList teams;
    private Boolean isLightTheme;
    private String activityName;
    private String hourStartStr;
    private String minStartStr;
    private LocalDate selectedStartDate;
    private LocalDate selectedEndDate;
    private String hourEndStr;
    private String minEndStr;
    private String infoActivity;
    private int hourStart;
    private int minStart;
    private int hourEnd;
    private int minEnd;
    private LocalTime startTimeActivity;
    private LocalTime endTimeActivity;
    private LocalDateTime startActivityTime;
    private LocalDateTime endActivityTime;
    private String cssPath;

    @FXML
    public void initialize() {
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        accountList = accountListDatasource.readData();

        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        event = (Event) objects[1];
        isLightTheme = (Boolean) objects[2];

        if (isLightTheme) {
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
        } else {
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-dark-theme.png").toExternalForm()));
        }

        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;

        loadTheme(isLightTheme);

        activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");
        eventName = event.getEventName();
        eventStartDate.setText(String.valueOf(event.getStartDate()));
        eventEndDate.setText(String.valueOf(event.getEndDate()));
        eventStartTime.setText(event.getStartTime());
        eventEndTime.setText(event.getEndTime());
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

        for (Activity activity: activityList.getActivities()) {
            activityTableView.getItems().add(activity);
        }
    }

    private void addComboBox(Event event) {
        chooseHourTimeStart.getItems().addAll(event.getArrayHour());
        chooseMinTimeStart.getItems().addAll(event.getArrayMinute());
        chooseHourTimeStop.getItems().addAll(event.getArrayHour());
        chooseMinTimeStop.getItems().addAll(event.getArrayMinute());
    }

    private void updateSchedule() {
        activityList = activityListDatasource.readData();
        activityList.findActivityInEvent(eventName);
        teamListDatasource = new TeamListFileDatasource("data","team.csv");
    }

    @FXML
    protected void addActivityOnClick() {
        try {
            activityName = activityTextField.getText();
            hourStartStr = (String) chooseHourTimeStart.getValue();
            minStartStr = (String) chooseMinTimeStart.getValue();
            selectedStartDate =  startDate.getValue();
            selectedEndDate = endDate.getValue();
            hourEndStr = (String) chooseHourTimeStop.getValue();
            minEndStr = (String) chooseMinTimeStop.getValue();
            infoActivity =  infoActivityTextField.getText();
            if (!activityName.isEmpty() && selectedStartDate != null && selectedEndDate != null&& hourStartStr != null && minStartStr != null && hourEndStr != null && minEndStr != null) {
                hourStart = Integer.parseInt(hourStartStr);
                minStart = Integer.parseInt(minStartStr);
                hourEnd = Integer.parseInt(hourEndStr);
                minEnd = Integer.parseInt(minEndStr);

                startTimeActivity = LocalTime.of(hourStart, minStart);
                endTimeActivity = LocalTime.of(hourEnd, minEnd);
                startActivityTime = LocalDateTime.of(selectedStartDate,startTimeActivity);
                endActivityTime = LocalDateTime.of(selectedEndDate,endTimeActivity);
                if(event.checkTimeActivity(startActivityTime,endActivityTime)){
                    if(activityList.checkActivityName(activityName)) {
                        if (activityList.checkActivity(startActivityTime,endActivityTime)) {
                            if(infoActivity == ""){
                                infoActivity = null;
                            }
                            activityList.addActivity(activityName, selectedStartDate, selectedEndDate, startTimeActivity, endTimeActivity, "", "", "0", eventName, infoActivity);
                            activityListDatasource.writeData(activityList);
                            if (activityList.getActivities().isEmpty()) {
                                activityList.findActivityInEvent(eventName);
                            }

                            updateSchedule();
                            showTable(activityList);
                            activityTextField.clear();
                            chooseHourTimeStart.setValue(null);
                            chooseMinTimeStart.setValue(null);
                            chooseHourTimeStop.setValue(null);
                            chooseMinTimeStop.setValue(null);
                            startDate.setValue(null);
                            endDate.setValue(null);
                            infoActivityTextField.clear();
                        } else {
                            showErrorAlert("Please select other time");
                        }
                    } else {
                        showErrorAlert("Please select other name");
                    }
                } else {
                    showErrorAlert("Please select time in event \n" + event.getStartDate() + "     "+ event.getStartTime() + "\nto\n"+ event.getEndDate() + "     " + event.getEndTime());
                }
            } else if (activityName.isEmpty()) {
                showErrorAlert("Please fill the name");
            } else {
                showErrorAlert("Please fill the time");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void deleteOnClick() {
        if (selectedActivity != null) {
            if (!selectedActivity.getTeamName().equals("")) {
                teams.deleteTeam(event,selectedActivity.getTeamName());
            }

            selectedActivity.deleteActivity();
            teamListDatasource.writeData(teams);
            activityListDatasource.writeData(activityList);
            updateSchedule();
            showTable(activityList);
        } else {
            showErrorAlert("Please select activity");
        }
    }

    @FXML
    protected void backOnClick() {
        try {
            FXRouter.goTo("event-history", objectsSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showErrorAlert(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void nextOnClick() {
        try {
            FXRouter.goTo("create-team", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void OnMenuBarClick() throws IOException {
        slideAnimate = new TranslateTransition();
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
        slideAnimate = new TranslateTransition();
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
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        time = LocalDateTime.now().format(formatter);
        account.setTime(time);
        accountListDatasource.writeData(accountList);
        FXRouter.goTo("login-page");
    }

    private void loadTheme(Boolean theme) {
        if (theme) {
            loadTheme("st-theme.css");
        } else {
            loadTheme("dark-theme.css");
        }
    }
    private void loadTheme(String themeName) {
        if (parent != null) {
            cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
