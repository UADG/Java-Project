package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.User;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.EventList;
import cs211.project.services.ActivityListFileDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.EventHardCode;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EventsListController {

    @FXML
    private Label eventNameLabel;
    @FXML
    private Label ticketLeftLabel;
    @FXML
    private Label participantLeftLabel;
    @FXML
    private ListView<Event> eventListView;
    private EventList eventList;
    private String textSearch = "";

    @FXML private Label errorLabelBook;
    @FXML private Label errorLabelApplyToParticipants;

    @FXML private TextField searchTextField;
    private Event selectedEvent;
    private ActivityList activityList;
    private Datasource<ActivityList> datasource;

    @FXML
    public void initialize() {
        errorLabelBook.setText("");
        errorLabelApplyToParticipants.setText("");
        clearEventInfo();
        EventHardCode datasource = new EventHardCode();
        eventList = datasource.readData();
        showList(eventList);
        eventListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observable, Event oldValue, Event newValue) {
                if (newValue == null) {
                    clearEventInfo();
                    selectedEvent = null;
                } else {
                    errorLabelBook.setText("");
                    showEventInfo(newValue);
                    selectedEvent = newValue;
                }
            }
        });
    }

    private void showList(EventList eventList) {
        if(textSearch.equals("")) {
            eventListView.getItems().clear();
            eventListView.getItems().addAll(eventList.getEvents());
        }
        else{
            eventListView.getItems().clear();
            eventListView.getItems().addAll(eventList.getSearch());
        }
    }

    private void showEventInfo(Event event) {
        eventNameLabel.setText(event.getEventName());
        ticketLeftLabel.setText(String.format("%d",event.getTicketLeft()));
        participantLeftLabel.setText(String.format("%d", event.getParticipantLeft()));
    }

    private void clearEventInfo() {
        eventNameLabel.setText("");
        ticketLeftLabel.setText("");
        participantLeftLabel.setText("");
    }

    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onSearchEventButton() {
        textSearch = searchTextField.getText().trim();
        try {
            eventList.searchEvent(textSearch);
            showList(eventList);

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onDetailClick() {
        try {
            FXRouter.goTo("event-details");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onBookTicketsClick() {
        Account data = (Account) FXRouter.getData();
        if (selectedEvent != null) {
            try {
                if(selectedEvent.getTicketLeft() > 0) {
                    selectedEvent.ticketBuy();
                    FXRouter.goTo("event-schedule", data);
                }
                else {
                    errorLabelBook.setText("Sorry tickets sold out");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }
    @FXML
    protected void onApplyToStaffClick() {
        try {
            FXRouter.goTo("event-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }@FXML
    protected void onApplyToParticipantClick() {
        try {
            if(selectedEvent.getParticipantLeft() > 0 ) {
                datasource = new ActivityListFileDatasource("data", "activity-list.csv");
                activityList = datasource.readData();
                activityList.findActivityInEvent(selectedEvent.getEventName());
                if(activityList.userIsParticipant("UADG")) {
                    activityList.addParticipant("UADG");
                    selectedEvent.participantJoin();
                    datasource.writeData(activityList);
                    FXRouter.goTo("participant-schedule");
                }
                else{
                    errorLabelApplyToParticipants.setText("Sorry you're participant in this event");
                }
            }
            else {
                errorLabelApplyToParticipants.setText("Sorry participant is full");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
