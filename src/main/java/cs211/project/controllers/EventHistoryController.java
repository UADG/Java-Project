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
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventHistoryController {
    @FXML
    private Label eventName;
    @FXML
    private Label dateStart;
    @FXML
    private  Label dateEnd;
    @FXML
    private Label timeStart;
    @FXML
    private Label timeEnd;
    @FXML
    private Label amountTicket;
    @FXML
    private AnchorPane slide;
    @FXML
    private Button menuButton;
    @FXML
    private BorderPane bPane;
    @FXML
    private ImageView imageView;
    @FXML
    private HBox hBox;
    @FXML
    private ListView<Event> eventListView;
    @FXML
    private Button editActivity;
    @FXML
    private Button editDetail;
    @FXML
    private Button finishActivity;
    @FXML
    private Button fixSchedule;
    @FXML
    private Button banAll;
    @FXML
    private AnchorPane parent;
    @FXML
    private ImageView logoImageView;
    private Object[] objects;
    private Object[] objectsSend;
    private Boolean isLightTheme;
    private Event selectedEvent;
    private Account accounts;
    private Datasource<AccountList> accountListDatasource;
    private AccountList accountList;
    private Datasource<EventList> eventListDatasource;
    private EventList eventList;
    private LocalDate currentDate;
    private LocalTime currentTime;
    private DateTimeFormatter formatter;
    private String startDate;
    private String endDate;
    private Alert alert;
    private TranslateTransition slideAnimate;
    private String time;
    private String cssPath;

    @FXML
    public void initialize() {
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        accountList = accountListDatasource.readData();
        eventListDatasource = new EventListFileDatasource("data","event-list.csv");
        eventList = eventListDatasource.readData();

        objects = (Object[]) FXRouter.getData();
        accounts = (Account) objects[0];
        isLightTheme = (Boolean) objects[1];
        loadTheme(isLightTheme);

        if (isLightTheme) {
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
        } else {
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-dark-theme.png").toExternalForm()));
        }

        hBox.setAlignment(javafx.geometry.Pos.CENTER);
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
    }
    private void showEventInfo(Event event) {
        currentDate = LocalDate.now();
        currentTime = LocalTime.now();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        startDate = event.getStartDate().format(formatter);
        endDate = event.getEndDate().format(formatter);

        if (event.getEndDate().isAfter(currentDate)) {
            editActivity.setVisible(true);
            editDetail.setVisible(true);
            finishActivity.setVisible(true);
            fixSchedule.setVisible(true);
            banAll.setVisible(true);

            eventName.setText(event.getEventName());
            dateStart.setText(startDate);
            dateEnd.setText(endDate);
            timeStart.setText(event.getStartTime());
            timeEnd.setText(event.getEndTime());
            amountTicket.setText(String.format("%d", event.getTicket()));

            if (!event.getPicURL().equals("/images/default-event.png")) {
                imageView.setImage(new Image("file:" + event.getPicURL(), true));
            } else {
                imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
            }
        } else if (event.getEndDate().isEqual(currentDate)){
            if (LocalTime.parse(event.getEndTime()).isAfter(currentTime)) {
                editActivity.setVisible(true);
                editDetail.setVisible(true);
                finishActivity.setVisible(true);
                fixSchedule.setVisible(true);
                banAll.setVisible(true);

                eventName.setText(event.getEventName());
                dateStart.setText(startDate);
                dateEnd.setText(endDate);
                timeStart.setText(event.getStartTime());
                timeEnd.setText(event.getEndTime());
                amountTicket.setText(String.format("%d", event.getTicket()));

                if (!event.getPicURL().equals("/images/default-event.png")) {
                    imageView.setImage(new Image("file:" + event.getPicURL(), true));
                } else {
                    imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
                }
            } else {
                editActivity.setVisible(false);
                editDetail.setVisible(false);
                finishActivity.setVisible(false);
                fixSchedule.setVisible(false);
                banAll.setVisible(false);

                eventName.setText(event.getEventName());
                dateStart.setText(startDate);
                dateEnd.setText(endDate);
                timeStart.setText(event.getStartTime());
                timeEnd.setText(event.getEndTime());
                amountTicket.setText(String.format("%d", event.getTicket()));

                if (!event.getPicURL().equals("/images/default-event.png")) {
                    imageView.setImage(new Image("file:" + event.getPicURL(), true));
                } else {
                    imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
                }
            }
        } else {
            editActivity.setVisible(false);
            editDetail.setVisible(false);
            finishActivity.setVisible(false);
            fixSchedule.setVisible(false);
            banAll.setVisible(false);

            eventName.setText(event.getEventName());
            dateStart.setText(startDate);
            dateEnd.setText(endDate);
            timeStart.setText(event.getStartTime());
            timeEnd.setText(event.getEndTime());
            amountTicket.setText(String.format("%d", event.getTicket()));

            if (!event.getPicURL().equals("/images/default-event.png")) {
                imageView.setImage(new Image("file:" + event.getPicURL(), true));
            } else {
                imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
            }
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
        imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
    }
    @FXML
    protected void onEditDetailClick() {
        if (selectedEvent != null) {
            try {
                FXRouter.goTo("edit-event", sendObject());
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
                FXRouter.goTo("finish-activity", sendObject());
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
                FXRouter.goTo("fix-team-schedule", sendObject());
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
                FXRouter.goTo("ban-all", sendObject());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }

    private void showErrorAlert(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void onEditActivity(){
        if (selectedEvent != null) {
            try {
                FXRouter.goTo("create-schedule", sendObject());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }

    @FXML
    public void OnMenuBarClick() throws IOException {
        slideAnimate = new TranslateTransition();
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
        slideAnimate = new TranslateTransition();
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
        FXRouter.goTo("events-list", objects);
    }

    @FXML
    public void onProfileClick() throws IOException {
        FXRouter.goTo("profile-setting", objects);
    }

    @FXML
    public void onCreateEvent() throws IOException {
        FXRouter.goTo("create-event", objects);
    }

    @FXML
    public void onJoinHistory() throws IOException {
        FXRouter.goTo("joined-history", objects);
    }

    @FXML
    public void onEventHis() throws IOException {
        FXRouter.goTo("event-history", objects);
    }

    @FXML
    public void onPartiSchedule() throws IOException {
        FXRouter.goTo("participant-schedule", objects);
    }

    @FXML
    public void onTeamSchedule() throws IOException {
        FXRouter.goTo("team-schedule", objects);
    }

    @FXML
    public void onComment() throws IOException {
        FXRouter.goTo("comment-activity", objects);
    }

    @FXML
    public void onLogOutButton() throws IOException {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        time = LocalDateTime.now().format(formatter);
        accounts.setTime(time);
        accountListDatasource.writeData(accountList);
        FXRouter.goTo("login-page");
    }
    private void loadTheme(Boolean theme) {
        if (theme) {
            loadTheme("st-theme.css");
        } else {
            loadTheme("dark-theme.css");
        }
    }
    private void loadTheme(String themeName) {
        if (parent != null) {
            cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }

    private Object sendObject() {
        objectsSend = new Object[3];
        objectsSend[0] = accounts;
        objectsSend[1] = selectedEvent;
        objectsSend[2] = isLightTheme;
        return objectsSend;
    }
}
