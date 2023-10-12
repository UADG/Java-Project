package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
public class LoginPageController {
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Label invalidLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane parent;
    private Datasource<AccountList> accountListDataSource;
    private AccountList accountList;
    private Account account;
    private Object[] objects;
    private Alert alert;
    private DateTimeFormatter formatter;
    private boolean isLightTheme;
    private String cssPath;
    private String time;
    private String username;
    private String password;
    @FXML
    public void initialize() {
        isLightTheme = true;
        imageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));

        invalidLabel.setVisible(false);
        accountListDataSource = new AccountListDatasource("data", "user-info.csv");
        accountList = accountListDataSource.readData();
        loadTheme(isLightTheme);
    }
    @FXML
    public void loginButt() throws IOException {
        username = usernameText.getText().trim();
        password = passwordText.getText().trim();
        account = accountList.findAccountByUsername(username);
        objects = new Object[2];
        objects[0] = account;
        objects[1] = isLightTheme;

        clearData();
        if(account != null || !usernameText.getText().equals("") || !passwordText.getText().equals("")){
            if (account.isPassword(password)) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                time = LocalDateTime.now().format(formatter);
                account.setTime(time);
                accountListDataSource.writeData(accountList);
                    if(account.getRole().equals("admin")){
                        FXRouter.goTo("user-status", objects);
                    }else{
                        FXRouter.goTo("events-list", objects);
                    }

            } else {
                invalidLabel.setText("Wrong password.");
                invalidLabel.setVisible(true);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                invalidLabel.setVisible(false);
                            }},
                        1000
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
                1000
        );
        }
    }
    @FXML
    public void registerLink() throws IOException {
        FXRouter.goTo("register-page", isLightTheme);
    }
    public void clearData(){
        usernameText.setText("");
        passwordText.setText("");
    }
    @FXML
    protected void onChangeTheme() {
        if (isLightTheme) {
            loadTheme("dark-theme.css");
            imageView.setImage(new Image(getClass().getResource("/images/logo-dark-theme.png").toExternalForm()));

        } else {
            loadTheme("st-theme.css");
            imageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
        }
        isLightTheme = !isLightTheme;
    }
    @FXML
    private void onAboutUsClick()throws IOException{
        FXRouter.goTo("about-us", isLightTheme);
    }
    @FXML
    private void onTipsClick(){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tips");
        alert.setHeaderText(null);
        alert.setContentText("1) ห้ามใช้ , (comma) ในการกรอกข้อมูลตัวอักษรในโปรแกรมโดยเด็ดขาด\n" +
                "2) ใช้รูปภาพนามสกุลไฟล์ .jpg, .jpeg, .png, .gif เท่านั้นในการตั้งรูปภาพ");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/cs211/project/views/st-theme.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("custom-alert");;
        alert.showAndWait();
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
            cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
