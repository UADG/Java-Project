package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.StaffList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;

public class BanAllController {
    @FXML Label constantTeamLabel;
    @FXML ComboBox chooseTeam;
    @FXML Label nameLabel;
    @FXML RadioButton chooseRoleTeam;
    @FXML RadioButton chooseRoleSingleParticipant;
    @FXML ListView staffListView;
    @FXML ListView userListView;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private Button adminButton;
    @FXML private BorderPane bPane;
    Account account = (Account) FXRouter.getData();
    private Team team;
    private Staff selectedStaff;
    private Account selectedUser;
    private TeamList list;
    private AccountList users;
    private boolean notFirst;
    private Event selectedEvent;
    private TeamListFileDatasource data;
    private Datasource<AccountList> dataAccount;
    private BanListFileDatasource banPath;
    @FXML
    public void initialize(){
        selectedEvent = (Event) FXRouter.getData();
        clearInfo();
        updateData();
        list = selectedEvent.loadTeamInEvent();
        users = dataAccount.readData();
        notFirst = false;
        selectedParticipant();
        chooseRoleSingleParticipant.setSelected(true);
        showParticipant();
        chooseTeam.getItems().addAll(list.getTeams());
        setChooseTeamVisible(false);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
        adminButton.setVisible(false);
        if(account.isAdmin(account.getRole())){
            adminButton.setVisible(true);
        }
}
    @FXML
    public void clearInfo(){
        nameLabel.setText("");
    }

    @FXML
    public void chooseRole(){

        if(chooseRoleTeam.isSelected()){
            userListView.setVisible(false);
            staffListView.setVisible(true);
            chooseRoleSingleParticipant.setSelected(false);
            setChooseTeamVisible(true);
            if(notFirst){
                chooseWhichTeam();
                System.out.println("1");
            }


        }
        if(chooseRoleSingleParticipant.isSelected()){
            userListView.setVisible(true);
            staffListView.setVisible(false);
            chooseRoleTeam.setSelected(false);
            setChooseTeamVisible(false);
            clearInfo();
            showParticipant();
            System.out.println("2");

        }
    }

    public void updateData(){
        data = new TeamListFileDatasource("data","team.csv");
        dataAccount = new UserEventListFileDatasource("data", "user-joined-event.csv");
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
        staffListView.getItems().clear();
        staffListView.getItems().addAll(team.getStaffThatNotBan().getStaffList());
    }

    public void showParticipant(){
        userListView.getItems().clear();
        for(Account account: users.getAccount()){
            System.out.println(account.getName());
            if(account.isEventName(selectedEvent.getEventName()))
            {
                userListView.getItems().add(account);
            }
        }
    }

    public void selectedTeam(){
        System.out.println("This is from selectedTeam");
        chooseRoleTeam.setSelected(true);
        chooseRoleSingleParticipant.setSelected(false);
        chooseRole();

        staffListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
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

        System.out.println("This is from selectedParticipant");
        chooseRoleSingleParticipant.setSelected(true);
        chooseRoleTeam.setSelected(false);
        chooseRole();
        userListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {

            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {
                if (newValue == null) {
                    clearInfo();
                    selectedUser = null;
                } else {
                    nameLabel.setText(newValue.getName());
                    selectedUser =  newValue;
                    System.out.println("click ra ja");
                    System.out.println(selectedUser.getName());
                }
            }
        });
    }
    public void banTarget(){
        if (team != null && selectedStaff != null) {
            System.out.println("This is from Ban Team");
            updateData();
            team.banStaffInTeam(selectedStaff.getId());
            data.updateStaffInTeam(selectedEvent.getEventName(),team.getTeamName(),selectedStaff,"-");
            banPath.writeData(selectedStaff);
            banPath.updateEventToId(selectedStaff.getId(),selectedStaff.getName(),"+");
            showStaff();
        } else if (selectedUser != null) {
            System.out.println("This is from Ban selectedParticipant");
            updateData();
            System.out.println(selectedEvent.getEventName());
            selectedUser.deleteUserEventName(selectedEvent.getEventName());
            System.out.println(selectedEvent.getEventName());
            dataAccount.writeData(users);
            showParticipant();
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
