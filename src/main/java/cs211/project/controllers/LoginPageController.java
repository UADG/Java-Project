package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountHardCode;
import javafx.event.ActionEvent;
import java.io.IOException;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController {
    @FXML private TextField usernameText;
    @FXML private PasswordField passwordText;
    @FXML private Label invalidLabel;
    @FXML private Label wrongPassLabel;
    private AccountList accountList;
    @FXML
    public void initialize() {
        invalidLabel.setVisible(false);
        wrongPassLabel.setVisible(false);
        AccountHardCode data = new AccountHardCode();
        accountList = data.readData();
    }
    @FXML
    public void loginButt(ActionEvent event) throws IOException {
        String username = usernameText.getText();
        String password = passwordText.getText();
        Account account = accountList.findAccountByUsername(username);
        clearData();
        if(account != null || !usernameText.getText().equals("") || !passwordText.getText().equals("")){
            if(account.isPassword(password)){
                FXRouter.goTo("home-page", account);
            }else{
                wrongPassLabel.setVisible(true);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                wrongPassLabel.setVisible(false);
                            }
                        },
                        1000 // 1 sec
                );
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
}
