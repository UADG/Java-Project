package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import cs211.project.models.Event;
import cs211.project.models.EventList;
import cs211.project.services.EventHardCode;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;

public class EventsListController {
    @FXML
    private Label eventName;
    @FXML
    private Label participantLeft;
    @FXML
    private  Label ticketLeft;
    @FXML
    ListView<Event> eventListView;
    EventList eventList;
    private Event selectedEvent;
    @FXML
    public void initialize() {
        EventHardCode datasource = new EventHardCode();
        eventList = datasource.readData();
        showList(eventList);
        clearEventInfo();
        eventListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observable, Event oldValue, Event newValue) {
                if (newValue == null) {
                    clearEventInfo();
                    selectedEvent = null;
                } else {
                    showEventInfo(newValue);
                    selectedEvent = newValue;
                }
            }
        });

    }
    private void showEventInfo(Event event){
        eventName.setText(event.getEventName());
        participantLeft.setText(String.format("%d",event.getParticipantNum()));
        ticketLeft.setText(String.format("%d",event.getTicket()));

    }
    private void showList(EventList eventList) {
        eventListView.getItems().clear();
        eventListView.getItems().addAll(eventList.getEvents());
    }
    public void clearEventInfo(){
        eventName.setText("");
        participantLeft.setText("");
        ticketLeft.setText("");
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
    protected void onDetailClick() {
        try {
            FXRouter.goTo("event-details");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onBookTicketsClick() {
        try {
            FXRouter.goTo("event-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
