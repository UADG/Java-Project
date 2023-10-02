package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController {
    @FXML private TextField usernameText;
    @FXML private PasswordField passwordText;
    @FXML private Label invalidLabel;
    private AccountList accountList;
    @FXML
    public void initialize() {
        invalidLabel.setVisible(false);
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
}
