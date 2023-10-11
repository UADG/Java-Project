package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Team;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.*;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
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

public class TeamScheduleController {
    @FXML
    private ComboBox teamComboBox;
    @FXML
    private AnchorPane parent;
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
    private Datasource<ActivityList> activityListDatasource;
    private Datasource<AccountList> accountListDatasource;
    private ActivityList activityList;
    private AccountList accountList;
    private Object[] objects;
    private Account account;
    private Team team;
    private TableColumn<Activity, String> activityNameColumn;
    private TableColumn<Activity, String> dateActivityColumn;
    private TableColumn<Activity, LocalTime> startTimeActivityColumn;
    private TableColumn<Activity, LocalTime> endTimeActivityColumn;
    private TableColumn<Activity, LocalTime> teamColumn;
    private TableColumn<Activity, String> statusColumn;
    private TranslateTransition slideAnimate;
    private DateTimeFormatter formatter;
    private String time;
    private String eventName;
    private String cssPath;
    private Boolean isLightTheme;
    private int status;

    @FXML
    public void initialize(){
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        isLightTheme = (Boolean) objects[1];
        loadTheme(isLightTheme);
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");
        accountList = accountListDatasource.readData();
        activityList = activityListDatasource.readData();
        if(isLightTheme){
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
        }else{
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-dark-theme.png").toExternalForm()));
        }
        team = new Team("",1,1,"");
        teamComboBox.getItems().addAll(team.getUserInTeam(account.getId()));
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }
    private void showTable(ActivityList activityList) {
        activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

        teamColumn = new TableColumn<>("team");
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(cellData -> {
            status = Integer.parseInt(cellData.getValue().getStatus());
            if (status == 1) {
                return new SimpleStringProperty("Finish");
            } else {
                return new SimpleStringProperty("Still Organize");
            }
        });
        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);
        activityTableView.getColumns().add(statusColumn);
        activityTableView.getItems().clear();

        for (Activity activity: activityList.getActivities()) {
            if(activity.getStatus().equals("0")) {
                activityTableView.getItems().add(activity);
            }
        }
        for (Activity activity: activityList.getActivities()) {
            if(activity.getStatus().equals("1"))
                activityTableView.getItems().add(activity);
        }
    }

    @FXML
    protected void onSearchClick() {
        eventName = (String) teamComboBox.getValue();
        updateSchedule();
        showTable(activityList);
    }
    private void updateSchedule(){
        activityList = activityListDatasource.readData();
        activityList.findActivityInEvent(eventName);
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
        FXRouter.goTo("events-list", objects);
    }
    @FXML
    public void onProfileClick() throws IOException {
        FXRouter.goTo("profile-setting", objects);
    }
    @FXML
    public void onCreateEvent() throws IOException {
        FXRouter.goTo("create-event", objects);
    }
    @FXML
    public void onJoinHistory() throws IOException {
        FXRouter.goTo("joined-history", objects);
    }
    @FXML
    public void onEventHis() throws IOException {
        FXRouter.goTo("event-history", objects);
    }
    @FXML
    public void onPartiSchedule() throws IOException {
        FXRouter.goTo("participant-schedule", objects);
    }
    @FXML
    public void onTeamSchedule() throws IOException {
        FXRouter.goTo("team-schedule", objects);
    }
    @FXML
    public void onComment() throws IOException {
        FXRouter.goTo("comment-activity", objects);
    }
    @FXML
    public void onLogOutButton() throws IOException {
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");
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
