package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.StaffList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;
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
    private Account selectedUser;
    private TeamList list;
    private AccountList users;
    private boolean notFirst;
    private Event selectedEvent;
    private TeamListFileDatasource data;
    private BanListFileDatasource banPath;
    @FXML
    public void initialize(){
        selectedEvent = (Event) FXRouter.getData();
        clearInfo();
        updateData();
        list = selectedEvent.loadTeamInEvent();
        users = selectedEvent.loadUserInEvent();
        showParticipant();
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
        banPath = new BanListFileDatasource("data", "ban-staff-list.csv");
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
        eventMemberListView.getItems().addAll(users.getAccount());
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

    public void selectedParticipant(){
        chooseRoleSingleParticipant.setSelected(true);
        chooseRoleTeam.setSelected(false);
        chooseRole();
        eventMemberListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {
                if (newValue == null) {
                    clearInfo();
                    selectedUser = null;
                } else {
                    nameLabel.setText(newValue.getName());
                    selectedUser =  newValue;
                }
            }
        });
    }
    public void banTarget(){
        if (team != null && selectedStaff != null) {
            updateData();
            team.banStaffInTeam(selectedStaff.getId());
            data.updateStaffInTeam(selectedEvent.getEventName(),team.getTeamName(),selectedStaff,"-");
            banPath.writeData(selectedStaff);
            banPath.updateEventToId(selectedStaff.getId(),selectedStaff.getName(),"+");
            showStaff();
        } else if (selectedUser != null) {
            selectedUser.deleteUserEventName(selectedEvent.getEventName());
            showParticipant();
        }
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
