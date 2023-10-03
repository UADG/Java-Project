package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.*;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import cs211.project.models.Event;
import cs211.project.models.collections.EventList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class EventHistoryController {
    @FXML private Label eventName;
    @FXML private Label dateStart;
    @FXML private  Label dateEnd;
    @FXML private Label timeStart;
    @FXML private Label timeEnd;
    @FXML private Label amountTicket;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private Button adminButton;
    @FXML private BorderPane bPane;
    @FXML private ImageView imageView;
    @FXML
    ListView<Event> eventListView;
    private Event selectedEvent;
    private Account accounts = (Account) FXRouter.getData();
    Datasource<EventList> eventListDatasource = new EventListFileDatasource("data","event-list.csv");
    EventList eventList = eventListDatasource.readData();
    @FXML
    public void initialize() {
        imageView.setVisible(false);
        showList(eventList);
        clearEventInfo();
        eventListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observable, Event oldValue, Event newValue) {
                if (newValue == null) {
                    clearEventInfo();
                    selectedEvent = null;
                } else {
                    imageView.setVisible(true);
                    showEventInfo(newValue);
                    selectedEvent = newValue;
                }
            }
        });
        bPane.setVisible(false);
        slide.setTranslateX(-200);
        adminButton.setVisible(false);
        if(accounts.isAdmin(accounts.getRole())){
            adminButton.setVisible(true);
        }
    }
    private void showEventInfo(Event event){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = event.getStartDate().format(formatter);
        String endDate = event.getEndDate().format(formatter);
        eventName.setText(event.getEventName());
        dateStart.setText(startDate);
        dateEnd.setText(endDate);
        timeStart.setText(event.getStartTime());
        timeEnd.setText(event.getEndTime());
        amountTicket.setText(String.format("%d",event.getTicket()));
        if(!event.getPicURL().equals("/images/default-profile.png")){
            imageView.setImage(new Image("file:"+event.getPicURL(), true));
        }else {
            imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }

    }
    private void showList(EventList eventList) {
        eventListView.getItems().clear();
        eventListView.getItems().addAll(eventList.getEvents());
        for (Event event : eventList.getEvents()) {
            if (!event.getEventManager().equals(accounts.getUsername())) {
                removeEvent(event);
            }
        }
    }
    public void removeEvent(Event event) {
        eventListView.getItems().remove(event);
    }

    public void clearEventInfo(){
        eventName.setText("");
        dateStart.setText("");
        dateEnd.setText("");
        timeStart.setText("");
        timeEnd.setText("");
        amountTicket.setText("");
        imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
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
                Object[] objects = new Object[2];
                objects[0] = accounts;
                objects[1] = selectedEvent;
                FXRouter.goTo("finish-activity",objects);
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
                Object[] objects = new Object[2];
                objects[0] = accounts;
                objects[1] = selectedEvent;
                FXRouter.goTo("ban-all",objects);
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
    @FXML
    protected void onEditActivity(){
        if (selectedEvent != null) {
            try {
                FXRouter.goTo("create-schedule", selectedEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }
    @FXML
    public void OnMenuBarClick() throws IOException {
        TranslateTransition slideAnimate = new TranslateTransition();
        slideAnimate.setDuration(Duration.seconds(0.5));
        slideAnimate.setNode(slide);
        slideAnimate.setToX(0);
        slideAnimate.play();
        menuButton.setVisible(false);
        slide.setTranslateX(0);
        bPane.setVisible(true);
    }
    @FXML
    public void closeMenuBar() throws IOException {
        TranslateTransition slideAnimate = new TranslateTransition();
        slideAnimate.setDuration(Duration.seconds(0.5));
        slideAnimate.setNode(slide);
        slideAnimate.setToX(-200);
        slideAnimate.play();
        slide.setTranslateX(-200);
        slideAnimate.setOnFinished(event-> {
            menuButton.setVisible(true);
            bPane.setVisible(false);
        });
    }
    @FXML
    public void onHomeClick() throws IOException {
        FXRouter.goTo("events-list", accounts);
    }
    @FXML
    public void onProfileClick() throws IOException {
        FXRouter.goTo("profile-setting", accounts);
    }
    @FXML
    public void onCreateEvent() throws IOException {
        FXRouter.goTo("create-event", accounts);
    }
    @FXML
    public void onJoinHistory() throws IOException {
        FXRouter.goTo("joined-history", accounts);
    }
    @FXML
    public void onEventHis() throws IOException {
        FXRouter.goTo("event-history", accounts);
    }
    @FXML
    public void onPartiSchedule() throws IOException {
        FXRouter.goTo("participant-schedule", accounts);
    }
    @FXML
    public void onTeamSchedule() throws IOException {
        FXRouter.goTo("team-schedule", accounts);
    }
    @FXML
    public void onComment() throws IOException {
        FXRouter.goTo("comment-activity", accounts);
    }
    @FXML
    public void onUserClick() throws IOException {
        FXRouter.goTo("user-status", accounts);
    }
}
