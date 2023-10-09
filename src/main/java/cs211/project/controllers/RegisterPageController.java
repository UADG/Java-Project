package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.ThemeDatasource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegisterPageController {
    Datasource<AccountList> accountListDatasource = new AccountListDatasource("data","user-info.csv");
    AccountList accountList = accountListDatasource.readData();
<<<<<<< HEAD
    @FXML TextField nameText;
    @FXML TextField usernameText;
    @FXML TextField passText;
    @FXML TextField confirmText;
    @FXML private AnchorPane parent;
    private ThemeDatasource themeDatasource = new ThemeDatasource("data", "theme.csv");
    private String theme = themeDatasource.read();
    @FXML
    public void initialize() {
        loadTheme(theme);
    }
=======
    @FXML private TextField nameText;
    @FXML private TextField usernameText;
    @FXML private TextField passText;
    @FXML private TextField confirmText;
    @FXML private AnchorPane parent;
    private Boolean isLightTheme;

    @FXML
    private void initialize() {
        isLightTheme = (Boolean) FXRouter.getData();
        loadTheme(isLightTheme);
    }


>>>>>>> 8ab29c07a331938002d3ef6deeeaf29016062bbf
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("login-page");
    }
    public void onConfirmClick(ActionEvent event) throws IOException {
        String username = usernameText.getText();
        String name = nameText.getText();
        String pass = passText.getText();
        String confirmPass = confirmText.getText();
        Account account = accountList.findAccountByUsername(username);
        int id = 0;
        File file = new File("data","user-info.csv");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);
        String line = "";
        try {
            while ( (line = buffer.readLine()) != null ){
                if (line.isEmpty()) continue;
                String[] data = line.split(",");
                if(id <= Integer.parseInt(data[0]))
                id = Integer.parseInt(data[0])+1;
                }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                buffer.close();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        if(account==null){
            if(!username.equals("")&&!name.equals("")&&!pass.equals("")){
                if(pass.equals(confirmPass)){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String time = LocalDateTime.now().format(formatter);
                    accountList.addNewAccount(id,username,pass,name, time, "/images/default-profile.png","unban", "user");
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
<<<<<<< HEAD
=======

    private void loadTheme(Boolean theme) {
        if (theme) {
            loadTheme("st-theme.css");
        } else {
            loadTheme("dark-theme.css");
        }
    }

>>>>>>> 8ab29c07a331938002d3ef6deeeaf29016062bbf
    private void loadTheme(String themeName) {
        if (parent != null) {
            String cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
