package cs211.project.controllers;

import cs211.project.services.AccountHardCode;
import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomePageController {
    @FXML Button adminButton;
    private AccountList accountList;
    public void initialize() {
        adminButton.setVisible(false);
        AccountHardCode data = new AccountHardCode();
        accountList = data.readData();
        String user = (String) FXRouter.getData();
        Account account = accountList.findAccountByUsername(user);
        if(account.checkRole(account.getId()).equals("admin")){
            adminButton.setVisible(true);
        }
    }
    @FXML
    protected void onCreateClick() {
        try {
            FXRouter.goTo("create-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onJoinClick() {
        try {
            FXRouter.goTo("events-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onProfileClick() {
        try {
            FXRouter.goTo("profile-setting");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onJoinedHistoryClick() {
        try {
            FXRouter.goTo("joined-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onParticipantScheduleClick() {
        try {
            FXRouter.goTo("participant-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onTeamScheduleClick() {
        try {
            FXRouter.goTo("team-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onEventHistoryClick() {
        try {
            FXRouter.goTo("event-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onUserButtClick() {
        try {
            FXRouter.goTo("user-status");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onCommentClick() {
        try {
            FXRouter.goTo("comment-activity");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
