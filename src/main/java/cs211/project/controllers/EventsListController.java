package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;

public class EventsListController {
    private Account account = (Account) FXRouter.getData();
    private AccountList accountList;

    @FXML
    private Label eventNameLabel;
    @FXML
    private Label ticketLeftLabel;
    @FXML
    private Label participantLeftLabel;
    @FXML
    private ListView<Event> eventListView;
    @FXML
    private Label errorLabelBook;
    @FXML
    private Label errorLabelApplyToParticipants;
    @FXML
    private TextField searchTextField;
    @FXML
    ImageView imageView;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private Button adminButton;
    @FXML private BorderPane bPane;
    @FXML private HBox hBox;
    private Datasource<EventList> eventListDatasource;
    private Datasource<ActivityList> datasource;
    private Datasource<AccountList> accountListDatasource;
    private EventList eventList;
    private String textSearch = "";
    private Event selectedEvent;
    private ActivityList activityList;

    @FXML
    public void initialize() {
        imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        bPane.setVisible(false);
        errorLabelBook.setText("");
        errorLabelApplyToParticipants.setText("");
        clearEventInfo();

        eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
        accountListDatasource = new UserEventListFileDatasource("data","user-joined-event.csv");

        eventList = eventListDatasource.readData();
        accountList = accountListDatasource.readData();
        slide.setTranslateX(-200);

        showList(eventList);
        eventListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observable, Event oldValue, Event newValue) {
                if (newValue == null) {
                    clearEventInfo();
                    selectedEvent = null;
                } else {
                    imageView.setVisible(true);
                    errorLabelBook.setText("");
                    showEventInfo(newValue);
                    selectedEvent = newValue;
                }
            }
        });
        adminButton.setVisible(false);
        if(account.isAdmin(account.getRole())){
            adminButton.setVisible(true);
        }
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
        if(!event.getPicURL().equals("/images/default-profile.png")){
            imageView.setImage(new Image("file:"+event.getPicURL(), true));
        }else {
            imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }
    }

    private void clearEventInfo() {
        eventNameLabel.setText("");
        ticketLeftLabel.setText("");
        participantLeftLabel.setText("");
        imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
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
        if (selectedEvent != null) {
            try {
                FXRouter.goTo("event-details", selectedEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }

    @FXML
    protected void onBookTicketsClick() {
        Account data = (Account) FXRouter.getData();
        Account account  = accountList.findAccountByUsername(data.getUsername());
        if (selectedEvent != null) {
            try {
                if (selectedEvent.isEventManager(account.getUsername())) {
                    showErrorAlert("You are the manager of this event.");
                } else if(account.isEventName(selectedEvent.getEventName())) {
                    showErrorAlert("You have already booked a ticket for this event.");
                } else if(selectedEvent.getTicketLeft() > 0) {
                    selectedEvent.ticketBuy();
                    account.addUserEventName(selectedEvent.getEventName());
                    accountListDatasource.writeData(accountList);
                    Object[] objects = new Object[2];
                    objects[0] = account;
                    objects[1] = selectedEvent;
                    FXRouter.goTo("event-schedule", objects);
                }
                else {
                    errorLabelBook.setText("Sorry, tickets for this event are sold out.");
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
        if(selectedEvent != null){
            selectedEvent.loadActivityInEvent();
            if(!selectedEvent.getEventManager().equals(account.getUsername())){
                TeamList teams = selectedEvent.loadTeamInEvent();
                System.out.println(selectedEvent.getEventName());
                Team teamFound = null;
                boolean found = false;
                for(Team team : teams.getTeams()){
                    for(Staff staff: team.getStaffs().getStaffList()){
                        if(staff.getId().equals(Integer.toString(account.getId()))){
                            found = true;
                            teamFound = team;
                            break;
                        }
                    }
                }

                if(!found){
                    try {
                        System.out.println(selectedEvent.getEventName());
                        TeamListFileDatasource data = new TeamListFileDatasource("data", "team.csv");
                        try{
                            String teamName = teams.findLowestStaffTeam().getTeamName();
                            data.updateStaffInTeam(selectedEvent.getEventName(),teamName , new Staff(account), "+");
                            showInfoPopup("You are in "+teamName+" team");
                            FXRouter.goTo("team-schedule",account);
                        }catch (NullPointerException e){
                            showErrorAlert("Sorry, there are no available seats at the moment.");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    showInfoPopup("You are have team in this event already \nYour team is "+teamFound);
                }
            }else{
                showErrorAlert("you can't join your own event");
            }
        }else{
            showErrorAlert("please select some event ");
        }

    }
    @FXML
    protected void onApplyToParticipantClick() {
        try {
            if(!selectedEvent.loadActivityInEvent().getActivities().isEmpty()) {
                if(selectedEvent.checkParticipantIsFull()) {
                    datasource = new ActivityListFileDatasource("data", "activity-list.csv");
                    activityList = datasource.readData();
                    activityList.findActivityInEvent(selectedEvent.getEventName());
                    if (activityList.userIsParticipant(account.getUsername())) {
                        activityList.addParticipant(account.getUsername());
                        datasource.writeData(activityList);
                        FXRouter.goTo("participant-schedule", account);
                    } else {
                        showErrorAlert("Sorry you're participant in this event");
                    }
                }
                else{
                    showErrorAlert("Sorry participant in full");
                }
            }
            else {
                showErrorAlert("Sorry this event not ready for apply to participant");
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

    private void showInfoPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
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
    public void onUserClick() throws IOException {
        FXRouter.goTo("user-status", account);
    }
}
