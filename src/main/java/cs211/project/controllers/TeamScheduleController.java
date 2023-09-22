package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.EventList;
import cs211.project.services.ActivityListFileDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalTime;

public class TeamScheduleController {
    @FXML private ComboBox teamComboBox;

    @FXML private TableView<Activity> activityTableView;
    private ActivityList activityList;
    private Team team;
    private String eventName;
    private  Datasource<ActivityList> datasource;
    @FXML
    public void initialize(){
        datasource = new ActivityListFileDatasource("data", "activity-list.csv");
        activityList = datasource.readData();
        Account account = (Account) FXRouter.getData();
        team = new Team("",1,1,"");
        teamComboBox.getItems().addAll(team.getUserInTeam(account.getId()));
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

        TableColumn<Activity, LocalTime> teamColumn = new TableColumn<>("team");
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);
        activityTableView.getColumns().add(teamColumn);

        activityTableView.getItems().clear();

        for (Activity activity: activityList.getActivities()) {
            activityTableView.getItems().add(activity);
        }
    }


    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onSearchClick() {
        eventName = (String) teamComboBox.getValue();
        updateSchedule();
        showTable(activityList);
    }
    private void updateSchedule(){
        activityList = datasource.readData();
        activityList.findActivityInEvent(eventName);
    }
}
