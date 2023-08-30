package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.collections.EventList;
import cs211.project.services.EventHardCode;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class JoinedHistoryController {

    @FXML
    private Label eventNameLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private ListView<Event> eventListView;
    private EventList eventList;
    private Event selectedEvent;

    @FXML
    public void initialize() {
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
                    showEventInfo(newValue);
                    selectedEvent = newValue;
                }
            }
        });
    }

    private void showList(EventList eventList) {
        eventListView.getItems().clear();
        eventListView.getItems().addAll(eventList.getEvents());
    }

    private void showEventInfo(Event event) {
        eventNameLabel.setText(event.getEventName());
        dayLabel.setText(String.format("%d - %d",event.getStartDate(), event.getEndDate()));
    }

    private void clearEventInfo() {
        eventNameLabel.setText("");
        dayLabel.setText("");
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
