package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.Team;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FixScheduleController {
    @FXML
    private Label constantTeamLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeStartLabel;
    @FXML
    private Label timeStopLabel;
    @FXML
    private ComboBox chooseTeam;
    @FXML
    private RadioButton chooseRoleTeam;
    @FXML
    private RadioButton chooseRoleSingleParticipant;
    @FXML
    private TableView activityTableView;
    @FXML
    private ComboBox chooseOperator;
    @FXML
    private AnchorPane slide;
    @FXML
    private Button menuButton;
    @FXML
    private BorderPane bPane;
    @FXML
    private AnchorPane parent;
    @FXML
    private ImageView logoImageView;
    private Datasource<AccountList> accountListDatasource;
    private Datasource<ActivityList> activityListDatasource;
    private AccountList accountList;
    private ActivityList activityList;
    private Account account;
    private Event selectedEvent;
    private Team team;
    private Activity selectedActivity;
    private Object[] objects;
    private Object[] objectsSend;
    private TranslateTransition slideAnimate;
    private TableColumn<Activity, String> activityNameColumn;
    private TableColumn<Activity, String> dateActivityColumn;
    private TableColumn<Activity, LocalTime> startTimeActivityColumn;
    private TableColumn<Activity, LocalTime> endTimeActivityColumn;
    private DateTimeFormatter formatter;
    private Boolean isLightTheme;
    private String[] op;
    private String participantName;
    private String operator;
    private String cssPath;
    private String time;

    @FXML
    public void initialize(){
        clearInfo();
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        accountList = accountListDatasource.readData();

        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        selectedEvent = (Event) objects[1];
        isLightTheme = (Boolean) objects[2];
        if(isLightTheme){
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
        }else{
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-dark-theme.png").toExternalForm()));
        }
        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;
        loadTheme(isLightTheme);

        op = new String[]{"add activity", "delete activity"};
        activityList = selectedEvent.loadActivityInEvent();
        chooseTeam.getItems().addAll(activityList.getParticipantInEvent());
        chooseRoleSingleParticipant.setSelected(true);
        chooseOperator.getItems().addAll(op);
        setChooseTeamVisible(false);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    @FXML
    public void clearInfo(){
        nameLabel.setText("");
        timeStopLabel.setText("");
        timeStartLabel.setText("");
    }

    @FXML
    public void chooseRole(){

        if(chooseRoleTeam.isSelected()){
            activityTableView.getItems().clear();
            chooseTeam.getItems().clear();
            chooseTeam.getItems().addAll(selectedEvent.loadTeamInEvent().getTeams());
            chooseRoleSingleParticipant.setSelected(false);
            setChooseTeamVisible(true);
            constantTeamLabel.setText("Team:");
            chooseWhichTeam();


        }
        if(chooseRoleSingleParticipant.isSelected()){
            activityTableView.getItems().clear();
            chooseTeam.getItems().clear();
            chooseTeam.getItems().addAll(activityList.getParticipantInEvent());
            chooseRoleTeam.setSelected(false);
            constantTeamLabel.setText("Parti:");
            clearInfo();
            if(chooseOperator.getValue() != null) {
                chooseWhichOperator();
                showActivity();
            }
        }
    }
    public void setChooseTeamVisible(boolean bool){
        chooseTeam.setVisible(bool);
        constantTeamLabel.setVisible(bool);
    }

    public void chooseWhichTeam(){
        if(chooseRoleTeam.isSelected()){
            if (team == null && operator == null) {
                team = (Team) chooseTeam.getSelectionModel().getSelectedItem();
            } else if(chooseTeam.getValue() != null){
                team = (Team) chooseTeam.getSelectionModel().getSelectedItem();
                if (operator != null) showActivity();
            }
        }
        else if (chooseRoleSingleParticipant.isSelected()){
            participantName = (String) chooseTeam.getSelectionModel().getSelectedItem();
        }
    }

    public void chooseWhichOperator(){
        operator = (String) chooseOperator.getSelectionModel().getSelectedItem();
        if(chooseRoleTeam.isSelected()){
            if(team != null && chooseTeam.getValue() != null) showActivity();
        } else if (chooseRoleSingleParticipant.isSelected()) {
            if(operator.equals("delete activity")){
                setChooseTeamVisible(false);
            }
            else {
                setChooseTeamVisible(true);
            }
            showActivity();
        }
    }

    public void showActivity(){
        activityNameColumn = new TableColumn<>("Activity Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);

        activityTableView.getItems().clear();

        if(chooseRoleTeam.isSelected()) {
            if (operator.equals("add activity")) {
                for (Activity activity : activityList.getActivities()) {
                    if (activity.getTeamName().equals("")) activityTableView.getItems().add(activity);
                }
            } else if (operator.equals("delete activity")) {
                for (Activity activity : activityList.getActivities()) {
                    if (activity.getTeamName().equals(team.getTeamName())) activityTableView.getItems().add(activity);
                }
            }
        }
        else {
            if (operator.equals("add activity")) {
                for (Activity activity : activityList.getActivities()) {
                    if (activity.getParticipantName().equals("")) {
                        activityTableView.getItems().add(activity);
                    }
                }
            } else if (operator.equals("delete activity")) {
                for (Activity activity : activityList.getActivities()) {
                    if (!activity.getParticipantName().isEmpty()) {
                        activityTableView.getItems().add(activity);
                    }
                }
            }
        }
    }

    public void updateDataToTarget(){
        if(chooseRoleTeam.isSelected()){
            if (selectedActivity != null) {
                if (operator.equals("add activity")) {
                    selectedActivity.updateTeamInActivity(team);
                } else if (operator.equals("delete activity")) {
                    selectedActivity.updateTeamInActivity(null);
                }
                activityList = selectedEvent.loadActivityInEvent();
                showActivity();
            }
        }
        else if (chooseRoleSingleParticipant.isSelected()){
            if (selectedActivity != null) {
                if (operator.equals("add activity")) {
                    selectedActivity.setParticipantName(participantName);
                } else if (operator.equals("delete activity")) {
                    selectedActivity.setParticipantName("");
                }
                activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");
                activityListDatasource.writeData(activityList);
                activityList = selectedEvent.loadActivityInEvent();
                showActivity();
            }
        }
    }


    public void selectedParticipant(){
        chooseRoleSingleParticipant.setSelected(true);
        chooseRoleTeam.setSelected(false);
        chooseRole();
    }

    public void showInfo(Activity activity){
        nameLabel.setText(activity.getActivityName());
        timeStartLabel.setText(activity.getStartDate()+" "+activity.getStartTimeActivity());
        timeStopLabel.setText(activity.getEndDate()+" "+activity.getEndTimeActivity());
    }

    public void selectedTeam(){
        chooseRoleTeam.setSelected(true);
        chooseRoleSingleParticipant.setSelected(false);
        chooseRole();
        activityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Activity>() {
            @Override
            public void changed(ObservableValue observable, Activity oldValue, Activity newValue) {
                if (newValue == null) {
                    clearInfo();
                    selectedActivity = null;
                } else {
                    showInfo(newValue);
                    selectedActivity = newValue;
                }
            }
        });
    }

    @FXML
    protected void backOnClick(){
        try {
            FXRouter.goTo("event-history", objectsSend);
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
        accountList = accountListDatasource.readData();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        time = LocalDateTime.now().format(formatter);
        account.setTime(time);
        accountList = accountListDatasource.readData();
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
