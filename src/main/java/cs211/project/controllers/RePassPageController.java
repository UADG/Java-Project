package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

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
}
