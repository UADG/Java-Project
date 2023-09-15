package cs211.project.controllers;

import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.Staff;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.StaffList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.ActivityListFileDatasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamListFileDatasource;
import cs211.project.services.TeamListHardCode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class BanAllController {
    @FXML Label constantTeamLabel;
    @FXML ComboBox chooseTeam;
    @FXML Label nameLabel;
    @FXML RadioButton chooseRoleTeam;
    @FXML RadioButton chooseRoleSingleParticipant;
    @FXML ListView eventMemberListView;
    private Team team;
    private Staff selectedStaff;
    private TeamList list;
    private boolean notFirst;
    private Event selectedEvent;
    private TeamListFileDatasource data;
    @FXML
    public void initialize(){
        selectedEvent = (Event) FXRouter.getData();
        clearInfo();
        updateData();
        list = selectedEvent.loadTeamInEvent();
        notFirst = false;
        chooseRoleSingleParticipant.setSelected(true);
        chooseTeam.getItems().addAll(list.getTeams());
        setChooseTeamVisible(false);
}
    @FXML
    public void clearInfo(){
        nameLabel.setText("");
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

    public void updateData(){
        data = new TeamListFileDatasource("data","team.csv");
    }

    public void setChooseTeamVisible(boolean bool){
        chooseTeam.setVisible(bool);
        constantTeamLabel.setVisible(bool);
    }

    public void chooseWhichTeam(){
        team = (Team) chooseTeam.getSelectionModel().getSelectedItem();
        notFirst = true;
        showStaff();
    }

    public void showStaff(){
        eventMemberListView.getItems().clear();
        eventMemberListView.getItems().addAll(team.getStaffThatNotBan().getStaffList());
    }

    public void showParticipant(){
        eventMemberListView.getItems().clear();
        eventMemberListView.getItems().addAll();
    }

    public void selectedTeam(){
        chooseRoleTeam.setSelected(true);
        chooseRoleSingleParticipant.setSelected(false);
        chooseRole();
        eventMemberListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
            @Override
            public void changed(ObservableValue<? extends Staff> observable, Staff oldValue, Staff newValue) {
                if (newValue == null) {
                    clearInfo();
                    selectedStaff = null;
                } else {
                    nameLabel.setText(newValue.getName());
                    selectedStaff =  newValue;
                }
            }
        });
    }

    public void banTarget(){
        if (team != null && selectedStaff != null) {
            updateData();
            team.banStaffInTeam(selectedStaff.getId());
            data.updateStaffInTeam(team.getTeamName(),selectedStaff,"-");
            showStaff();
        }
    }


    public void selectedParticipant(){
        chooseRoleSingleParticipant.setSelected(true);
        chooseRoleTeam.setSelected(false);
        chooseRole();
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
