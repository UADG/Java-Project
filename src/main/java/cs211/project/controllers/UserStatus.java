package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.collections.UserList;
import cs211.project.services.FXRouter;
import cs211.project.services.UserHardCode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

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
    private UserList userList;
    private User selectedUser;



    @FXML
    private void initialize() {
        clearDataInfo();
        UserHardCode dataOfUser = new UserHardCode();

        userList = dataOfUser.readData();

        showList(userList);
        userListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                if (newValue == null) {
                    clearDataInfo();
                    selectedUser = null;
                } else {
                    showInfo(newValue);
                    selectedUser = newValue;
                }
            }
        });
    }




    public void showInfo(User user) {
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getName());
        timeLabel.setText(user.getTime());
    }

    public void showList(UserList userList) {
        userListView.getItems().clear();
        userListView.getItems().addAll(userList.getUsers());

    }

    public void clearDataInfo () {
        usernameLabel.setText("");
        nameLabel.setText("");
        timeLabel.setText("");
    }
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("home-page");
    }
}
