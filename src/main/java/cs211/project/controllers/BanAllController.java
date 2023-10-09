package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.*;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BanAllController {
    @FXML Label constantTeamLabel;
    @FXML ComboBox<Team> chooseTeam;
    @FXML Label nameLabel;
    @FXML RadioButton chooseRoleTeam;
    @FXML RadioButton chooseRoleSingleParticipant;
    @FXML ListView<Staff> staffListView;
    @FXML ListView<Account> userListView;
    @FXML private AnchorPane parent;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private BorderPane bPane;
    private Team team;
    private Staff selectedStaff;
    private Account selectedUser;
    private TeamList list;
    private boolean notFirst;
    private Event selectedEvent;
    private TeamListFileDatasource data;
    private Datasource<AccountList> accountListDatasource;
    private Datasource<AccountList> banUserDatasource;
    private AccountList accountList;
    private AccountList banUserList;

    private BanListFileDatasource banPath;
    private Account account;
    private ThemeDatasource themeDatasource = new ThemeDatasource("data", "theme.csv");
    private String theme = themeDatasource.read();
    @FXML
    public void initialize(){
        loadTheme(theme);
        Object[] objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        selectedEvent = (Event) objects[1];
        clearInfo();
        updateData();
        accountList = accountListDatasource.readData();
        banUserList = banUserDatasource.readData();
        list = selectedEvent.loadTeamInEvent();
        notFirst = false;
        selectedParticipant();
        chooseRoleSingleParticipant.setSelected(true);
        showParticipant();
        chooseTeam.getItems().addAll(list.getTeams());
        setChooseTeamVisible(false);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
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
        banPath = new BanListFileDatasource("data", "ban-staff-list.csv");
        accountListDatasource = new UserEventListFileDatasource("data", "user-joined-event.csv");
        banUserDatasource = new UserEventListFileDatasource("data", "ban-user.csv");
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
        for(Account account: accountList.getAccount()){
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
            updateData();
            team.banStaffInTeam(selectedStaff.getId());
            data.updateStaffInTeam(selectedEvent.getEventName(),team.getTeamName(),selectedStaff,"-");
            banPath.writeData(selectedStaff);
            banPath.updateEventToId(selectedStaff.getId(),selectedStaff.getName(),"+");
            showStaff();
        } else if (selectedUser != null) {
            updateData();
            selectedUser.deleteUserEventName(selectedEvent.getEventName());
            banUserList.addUserEvent(selectedUser.getId(),selectedEvent.getEventName());
            banUserDatasource.writeData(banUserList);
            accountListDatasource.writeData(accountList);
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
    protected void backOnClick(){
        try {
            FXRouter.goTo("event-history", account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private void loadTheme(String themeName) {
        if (parent != null) {
            String cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
