package cs211.project.controllers;

import cs211.project.models.Staff;
import cs211.project.models.Team;
import cs211.project.models.collections.StaffList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.FXRouter;
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
    private String name;
    private Team team;
    private Staff selectedStaff;
    private StaffList forShowInListView;
    private TeamListHardCode data;
    private TeamList list;
    private boolean notFirst;
    @FXML
    public void initialize(){
        clearInfo();
        notFirst = false;
        data = new TeamListHardCode();
        list = data.readData();
        chooseRoleSingleParticipant.setSelected(true);
        setChooseTeamVisible(false);
        chooseTeam.getItems().addAll(list.getTeams());


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
            team.banStaffInTeam(selectedStaff.getId());
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
