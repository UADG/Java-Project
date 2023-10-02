package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class RePassPageController {

    @FXML private PasswordField passwordOld;
    @FXML private PasswordField passwordNew;
    @FXML private Label usernameLabel;
    @FXML private Label myText;
    @FXML private Rectangle myRectangle;
    @FXML private Label errorLabel;
    @FXML private Label errorLabel1;
    @FXML private ImageView imageView;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private Button adminButton;
    @FXML private BorderPane bPane;
    private Account accounts = (Account) FXRouter.getData();
    Datasource<AccountList> accountListDataSource = new AccountListDatasource("data","user-info.csv");
    AccountList accountList = accountListDataSource.readData();
    private Account account = accountList.findAccountByUsername(accounts.getUsername());
    @FXML
    public void initialize(){
        Image image = new Image(getClass().getResource("/images/default-profile.png").toExternalForm());
        imageView.setImage(image);
        usernameLabel.setText(account.getUsername());
        myText.setVisible(false);
        myRectangle.setVisible(false);
        errorLabel.setVisible(false);
        errorLabel1.setVisible(false);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
        adminButton.setVisible(false);
        if(account.isAdmin(account.getRole())){
            adminButton.setVisible(true);
        }
    }
    @FXML
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("profile-setting", account);
    }
    @FXML
    public void onConfirmClick(ActionEvent event) throws IOException {
        String oldPass = passwordOld.getText();
        String newPass = passwordNew.getText();
        if(account.getPassword().equals(oldPass) && !newPass.equals("")){
            account.setPassword(newPass);
            accountListDataSource.writeData(accountList);
            clearText();
            myText.setVisible(true);
            myRectangle.setVisible(true);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            myText.setVisible(false);
                            myRectangle.setVisible(false);
                        }
                    },
                    1000 // 1 second
            );
        }else if(!newPass.equals("")||oldPass.equals("")){
            clearText();
            errorLabel.setVisible(true);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            errorLabel.setVisible(false);
                        }
                    },
                    1000 // 1 second
            );
        }else {
            clearText();
            errorLabel1.setVisible(true);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            errorLabel1.setVisible(false);
                        }
                    },
                    1000 // 1 second
            );
        }
    }
    public void clearText(){
        passwordOld.setText("");
        passwordNew.setText("");
    }
    @FXML
    protected void onBackClick() throws IOException{
        FXRouter.goTo("profile-setting");
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
