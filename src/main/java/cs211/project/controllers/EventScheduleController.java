package cs211.project.controllers;

import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.EventList;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalTime;

public class EventScheduleController {
    @FXML private TableView<Activity> activityTableView;
    private Event selectedEvent;
    @FXML
    public void initialize() {
        selectedEvent = (Event) FXRouter.getData();
        Datasource<EventList> eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
        EventList eventList = eventListDatasource.readData();
        selectedEvent = eventList.findEventByEventName(selectedEvent.getEventName());
        System.out.println(selectedEvent);
        showTable(selectedEvent.loadActivityInEvent());
    }
    private void showTable(ActivityList activityList) {
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, String> dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

        TableColumn<Activity, LocalTime> participantColumn = new TableColumn<>("Participant");
        participantColumn.setCellValueFactory(new PropertyValueFactory<>("participantName"));

        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);
        activityTableView.getColumns().add(participantColumn);

        activityTableView.getItems().clear();

        for (Activity activity: activityList.getActivities()) {
            activityTableView.getItems().add(activity);
        }
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
