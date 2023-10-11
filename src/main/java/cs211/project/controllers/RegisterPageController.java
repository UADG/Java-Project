package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
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
    @FXML
    private TextField nameText;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField passText;
    @FXML
    private TextField confirmText;
    @FXML
    private AnchorPane parent;
    private Datasource<AccountList> accountListDatasource;
    private AccountList accountList;
    private Account account;
    private File file;
    private FileInputStream fileInputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader buffer;
    private Alert alert;
    private Boolean isLightTheme;
    private DateTimeFormatter formatter;
    private String time;
    private String username;
    private String name;
    private String pass;
    private String confirmPass;
    private String line;
    private String cssPath;
    private String specialChar;
    private String[] data;
    private int id;
    @FXML
    public void initialize() {
        isLightTheme = (Boolean) FXRouter.getData();
        loadTheme(isLightTheme);

        accountListDatasource = new AccountListDatasource("data","user-info.csv");
        accountList = accountListDatasource.readData();
    }
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("login-page");
    }
    public void onConfirmClick(ActionEvent event) throws IOException {
        username = usernameText.getText();
        name = nameText.getText();
        pass = passText.getText();
        confirmPass = confirmText.getText();
        account = accountList.findAccountByUsername(username);
        id = 0;
        file = new File("data","user-info.csv");
        fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        buffer = new BufferedReader(inputStreamReader);
        line = "";
        try {
            while ( (line = buffer.readLine()) != null ){
                if (line.isEmpty()) continue;
                data = line.split(",");
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
            if(!username.equals("")&&!name.equals("")&&!pass.equals("")&&!confirmPass.equals("")){
                if(!isContainSpecialCharacter(username)||!isContainSpecialCharacter(name)||!isContainSpecialCharacter(pass)) {
                    if (pass.equals(confirmPass)) {
                        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        time = LocalDateTime.now().format(formatter);
                        accountList.addNewAccount(id, username, pass, name, time, "/images/default-profile.png", "user");
                        accountList = accountListDatasource.readData();
                        accountListDatasource.writeData(accountList);
                        FXRouter.goTo("login-page");
                    } else {
                        showErrorAlert("Confirm password wrong. Please try again.");
                    }
                }else {
                    showErrorAlert("Information must not contain special character.");
                }
            }else {
                showErrorAlert("Please fill all the information.");
            }
        }else {
            showErrorAlert("This username already in used.");
        }
    }
    private void showErrorAlert(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
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

    public boolean isContainSpecialCharacter(String cha){
        specialChar = "~`!@#$%^&*()={[}]|\\:;\"'<,>.?/";
        for(char c : cha.toCharArray()){
            if (specialChar.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }
}
