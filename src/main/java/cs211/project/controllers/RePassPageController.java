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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RePassPageController {

    @FXML private PasswordField passwordOld;
    @FXML private PasswordField passwordNew;
    @FXML private Label usernameLabel;
    @FXML private Label myText;
    @FXML private Pane myRectangle;
    @FXML private Label errorLabel;
    @FXML private Label errorLabel1;
    @FXML private ImageView imageView;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private BorderPane bPane;
    @FXML private HBox hBox;
    @FXML private Button backButton;
    @FXML private AnchorPane parent;
    private Object[] objects;
    private Account accounts;
    private Boolean isLightTheme;
    Datasource<AccountList> accountListDataSource = new AccountListDatasource("data","user-info.csv");
    AccountList accountList = accountListDataSource.readData();
    private Account account;
    @FXML
    public void initialize(){
        objects = (Object[]) FXRouter.getData();
        accounts = (Account) objects[0];
        isLightTheme = (Boolean) objects[1];
        loadTheme(isLightTheme);

        account = accountList.findAccountByUsername(accounts.getUsername());
        if(account.getRole().equals("admin")){
            menuButton.setVisible(false);
            backButton.setLayoutX(14);
            backButton.setLayoutY(18);
        }
        if(!account.getPictureURL().equals("/images/default-profile.png")){
            imageView.setImage(new Image("file:"+account.getPictureURL(), true));
        }else {
            imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        usernameLabel.setText(account.getUsername());
        myText.setVisible(false);
        myRectangle.setVisible(false);
        errorLabel.setVisible(false);
        errorLabel1.setVisible(false);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
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
    protected void onBackClick() throws IOException {
        System.out.println(account.getRole());
        if (account.getRole().equals("user")) {
            FXRouter.goTo("profile-setting", objects);
        } else {
            FXRouter.goTo("user-status", objects);
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
        FXRouter.goTo("events-list", objects);
    }
    @FXML
    public void onProfileClick() throws IOException {
        FXRouter.goTo("profile-setting", objects);
    }
    @FXML
    public void onCreateEvent() throws IOException {
        FXRouter.goTo("create-event", objects);
    }
    @FXML
    public void onJoinHistory() throws IOException {
        FXRouter.goTo("joined-history", objects);
    }
    @FXML
    public void onEventHis() throws IOException {
        FXRouter.goTo("event-history", objects);
    }
    @FXML
    public void onPartiSchedule() throws IOException {
        FXRouter.goTo("participant-schedule", objects);
    }
    @FXML
    public void onTeamSchedule() throws IOException {
        FXRouter.goTo("team-schedule", objects);
    }
    @FXML
    public void onComment() throws IOException {
        FXRouter.goTo("comment-activity", objects);
    }
    @FXML
    public void onLogOutButton() throws IOException {
        Datasource<AccountList> accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        AccountList accountList = accountListDatasource.readData();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = LocalDateTime.now().format(formatter);
        account.setTime(time);
        Datasource<AccountList> dataSource = new AccountListDatasource("data","user-info.csv");
        dataSource.writeData(accountList);
        FXRouter.goTo("login-page");
    }

    private void loadTheme(Boolean theme) {
        if (theme) {
            loadTheme("st-theme.css");
        } else {
            loadTheme("dark-theme.css");
        }
    }

    private void loadTheme(String themeName) {
        if (parent != null) {
            String cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
