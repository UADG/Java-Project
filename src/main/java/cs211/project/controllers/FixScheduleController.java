package cs211.project.controllers;

import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalTime;

public class FixScheduleController {
    @FXML Label constantTeamLabel;
    @FXML Label nameLabel;
    @FXML Label timeStartLabel;
    @FXML Label timeStopLabel;
    @FXML ComboBox chooseTeam;
    @FXML RadioButton chooseRoleTeam;
    @FXML RadioButton chooseRoleSingleParticipant;
    @FXML TableView activityTableView;
    @FXML ComboBox chooseOperator;
    private Event selectedEvent;
    private Team team;
    private boolean notFirst;
    private ActivityList list;
    private Activity selectedActivity;
    private String operator;


    @FXML
    public void initialize(){
        clearInfo();
        selectedEvent = (Event) FXRouter.getData();
        String[] op = {"add activity","delete activity"};
        notFirst = false;
        list = selectedEvent.loadActivityInEvent();
        chooseRoleSingleParticipant.setSelected(true);
        chooseTeam.getItems().addAll(selectedEvent.loadTeamInEvent().getTeams());
        chooseOperator.getItems().addAll(op);
        setChooseTeamVisible(false);
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
            chooseRoleSingleParticipant.setSelected(false);
            setChooseTeamVisible(true);
            if(notFirst){
                chooseWhichTeam();
            }


        }
        if(chooseRoleSingleParticipant.isSelected()){
            chooseRoleTeam.setSelected(false);
            setChooseTeamVisible(false);
            clearInfo();
            showParticipant();

        }
    }

    public void setChooseTeamVisible(boolean bool){
        chooseTeam.setVisible(bool);
        constantTeamLabel.setVisible(bool);
    }

    public void chooseWhichTeam(){
        if(team == null && operator == null){
            team = (Team) chooseTeam.getSelectionModel().getSelectedItem();
            notFirst = true;
        }else{
            team = (Team) chooseTeam.getSelectionModel().getSelectedItem();
            notFirst = true;
            showActivity();
        }

    }

    public void chooseWhichOperator(){
        operator = (String) chooseOperator.getSelectionModel().getSelectedItem();
        if(team != null) showActivity();
    }

    public void showActivity(){

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


        if(operator.equals("add activity")) {
            for(Activity activity: list.getActivities()){
                if(activity.getTeamName().equals(""))activityTableView.getItems().add(activity);
            }
        }else if(operator.equals("delete activity")){
            for(Activity activity: list.getActivities()){
                if(activity.getTeamName().equals(team.getTeamName()))activityTableView.getItems().add(activity);
            }
        }
    }

    public void updateDataToTarget(){
        if(selectedActivity != null) {
            if(operator.equals("add activity")){
                selectedActivity.updateTeamInActivity(team);
            }else if(operator.equals("delete activity")){
                selectedActivity.updateTeamInActivity(null);
            }
            list = selectedEvent.loadActivityInEvent();
            showActivity();
        }
    }

    public void showParticipant(){
        activityTableView.getItems().clear();
        activityTableView.getItems().addAll();
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
            FXRouter.goTo("event-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
