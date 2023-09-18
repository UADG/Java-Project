package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.EventList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserEventListFileDatasource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.ArrayList;

public class JoinedHistoryController {

    @FXML
    private ListView<String> eventOrganizeListView;
    @FXML
    private ListView<String> eventCompleteListView;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label dateLabel;

    private Datasource<AccountList> datasource;
    private Datasource<AccountList> accountListDataSource;
    private AccountList accountList;
    private AccountList userInfo;
    private EventList eventList;
    private Event selectedEvent;

    @FXML
    public void initialize() {
        clearEventInfo();
        accountListDataSource = new AccountListDatasource("data", "user-info.csv");
        datasource = new UserEventListFileDatasource("data", "user-history.csv");
        userInfo = accountListDataSource.readData();
        accountList = datasource.readData();
        System.out.println(userInfo.getAccount());
        System.out.println(accountList.getAccount());
        showList();
        eventOrganizeListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue == null) {
                    clearEventInfo();
                    selectedEvent = null;
                } else {
                    showEventInfo(newValue);
                    displayUserJoinedEvents(newValue);
                }
            }
        });
    }

    private void displayUserJoinedEvents(String username) {
        System.out.println("Displaying events for user: " + username);

        Account user = accountList.findAccountByUsername(username);
        if (user != null) {
            System.out.println("User found: " + user.getUsername());

            ArrayList<String> joinedEvents = user.getEventName();
            System.out.println("Joined events: " + joinedEvents);

            eventCompleteListView.getItems().clear();

            eventCompleteListView.getItems().addAll(joinedEvents);
            System.out.println("Added events to eventCompleteListView");
        } else {
            System.out.println("User not found: " + username);
        }
    }

    private void showList() {
        Account account = (Account) FXRouter.getData();
        eventOrganizeListView.getItems().addAll(account.getEventName());
    }

    private void showEventInfo(String event) {
        eventNameLabel.setText(event);
        dateLabel.setText(eventList.findEventByEventName(event).getEndTime());
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