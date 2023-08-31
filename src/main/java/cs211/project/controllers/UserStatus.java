package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.collections.UserList;
import cs211.project.services.FXRouter;
import cs211.project.services.UserHardCode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class UserStatus {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ListView<User> userListView;
    @FXML
    private ImageView imageUserView;
    private User selectedUser;



    @FXML
    private void initialize() {
        clearDataInfo();
        UserHardCode dataOfUser = new UserHardCode();

        UserList userList = dataOfUser.readData();
        showList(userList);

        userListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearDataInfo();
                selectedUser = null;
            } else {
                showInfo(newValue);
                selectedUser = newValue;
            }
        });
    }




    public void showInfo(User user) {
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getName());
        timeLabel.setText(user.getTime());
        Image image = new Image(user.getPictureURL());
        imageUserView.setImage(image);
    }

    public void showList(UserList userList) {
        userListView.getItems().clear();
        userListView.getItems().addAll(userList.getUsers());
    }

    public void clearDataInfo () {
        usernameLabel.setText("");
        nameLabel.setText("");
        timeLabel.setText("");
        imageUserView.setImage(null);
    }
    public void onBackClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
