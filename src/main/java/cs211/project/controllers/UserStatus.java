package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;

public class UserStatus {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ListView<Account> accountListView;
    @FXML
    private ImageView imageUserView;
    @FXML
    private HBox hBox;
    @FXML
    private AnchorPane parent;
    private Account account;
    private Boolean isLightTheme;
    private Account selectedAccount;
    private AccountList accountList;
    private Object[] objects;

    @FXML
    private void initialize() {
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        isLightTheme = (Boolean) objects[1];
        loadTheme(isLightTheme);

        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        clearDataInfo();
        Datasource<AccountList> accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        accountList = accountListDatasource.readData();
        accountList.sort();
        showList(accountList);
        accountListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearDataInfo();
                selectedAccount = null;
            } else {
                showInfo(newValue);
                selectedAccount = newValue;
            }
        });
    }

    public void showInfo(Account account) {
        Account user = accountList.findAccountByUsername(account.getUsername());
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getName());
        timeLabel.setText(user.getTime());
        if(!user.getPictureURL().equals("/images/default-profile.png")){
            imageUserView.setImage(new Image("file:"+user.getPictureURL(), true));
        }else {
            imageUserView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }
    }

    public void showList(AccountList accountList) {
        accountListView.getItems().clear();
        accountListView.getItems().addAll(accountList.getAccount());
        for (Account account : accountList.getAccount()) {
            if (account.isAdmin(account.getRole())) {
                removeAccount(account);
            }
        }

    }

    public void removeAccount(Account accountToRemove) {
        accountListView.getItems().remove(accountToRemove);
    }

    public void clearDataInfo () {
        usernameLabel.setText("");
        nameLabel.setText("");
        timeLabel.setText("");
        imageUserView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
    }
    @FXML
    private void onBackClick() throws IOException {
        FXRouter.goTo("login-page");
    }
    @FXML
    private void onChangePasswordClick() throws IOException {
        FXRouter.goTo("re-password", objects);
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
