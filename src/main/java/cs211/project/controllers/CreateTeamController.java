package cs211.project.controllers;

import cs211.project.models.Activity;
import cs211.project.models.Staff;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamListFileDatasource;
import cs211.project.services.TeamListHardCode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalTime;

public class CreateTeamController {
    @FXML public TableView activityTableView;
    @FXML public Label nameLabel;
    @FXML public Label timeStartLabel;
    @FXML public Label timeStopLabel;
    @FXML public TextField teamNameTextField;
    @FXML public TextField numberOfTeamMemberTextField;
    @FXML public Label errorLabel;
    public ActivityList list;
    public Activity selectedActivity;
    public void initialize(){
        errorLabel.setText("");
        clearInfo();
        list = (ActivityList) FXRouter.getData();
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
            if(activity.getTeam() == null){
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
                    Team team = new Team(teamName, number);
                    team.createTeamInCSV();
                    selectedActivity.updateTeamInActivity(team);
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
}
