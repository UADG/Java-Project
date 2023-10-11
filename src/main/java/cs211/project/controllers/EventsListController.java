package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.*;
import cs211.project.services.*;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventsListController {
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label ticketLeftLabel;
    @FXML
    private Label teamLeftLabel;
    @FXML
    private Label errorLabelBook;
    @FXML
    private Label errorLabelApplyToParticipants;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane slide;
    @FXML
    private AnchorPane parent;
    @FXML
    private Button menuButton;
    @FXML
    private BorderPane bPane;
    @FXML
    private HBox hBox;
    @FXML
    private Button bookTicket;
    @FXML
    private Button applyStaff;
    @FXML
    private Button applyParticipant;
    @FXML
    private ImageView logoImageView;
    @FXML
    private GridPane eventGridPane;
    private AnchorPane anchorPane;
    private Datasource<EventList> eventListDatasource;
    private Datasource<ActivityList> datasource;
    private Datasource<AccountList> accountListDatasource;
    private Datasource<AccountList> banListDatasource;
    private Datasource<TeamList> teamListDatasource;
    private Datasource<StaffList> banStaffListDatasource;
    private AccountList accountList;
    private AccountList banList;
    private StaffList banStaffList;
    private EventList eventList;
    private TeamList teamList;
    private TeamList teams;
    private ActivityList activityList;
    private Account account;
    private Account ban;
    private Staff banStaff;
    private Event selectedEvent;
    private Team teamFound;
    private TeamListFileDatasource data;
    private Object[] objects;
    private Object[] objectsSend;
    private FXMLLoader fxmlLoader;
    private TranslateTransition slideAnimate;
    private Alert alert;
    private EventItemController eventItemController;
    private DateTimeFormatter formatter;
    private LocalDate currentDate;
    private LocalTime currentTime;
    private String textSearch;
    private String cssPath;
    private String time;
    private String teamName;
    private Boolean isLightTheme;
    private boolean found;
    private int column;
    private int row;
    @FXML
    public void initialize() {
        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        isLightTheme = (Boolean) objects[1];
        loadTheme(isLightTheme);
        if(isLightTheme){
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
        }else{
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-dark-theme.png").toExternalForm()));
        }
        imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        bPane.setVisible(false);
        errorLabelBook.setText("");
        errorLabelApplyToParticipants.setText("");
        textSearch = "";
        clearEventInfo();

        eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
        accountListDatasource = new UserEventListFileDatasource("data","user-joined-event.csv");
        banListDatasource = new UserEventListFileDatasource("data","ban-user.csv");
        teamListDatasource = new TeamListFileDatasource("data", "team.csv");

        eventList = eventListDatasource.readData();
        accountList = accountListDatasource.readData();
        banList = banListDatasource.readData();
        teamList = teamListDatasource.readData();

        currentDate = LocalDate.now();
        currentTime = LocalTime.now();
        ban = banList.findAccountByUsername(account.getUsername());
        banStaffListDatasource = new BanListFileDatasource("data","ban-staff-list.csv");
        banStaffList = banStaffListDatasource.readData();
        banStaff = banStaffList.checkStaffInList(String.valueOf(account.getId()));
        slide.setTranslateX(-200);
        account = accountList.findAccountByUsername(account.getUsername());
        showGrid(eventList);
    }
    public void showGrid(EventList eventLists){
        eventGridPane.getChildren().clear();
        column = 0;
        row = 1;
        if (textSearch.equals("")) {
            for (Event event: eventLists.getEvents()) {
                if (event.getEndDate().isAfter(currentDate)) {
                    createGrid(event);
                }else if (event.getEndDate().isEqual(currentDate)) {
                    createGrid(event);
                }
            }
        }else {
            for (Event event: eventLists.getSearch()) {
                createGrid(event);
            }
        }
    }

    private void createGrid(Event event){
        try {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/event-item.fxml"));
            anchorPane = fxmlLoader.load();

            eventItemController = fxmlLoader.getController();
            eventItemController.setData(event);

            if (column == 2) {
                column = 0;
                row++;
            }
            anchorPane.setOnMouseClicked(events -> {
                clearEventInfo();
                imageView.setVisible(true);
                errorLabelBook.setText("");
                selectedEvent = event;
                showEventInfo(event);
            });

            eventGridPane.add(anchorPane, column++, row);
            eventGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            eventGridPane.setPrefWidth(460);
            eventGridPane.setMaxWidth(Region.USE_PREF_SIZE);

            eventGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
            eventGridPane.setPrefHeight(460);
            eventGridPane.setMaxHeight(Region.USE_PREF_SIZE);

            GridPane.setMargin(anchorPane, new Insets(20));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showEventInfo(Event event) {
        if (event.getEndDate().isAfter(currentDate)){
            bookTicket.setVisible(true);
            applyStaff.setVisible(true);
            applyParticipant.setVisible(true);
            eventNameLabel.setText(event.getEventName());
            ticketLeftLabel.setText(String.format("%d", event.getTicketLeft()));
            teamLeftLabel.setText(String.format("%d", teamList.allNumberOfStaffLeft(event.getEventName())));
            if (!event.getPicURL().equals("/images/default-event.png")) {
                imageView.setImage(new Image("file:" + event.getPicURL(), true));
            } else {
                imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
            }
        } else if (event.getEndDate().isEqual(currentDate)){
            if (LocalTime.parse(event.getEndTime()).isAfter(currentTime)) {
                bookTicket.setVisible(true);
                applyStaff.setVisible(true);
                applyParticipant.setVisible(true);
                eventNameLabel.setText(event.getEventName());
                ticketLeftLabel.setText(String.format("%d", event.getTicketLeft()));
                teamLeftLabel.setText(String.format("%d", teamList.allNumberOfStaffLeft(event.getEventName())));
                if (!event.getPicURL().equals("/images/default-event.png")) {
                    imageView.setImage(new Image("file:" + event.getPicURL(), true));
                } else {
                    imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
                }
            } else {
                bookTicket.setVisible(false);
                applyStaff.setVisible(false);
                applyParticipant.setVisible(false);
                eventNameLabel.setText(event.getEventName());
                ticketLeftLabel.setText(String.format("%d", event.getTicketLeft()));
                teamLeftLabel.setText(String.format("%d", teamList.allNumberOfStaffLeft(event.getEventName())));
                if (!event.getPicURL().equals("/images/default-event.png")) {
                    imageView.setImage(new Image("file:" + event.getPicURL(), true));
                } else {
                    imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
                }

            }
        } else {
            bookTicket.setVisible(false);
            applyStaff.setVisible(false);
            applyParticipant.setVisible(false);
            eventNameLabel.setText(event.getEventName());
            ticketLeftLabel.setText(String.format("%d", event.getTicketLeft()));
            teamLeftLabel.setText(String.format("%d", teamList.allNumberOfStaffLeft(event.getEventName())));
            if (!event.getPicURL().equals("/images/default-event.png")) {
                imageView.setImage(new Image("file:" + event.getPicURL(), true));
            } else {
                imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
            }
        }
    }

    private void clearEventInfo() {
        eventNameLabel.setText("");
        ticketLeftLabel.setText("");
        teamLeftLabel.setText("");
        imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
    }

    @FXML
    public void onSearchEventButton() {
        textSearch = searchTextField.getText().trim();
        try {
            eventList.searchEvent(textSearch);
            showGrid(eventList);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onDetailClick() {
        if (selectedEvent != null) {
            try {
                objectsSend = new Object[3];
                objectsSend[0] = account;
                objectsSend[1] = selectedEvent;
                objectsSend[2] = isLightTheme;
                FXRouter.goTo("event-details", objectsSend);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showErrorAlert("Must selected at least 1 event");
        }
    }

    @FXML
    protected void onBookTicketsClick() {
        if (selectedEvent != null) {
            selectedEvent.loadTeamInEvent();
            teamFound = null;
            found = false;
            try {
                if (!selectedEvent.getEventManager().equals(account.getUsername())) {
                    TeamList teams = selectedEvent.getTeams();
                    for (Team team : teams.getTeams()) {
                        for (Staff staff : team.getStaffs().getStaffList()) {
                            if (staff.getId().equals(Integer.toString(account.getId()))) {
                                found = true;
                                teamFound = team;
                                break;
                            }
                        }
                    }
                }

                if (selectedEvent.isEventManager(account.getUsername())) {
                    showErrorAlert("You can't join your own event.");
                }else if(account.isEventName(selectedEvent.getEventName())) {
                    showErrorAlert("You have already booked a ticket for this event.");
                }else if(!selectedEvent.getArrayListActivities().isEmpty() && selectedEvent.getActivities().userIsParticipant(account.getUsername())){
                    showErrorAlert("Sorry, you're participant in this event.");
                }else if(ban.isEventName(selectedEvent.getEventName())){
                    showErrorAlert("Sorry, you have ban from this event.");
                }else if(found){
                    showErrorAlert("\"You are have team in this event already \nYour team is \"" + teamFound);
                }else if(selectedEvent.getTicketLeft() > 0) {
                    selectedEvent.ticketBuy();

                    eventListDatasource.readData();
                    eventListDatasource.writeData(eventList);
                    account.addUserEventName(selectedEvent.getEventName());
                    accountListDatasource.writeData(accountList);

                    objectsSend = new Object[3];
                    objectsSend[0] = account;
                    objectsSend[1] = selectedEvent;
                    objectsSend[2] = isLightTheme;
                    FXRouter.goTo("event-schedule", objectsSend);
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
            selectedEvent.loadTeamInEvent();
            if(!selectedEvent.getArrayListActivities().isEmpty() && selectedEvent.getActivities().userIsParticipant(account.getUsername())){
                showErrorAlert("Sorry, you're participant in this event.");
            }
            else if(account.isEventName(selectedEvent.getEventName())) {
                showErrorAlert("You have already booked a ticket for this event.");
            }else if(ban.isEventName(selectedEvent.getEventName())){
                showErrorAlert("Sorry, you have ban form this event.");
            }else if(banStaff != null){
                showErrorAlert("Sorry, you have ban from being staff in this event");
            }else {
                if (!selectedEvent.getEventManager().equals(account.getUsername())) {
                    teams = selectedEvent.getTeams();
                    teamFound = null;
                    found = false;
                    for (Team team : teams.getTeams()) {
                        for (Staff staff : team.getStaffs().getStaffList()) {
                            if (staff.getId().equals(Integer.toString(account.getId()))) {
                                found = true;
                                teamFound = team;
                                break;
                            }
                        }
                    }

                    if (!found) {
                        try {
                            data = new TeamListFileDatasource("data", "team.csv");
                            try {
                                teamName = teams.findLowestStaffTeam().getTeamName();
                                data.updateStaffInTeam(selectedEvent.getEventName(), teamName, new Staff(account), "+");
                                showInfoPopup("You are in " + teamName + " team");
                                FXRouter.goTo("team-schedule", objects);
                            } catch (NullPointerException e) {
                                showErrorAlert("Sorry, there are no available seats at the moment.");
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        showInfoPopup("You are have team in this event already \nYour team is " + teamFound);
                    }
                } else {
                    showErrorAlert("You can't join your own event.");
                }
            }
        }else{
            showErrorAlert("Please select some event ");
        }

    }
    @FXML
    protected void onApplyToParticipantClick() throws IOException {
        if(selectedEvent != null) {
            selectedEvent.loadActivityInEvent();
            if(ban.isEventName(selectedEvent.getEventName())){
                showErrorAlert("Sorry, you have ban form this event.");
            }else if(!selectedEvent.getEventManager().equals(account.getUsername())) {
                if(account.isEventName(selectedEvent.getEventName())) {
                    showErrorAlert("You have already booked a ticket for this event.");
                }else if (!selectedEvent.getArrayListActivities().isEmpty()) {
                    datasource = new ActivityListFileDatasource("data", "activity-list.csv");
                    activityList = datasource.readData();
                    activityList.findActivityInEvent(selectedEvent.getEventName());
                    if (!activityList.userIsParticipant(account.getUsername())) {
                        if (selectedEvent.checkParticipantIsFull()) {
                            activityList.addParticipant(account.getUsername());
                            datasource.writeData(activityList);
                            FXRouter.goTo("participant-schedule", objects);
                        } else {
                            showErrorAlert("Sorry, participant in full.");
                        }
                    } else {
                        showErrorAlert("Sorry, you're participant in this event.");
                    }
                } else {
                    showErrorAlert("Sorry, this event not ready for apply to participant.");
                }
            } else {
                showErrorAlert("You can't join your own event.");
            }
        }
        else {
            showErrorAlert("Please select event");
        }
    }

    private void showErrorAlert(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoPopup(String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        account.setTime(time);
        accountList = accountListDatasource.readData();
        accountListDatasource.writeData(accountList);
        FXRouter.goTo("login-page");
    }

    @FXML
    protected void onChangeTheme() {
        if (isLightTheme) {
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-dark-theme.png").toExternalForm()));
            loadTheme("dark-theme.css");
        } else {
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
            loadTheme("st-theme.css");
        }
        isLightTheme = !isLightTheme;
        objects[1] = isLightTheme;
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

}
