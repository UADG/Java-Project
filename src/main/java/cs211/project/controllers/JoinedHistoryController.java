package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.EventList;
import cs211.project.services.*;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JoinedHistoryController {

    @FXML
    private ListView<String> eventOrganizeListView;
    @FXML
    private ListView<String> eventFinishListView;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private ImageView imageUserView;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private BorderPane bPane;
    @FXML private Button cancelEvent;
    private Datasource<EventList> eventListDatasource;
    private Datasource<AccountList> datasource;
    Account account;
    private AccountList accountList;
    private EventList eventList;
    private String selectedEvent;
    private LocalDate currentDate;

    @FXML
    public void initialize() {
        clearEventInfo();
        currentDate = LocalDate.now();

        datasource = new UserEventListFileDatasource("data","user-joined-event.csv");
        eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
        account = (Account) FXRouter.getData();

        accountList = datasource.readData();
        eventList = eventListDatasource.readData();

        showOrganizeList(account);
        showFinishList(account);

        eventOrganizeListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null)  {
                eventFinishListView.getSelectionModel().clearSelection();
                cancelEvent.setVisible(true);
                clearEventInfo();
                showEventOrganizeInfo(displayUserJoinedEvents(newValue));
                selectedEvent = newValue;
            }
        });

        eventFinishListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                eventOrganizeListView.getSelectionModel().clearSelection();
                cancelEvent.setVisible(false);
                clearEventInfo();
                showEventFinishInfo(displayUserJoinedEvents(newValue));
                selectedEvent = newValue;
            }
        });

        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    private Event displayUserJoinedEvents(String eventName) {
        return eventList.findEventByEventName(eventName);
    }

    private void showOrganizeList(Account account) {
        eventOrganizeListView.getItems().clear();

        String username = account.getUsername();
        Account user = accountList.findAccountByUsername(username);

        for (String eventName : user.getAllEventUser()) {
            Event event = eventList.findEventByEventName(eventName);

            if (currentDate.isBefore(event.getEndDate())) {
                eventOrganizeListView.getItems().add(eventName);
            }
        }
    }

    private void showFinishList(Account account) {
        eventFinishListView.getItems().clear();

        String username = account.getUsername();
        Account user = accountList.findAccountByUsername(username);

        for (String eventName : user.getAllEventUser()) {
            Event event = eventList.findEventByEventName(eventName);

            if (currentDate.isAfter(event.getEndDate())) {
                eventFinishListView.getItems().add(eventName);
            }
        }
    }

    private void showEventOrganizeInfo(Event event) {
        eventNameLabel.setText(event.getEventName());
        dateLabel.setText(event.getStartDate() + " - " + event.getEndDate());
        statusLabel.setText("Still being organized.");
    }

    private void showEventFinishInfo(Event event) {
        eventNameLabel.setText(event.getEventName());
        dateLabel.setText(event.getStartDate() + " - " + event.getEndDate());
        statusLabel.setText("Finished");
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
                    datasource.readData();
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
            Object[] objects = new Object[2];
            objects[0] = account;
            objects[1] = eventList.findEventByEventName(selectedEvent);
            try{
                FXRouter.goTo("event-schedule",objects);
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
        FXRouter.goTo("events-list", account);
    }
    @FXML
    public void onProfileClick() throws IOException {
        FXRouter.goTo("profile-setting", account);
    }
    @FXML
    public void onCreateEvent() throws IOException {
        FXRouter.goTo("create-event", account);
    }
    @FXML
    public void onJoinHistory() throws IOException {
        FXRouter.goTo("joined-history", account);
    }
    @FXML
    public void onEventHis() throws IOException {
        FXRouter.goTo("event-history", account);
    }
    @FXML
    public void onPartiSchedule() throws IOException {
        FXRouter.goTo("participant-schedule", account);
    }
    @FXML
    public void onTeamSchedule() throws IOException {
        FXRouter.goTo("team-schedule", account);
    }
    @FXML
    public void onComment() throws IOException {
        FXRouter.goTo("comment-activity", account);
    }
    @FXML
    public void onLogOutButton() throws IOException {
        Datasource<AccountList> accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        AccountList accountList = accountListDatasource.readData();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = LocalDateTime.now().format(formatter);
        account.setTime(time);
        Datasource<AccountList> dataSource = new AccountListDatasource("data","user-info.csv");
        dataSource.writeData(accountList);
        FXRouter.goTo("login-page");
    }
}