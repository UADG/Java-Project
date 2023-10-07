package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.EventList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class LoginPageController {
    @FXML private TextField usernameText;
    @FXML private PasswordField passwordText;
    @FXML private Label invalidLabel;
    @FXML private ImageView imageView;
    @FXML private HBox hBox;
    @FXML private AnchorPane parent;
    private AccountList accountList;
    private boolean isLightTheme = true;
    @FXML
    public void initialize() {
        invalidLabel.setVisible(false);
        hBox.setAlignment(Pos.CENTER);
        Datasource<AccountList> accountListDataSource = new AccountListDatasource("data", "user-info.csv");
        this.accountList = accountListDataSource.readData();
    }
    @FXML
    public void loginButt() throws IOException {
        String username = usernameText.getText().trim();
        String password = passwordText.getText().trim();
        Account account = accountList.findAccountByUsername(username);
        clearData();
        if(account != null || !usernameText.getText().equals("") || !passwordText.getText().equals("")){
            if(account.isUnban(account.getUserStatus())) {
                if (account.isPassword(password)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String time = LocalDateTime.now().format(formatter);
                    account.setTime(time);
                    FXRouter.goTo("events-list", account);
                    Datasource<AccountList> dataSource = new AccountListDatasource("data","user-info.csv");
                    dataSource.writeData(accountList);
                    try{
                    if(account.getRole().equals("admin")){
                        FXRouter.goTo("user-status",account);
                    }else{
                        FXRouter.goTo("events-list", account);
                    }}catch(IOException e){
                        showAlert("Program Error");
                    }
                } else {
                    invalidLabel.setText("Wrong password.");
                    invalidLabel.setVisible(true);
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    invalidLabel.setVisible(false);
                                }
                            },
                            1000 // 1 sec
                    );
                }
            }else {
                showAlert("Your account got banned.");
            }
        }else{
            invalidLabel.setVisible(true);
            new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        invalidLabel.setVisible(false);
                    }
                },
                1000 // 1 sec
        );
        }
    }
    @FXML
    public void registerLink(ActionEvent event) throws IOException {
        FXRouter.goTo("register-page");
    }
    public void clearData(){
        usernameText.setText("");
        passwordText.setText("");
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Banned");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void onChangeTheme() {
        if (isLightTheme) {
            loadTheme("dark-theme.css");
        } else {
            loadTheme("st-theme.css");
        }
        isLightTheme = !isLightTheme;
    }

    private void loadTheme(String themeName) {
        if (parent != null) {
            String cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
