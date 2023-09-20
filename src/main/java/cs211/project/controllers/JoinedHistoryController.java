package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.EventList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

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
    @FXML
    private Label statusLabel;
    @FXML
    private ImageView imageUserView;

    private Datasource<EventList> eventListDatasource;
    private Datasource<AccountList> datasource;
    Account account;
    private AccountList accountList;
    private EventList eventList;
    private String selectedEvent;

    @FXML
    public void initialize() {
        clearEventInfo();

        datasource = new UserEventListFileDatasource("data","user-joined-event.csv");
        eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
        account = (Account) FXRouter.getData();

        accountList = datasource.readData();
        eventList = eventListDatasource.readData();

        showOrganizeList(account);

        eventOrganizeListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                clearEventInfo();
                selectedEvent = null;
            } else {
                clearEventInfo();
                showEventOrganizeInfo(displayUserJoinedEvents(newValue));
                selectedEvent = newValue;
            }
        });

    }

    private Event displayUserJoinedEvents(String eventName) {
        return eventList.findEventByEventName(eventName);
    }

    private void showOrganizeList(Account account) {
        eventOrganizeListView.getItems().addAll(accountList.findAccountByUsername(account.getUsername()).getAllEventUser());
    }

    private void showEventOrganizeInfo(Event event) {
        eventNameLabel.setText(event.getEventName());
        dateLabel.setText(event.getStartDate() + " - " + event.getEndDate());
        statusLabel.setText("still being organized.");
    }

    private void clearEventInfo() {
        eventNameLabel.setText("");
        dateLabel.setText("");
        imageUserView.setImage(null);
    }

    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateEventInfo() {
        clearEventInfo();
        eventOrganizeListView.getItems().clear();
        showOrganizeList(account);
    }

    @FXML
    public void onCancelEventClick() {
        if (selectedEvent != null) {
            Account account = (Account) FXRouter.getData();
            Account user = accountList.findAccountByUsername(account.getUsername());

            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation");
            confirmationDialog.setHeaderText("Delete Event");
            confirmationDialog.setContentText("Are you sure you want to delete this event: " + selectedEvent + "?");

            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    user.deleteUserEventName(selectedEvent);
                    datasource.writeData(accountList);
                    updateEventInfo();
                }
            });
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }
    @FXML
    public void onOpenSchedule() {
        if(selectedEvent!=null){
            try{
                FXRouter.goTo("event-schedule",eventList.findEventByEventName(selectedEvent));
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}