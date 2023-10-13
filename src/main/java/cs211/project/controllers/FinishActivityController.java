package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.*;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class FinishActivityController {
    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeStartLabel;
    @FXML
    private Label timeStopLabel;
    @FXML
    private Label dateLabel;
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
    private ActivityListFileDatasource data;
    private AccountList accountList;
    private ActivityList list;
    private Account account;
    private Event selectedEvent;
    private Activity selectedActivity;
    private Object[] objectsSend;
    private Object[] objects;
    private TranslateTransition slideAnimate;
    private TableColumn<Activity, String> activityNameColumn;
    private TableColumn<Activity, String> dateActivityColumn;
    private TableColumn<Activity, LocalTime> startTimeActivityColumn;
    private TableColumn<Activity, LocalTime> endTimeActivityColumn;
    private DateTimeFormatter formatter;
    private Boolean isLightTheme;
    private String time;
    private String cssPath;

    @FXML
    public void initialize(){
        clearInfo();
        updateData();
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
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");

        showTable(selectedEvent.loadActivityInEvent());

        bPane.setVisible(false);
        slide.setTranslateX(-200);

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

    public void showTable(ActivityList list){
        activityNameColumn = new TableColumn<>("Activity Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

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


        for (Activity activity: list.getActivities()) {
            if(activity.getStatus().equals("0")) activityTableView.getItems().add(activity);
        }
    }

    public void showInfo(Activity activity){
        nameLabel.setText(activity.getActivityName());
        timeStartLabel.setText(activity.getStartTimeActivity());
        timeStopLabel.setText(activity.getEndTimeActivity());
        dateLabel.setText(activity.getDate());
    }

    public void clearInfo(){
        nameLabel.setText("");
        timeStopLabel.setText("");
        timeStartLabel.setText("");
        dateLabel.setText("");
    }

    public void updateData(){
        data = new ActivityListFileDatasource("data", "activity-list.csv");
    }

    public void finishActivity(){
        list = data.readData();
        list.findActivityInEvent(selectedActivity.getEventName());
        list.setActivityStatus(selectedActivity.getActivityName(),"1");
        data.writeData(list);
        updateData();
        list = data.readData();
        list.findActivityInEvent(selectedEvent.getEventName());
        showTable(selectedEvent.loadActivityInEvent());
    }


    @FXML
    protected void onBackClick() {
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
