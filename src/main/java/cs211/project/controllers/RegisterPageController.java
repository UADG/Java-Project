package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.User;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.UserList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;

public class RegisterPageController {
    Datasource<AccountList> accountListDatasource = new AccountListDatasource("data","user-info.csv");
    AccountList accountList = accountListDatasource.readData();
    @FXML TextField nameText;
    @FXML TextField usernameText;
    @FXML TextField passText;
    @FXML TextField confirmText;
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("login-page");
    }
    public void onConfirmClick(ActionEvent event) throws IOException {
        String username = usernameText.getText();
        String name = nameText.getText();
        String pass = passText.getText();
        String confirmPass = confirmText.getText();
        Account account = accountList.findAccountByUsername(username);
        if(account==null){
            if(!username.equals("")&&!name.equals("")&&!pass.equals("")){
                if(pass.equals(confirmPass)){
                    accountList.addNewAccount("user",username,pass,name, "time", "PIC","unban");
                    Datasource<AccountList> dataSource = new AccountListDatasource("data","user-info.csv");
                    dataSource.writeData(accountList);
                    FXRouter.goTo("login-page");
                }else {
                    showErrorAlert("Confirm password wrong. Please try again.");
                }
            }else {
                showErrorAlert("Please fill all the information.");
            }
        }else {
            showErrorAlert("This username already in used.");
        }
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        clearText();
    }
    public void clearText(){
        nameText.setText("");
        usernameText.setText("");
        passText.setText("");
        confirmText.setText("");
    }
}
