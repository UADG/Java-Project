package cs211.project.controllers;

import cs211.project.models.*;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CreateTeamController {
    @FXML public TableView<Activity> activityTableView;
    @FXML public Label nameLabel;
    @FXML public Label timeStartLabel;
    @FXML public Label timeStopLabel;
    @FXML public TextField teamNameTextField;
    @FXML public TextField numberOfTeamMemberTextField;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private BorderPane bPane;
    @FXML private AnchorPane parent;
    private Object[] objects;
    private Object[] objectsSend;
    private Datasource<AccountList> accountListDatasource = new AccountListDatasource("data", "user-info.csv");
    private AccountList accountList = accountListDatasource.readData();
    private Account account;
    public ActivityList list;
    public String eventName;
    public Event event;
    public Activity selectedActivity;
    private Boolean isLightTheme;

    public void initialize(){
        clearInfo();
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        event = (Event) objects[1];
        isLightTheme = (Boolean) objects[2];

        objectsSend = new Object[2];
        objectsSend[0] = account;
        objectsSend[1] = isLightTheme;
        loadTheme(isLightTheme);

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
            if(activity.getTeamName().equals("")){
                activityTableView.getItems().add(activity);
            }
        }
    }

    public void createTeam(){
        if(selectedActivity != null){
            String teamName = teamNameTextField.getText();
            String numberStr = numberOfTeamMemberTextField.getText();

            boolean found = false;
            TeamList teamList = event.loadTeamInEvent();
            for(Team team : teamList.getTeams()) if(team.getTeamName().equals(teamName)) found = true;
            if(!teamName.equals("")&&!numberStr.equals("")){
                int number = Integer.parseInt(numberStr);
                if(number > 0){
                    if(!found){
                        try {

                            Team team = new Team(teamName, number, event.getEventName());
                            team.createTeamInCSV();
                            selectedActivity.updateTeamInActivity(team);
                            list = event.loadActivityInEvent();
                            showTable(list);
                            teamNameTextField.clear();
                            numberOfTeamMemberTextField.clear();
                        }catch (NumberFormatException e){
                            showErrorAlert("Number of people must be number");
                        }
                    }else{
                        showErrorAlert("This team name already exist");
                    }
                }else{
                    showErrorAlert("Number of people must more than zero");
                }
            }else{
                showErrorAlert("Must fill all information before create team");
            }
        }else{
            showErrorAlert("please select some activity before create team");
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
    protected void onBackClick(){
        try {
            FXRouter.goTo("create-schedule", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onNextClick(){
        try {
            FXRouter.goTo("event-history", objectsSend);
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
    private void loadTheme(Boolean theme) {
        if (theme) {
            loadTheme("st-theme.css");
        } else {
            loadTheme("dark-theme.css");
        }
    }
    private void loadTheme(String themeName) {
        if (parent != null) {
            String cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
