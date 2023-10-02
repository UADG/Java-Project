package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
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
import java.time.LocalTime;

public class CreateTeamController {
    private Event events = (Event) FXRouter.getData();
    private Datasource<AccountList> accountListDatasource = new AccountListDatasource("data", "user-info.csv");
    private AccountList accountList = accountListDatasource.readData();
    private Account account = accountList.findAccountByUsername(events.getEventManager());
    @FXML public TableView activityTableView;
    @FXML public Label nameLabel;
    @FXML public Label timeStartLabel;
    @FXML public Label timeStopLabel;
    @FXML public TextField teamNameTextField;
    @FXML public TextField numberOfTeamMemberTextField;
    @FXML public Label errorLabel;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private Button adminButton;
    @FXML private BorderPane bPane;
    public ActivityList list;
    public String eventName;
    public Event event;
    public Activity selectedActivity;
    public void initialize(){
        errorLabel.setText("");
        clearInfo();
        eventName = (String) FXRouter.getData();
        event = new Event(eventName);
        event.loadEventInfo();
        list = event.loadActivityInEvent();
        showTable(list);
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
        bPane.setVisible(false);
        slide.setTranslateX(-200);
        adminButton.setVisible(false);
        if(account.isAdmin(account.getRole())){
            adminButton.setVisible(true);
        }
    }

    public void clearInfo(){
        nameLabel.setText("");
        timeStopLabel.setText("");
        timeStopLabel.setText("");
    }

    public void showInfo(Activity activity){
        nameLabel.setText(activity.getActivityName());
        timeStartLabel.setText(activity.getStartTimeActivity());
        timeStopLabel.setText(activity.getEndTimeActivity());
    }

    public void showTable(ActivityList list){
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Activity Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, String> dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));


        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));


        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);

        activityTableView.getItems().clear();


        for (Activity activity: list.getActivities()) {
            if(activity.getTeamName().equals("null")){
                activityTableView.getItems().add(activity);
            }
        }
    }

    public void createTeam(){
        if(selectedActivity != null){
            String teamName = teamNameTextField.getText();
            String numberStr = numberOfTeamMemberTextField.getText();
            if(!teamName.equals("")&&!numberStr.equals("")){
                try {
                    int number = Integer.parseInt(numberStr);
                    Team team = new Team(teamName, number, eventName);
                    team.createTeamInCSV();
                    selectedActivity.updateTeamInActivity(team);
                    list = event.loadActivityInEvent();
                    showTable(list);
                    teamNameTextField.clear();
                    numberOfTeamMemberTextField.clear();
                }catch (NumberFormatException e){
                    errorLabel.setText("Number of people must be number");
                }
            }else{
                errorLabel.setText("Must fill all information before create team");
            }
        }else{
            errorLabel.setText("please select some activity before create team");
        }

    }

    @FXML
    protected void onBackClick(){
        try {
            FXRouter.goTo("create-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onNextClick(){
        try {
            FXRouter.goTo("event-history");
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
