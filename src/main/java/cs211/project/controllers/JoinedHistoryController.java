package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.User;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.UserList;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserEventListFileDatasource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.io.IOException;

public class JoinedHistoryController {

    @FXML
    private ListView<String> eventOrganizeListView;
    @FXML
    private ListView<String> eventCompleteListView;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label dateLabel;

    private UserEventListFileDatasource userEventListFileDatasourceDatasource;
    private UserList userList;
    private Event selectedEvent;

    @FXML
    public void initialize() {
        clearEventInfo();
        User account = (User) FXRouter.getData();
        userList = userEventListFileDatasourceDatasource.readData();
        showList(account);
        eventOrganizeListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) {
                    clearEventInfo();
                    selectedEvent = null;
                }
            }
        });
    }

    private void showList(User account) {
    }

    private void showEventInfo(String event) {
        eventNameLabel.setText(event);
        dateLabel.setText("");
    }

    private void clearEventInfo() {
        eventNameLabel.setText("");
        dateLabel.setText("");
    }

    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}