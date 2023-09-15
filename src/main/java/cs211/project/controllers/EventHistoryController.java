package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import cs211.project.models.Event;
import cs211.project.models.collections.EventList;
import cs211.project.services.EventHardCode;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.fxml.FXML;

import java.io.IOException;

public class EventHistoryController {
    @FXML private Label eventName;
    @FXML private Label dateStart;
    @FXML private  Label dateEnd;
    @FXML private Label timeStart;
    @FXML private Label timeEnd;
    @FXML private Label amountTicket;
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
        dateStart.setText(event.getStartDate());
        dateEnd.setText(event.getEndDate());
        timeStart.setText(event.getStartTime());
        timeEnd.setText(event.getEndTime());
        amountTicket.setText(String.format("%d",event.getTicket()));

    }
    private void showList(EventList eventList) {
        eventListView.getItems().clear();
        eventListView.getItems().addAll(eventList.getEvents());
    }
    public void clearEventInfo(){
        eventName.setText("");
        dateStart.setText("");
        dateEnd.setText("");
        timeStart.setText("");
        timeEnd.setText("");
        amountTicket.setText("");
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
    protected void onEditDetailClick() {
        if (selectedEvent != null) {
            try {
                FXRouter.goTo("edit-event", selectedEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }

    @FXML
    protected void onFinishActivityClick() {
        if (selectedEvent != null) {
            try {
                FXRouter.goTo("finish-activity",selectedEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }
    @FXML
    protected void onFixScheduleClick() {
        if (selectedEvent != null) {
            try {
                FXRouter.goTo("fix-team-schedule",selectedEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }


    @FXML
    protected void onBanAllClick() {
        if (selectedEvent != null) {
            try {
                FXRouter.goTo("ban-all",selectedEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
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
