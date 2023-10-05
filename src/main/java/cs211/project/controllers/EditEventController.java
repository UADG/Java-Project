package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.EventList;
import cs211.project.services.*;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

public class EditEventController {
    @FXML
    private TextField eventNameTextField;
    @FXML
    private DatePicker eventDatePickerStart;
    @FXML
    private DatePicker eventDatePickerEnd;
    @FXML
    private TextField timeStartEventTextField;
    @FXML
    private TextField timeEndEventTextField;
    @FXML
    private TextField amountTicketTextField;
    @FXML
    private TextField amountParticipantTextField;
    @FXML
    private TextField detailTextField;
    @FXML
    private TextField timeEndTeamTextField;
    @FXML
    private TextField timeEndParticipantTextField;
    @FXML
    private ImageView imageView;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private BorderPane bPane;
    @FXML private HBox hBox;
    private Datasource<EventList> eventListDatasource;
    private Datasource<AccountList> accountListDatasource;
    private Datasource<AccountList> joinedEventDatasource;
    private EventList eventList;
    private Event event;
    private LocalDate currentDate;
    private String errorMessage;
    private Event events;
    private AccountList accountList;
    private Account account;
    private AccountList accountJoinList;
    private Account accountJoined;

    @FXML
    private void initialize() {
        clearErrorMessage();

        Event data = (Event) FXRouter.getData();
        currentDate = LocalDate.now();
        events = (Event) FXRouter.getData();
        eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        joinedEventDatasource = new UserEventListFileDatasource("data", "user-joined-event.csv");

        eventList = eventListDatasource.readData();
        accountList = accountListDatasource.readData();
        accountJoinList = joinedEventDatasource.readData();

        event = eventList.findEventByEventName(data.getEventName());
        account = accountList.findAccountByUsername(events.getEventManager());

        showInfo(event);

        eventDatePickerEnd.setEditable(false);
        eventDatePickerStart.setEditable(false);

        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    private void showInfo(Event event) {
        eventNameTextField.setText(event.getEventName());
        eventDatePickerStart.setValue(event.getStartDate());
        eventDatePickerEnd.setValue(event.getEndDate());;
        timeStartEventTextField.setText(event.getStartTime());
        timeEndEventTextField.setText(event.getEndTime());
        amountTicketTextField.setText(String.valueOf(event.getTicket()));
        amountParticipantTextField.setText(String.valueOf(event.getParticipantNum()));
        detailTextField.setText(event.getDetail());
        timeEndTeamTextField.setText(event.getTimeTeam());
        timeEndParticipantTextField.setText(event.getTimeParticipant());
        if(!event.getPicURL().equals("/images/default-profile.png")){
            imageView.setImage(new Image("file:"+event.getPicURL(), true));
        }else {
            imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
    }

    @FXML
    protected void onFinishClick() {
        String eventName = eventNameTextField.getText().trim();
        LocalDate eventDateStart = eventDatePickerStart.getValue();
        LocalDate eventDateEnd = eventDatePickerEnd.getValue();
        String timeStartEvent = timeStartEventTextField.getText().trim();
        String timeEndEvent = timeEndEventTextField.getText().trim();
        String amountTicket = amountTicketTextField.getText().trim();
        String amountParticipant = amountParticipantTextField.getText().trim();
        String detail = detailTextField.getText().trim();
        String timeEndTeam = timeEndTeamTextField.getText().trim();
        String timeEndParticipant = timeEndParticipantTextField.getText().trim();

        if (!eventName.isEmpty() && eventDateStart != null && eventDateEnd != null &&
                !timeStartEvent.isEmpty() && !timeEndEvent.isEmpty() &&
                !amountTicket.isEmpty() && !amountParticipant.isEmpty() &&
                !timeEndTeam.isEmpty() && !timeEndParticipant.isEmpty()) {

            changeNameDisplay(eventName);
            changeDateStart(eventDateStart, eventDateEnd);
            changeDateEnd(eventDateStart, eventDateEnd);
            changeTimeStartEvent(timeStartEvent, timeEndEvent);
            changeAmountTicket(amountTicket);
            changeAmountParticipant(amountParticipant);
            changeTimeEndTeam(timeEndTeam);
            changeTimeEndParticipant(timeEndParticipant);
            changeDetail(detail);

            if (errorMessage.equals("")) {
                boolean confirmFinish = showConfirmationDialog("Confirm Finish Event", "Are you sure you want to finish the event?");
                if (confirmFinish) {
                    eventListDatasource.writeData(eventList);
                    joinedEventDatasource.writeData(accountJoinList);

                    eventListDatasource.readData();
                    joinedEventDatasource.readData();
                    showInfo(event);
                    onBackClick();
                }
            } else {
                showErrorAlert(errorMessage);
                clearErrorMessage();
            }
        } else {
            showErrorAlert("Please fill all information.");
            clearErrorMessage();
        }
    }
    @FXML
    public  void onImageView(ActionEvent events){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG GIF", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        Node source = (Node) events.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                String[] fileSplit = file.getName().split("\\.");
                String filename = "Event_" + event.getEventName() + "_image" + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                imageView.setImage(new Image(target.toUri().toString()));
                event.setPicURL(destDir + "/" + filename);
                eventListDatasource.writeData(eventList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeNameDisplay(String name) {
        if (name.equals(event.getEventName())) {
            return;
        }
        try {
            if (name.length() < 3) {
                errorMessage += "EVENT NAME:\nLength of name be must more than 3.\n";
            } else {
                String thisEvent = event.getEventName();
                for (Account account1 : accountJoinList.getAccount()) {
                    System.out.println(account1.getAllEventUser());
                    for (String event1 : account1.getAllEventUser()) {
                        if (event1.equals(thisEvent)) {
                            account1.getEventName(event1);
                            System.out.println(account1.getName());
                            account1.deleteUserEventName(thisEvent);
                            account1.addUserEventName(name);
                            event.setEventName(name);
                        }
                    }
                }
            }
        } catch (Exception e) {
            errorMessage += "EVENT NAME:\nInvalid name.\n";
        }
    }

    private void changeDateStart(LocalDate startDate, LocalDate endDate) {
        if (startDate.isEqual(event.getStartDate())) {
            return;
        }
        try {
            if (currentDate.isBefore(startDate) && (endDate.isAfter(startDate) || startDate.isEqual(endDate))) {
                event.setStartDate(startDate);
            } else {
                errorMessage += "DATE START:\nStart date must be after the current date.\n";
            }
        } catch (Exception e) {
            errorMessage += "DATE START:\nInvalid Date.\n";
        }
    }

    private void changeDateEnd(LocalDate startDate, LocalDate endDate) {
        if (endDate.isEqual(event.getEndDate())){
            return;
        }
        try {
            if (currentDate.isBefore(endDate) && (endDate.isAfter(startDate) || startDate.isEqual(endDate))) {
                event.setEndDate(endDate);
            } else {
                errorMessage += "DATE END:\nEnd date must be after the current date and the Start Date.\n";
            }
        } catch (Exception e) {
            errorMessage += "DATE END:\nInvalid Date.\n";
        }
    }

    private void changeTimeStartEvent(String timeStartEvent, String timeEndEvent) {
        if (timeStartEvent.equals(event.getStartTime())) {
            return;
        }

        try {
            LocalTime startTime = LocalTime.parse(timeStartEvent, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime endTime = LocalTime.parse(timeEndEvent, DateTimeFormatter.ofPattern("HH:mm"));

            if (startTime.isBefore(endTime)) {
                if (startTime.isBefore(endTime.minusHours(3))) {
                    event.setStartTime(timeStartEvent);
                    event.setEndTime(timeEndEvent);
                } else {
                    errorMessage += "TIME EVENT:\nStart date must be at least 4 hours before the end date.\n";
                }
            } else {
                errorMessage += "TIME EVENT:\nEnd date must be after the Start Date.\n";
            }
        } catch (DateTimeParseException e) {
            errorMessage += "INVALID TIME EVENT:\nPlease use HH:mm format.\n";
        }
    }

    private void changeAmountTicket(String ticket) {
        if (Integer.parseInt(ticket) == event.getTicket()) {
            return;
        }

        try {
            int newTicketValue = Integer.parseInt(ticket);

            if (newTicketValue < event.getTicketLeft()) {
                errorMessage += "AMOUNT TICKET:\nTicket value cannot be less than the current ticket left\n";
            } else {
                event.setTicket(newTicketValue);
            }
        } catch (NumberFormatException e) {
            errorMessage += "INVALID AMOUNT TICKET:\nPlease enter a valid integer value for the ticket\n.";
        }
    }

    private void changeAmountParticipant(String participant) {
        if (Integer.parseInt(participant) == event.getParticipantNum()) {
            return;
        }

        try {
            int participantValue = Integer.parseInt(participant);

            if (participantValue < event.getParticipantLeft()) {
                errorMessage += "AMOUNT PARTICIPANT:\nParticipant value cannot be less than the current participant left\n.";
            } else {
                event.setParticipantNum(participantValue);
            }
        } catch (NumberFormatException e) {
            errorMessage += "INVALID AMOUNT PARTICIPANT:\nPlease enter a valid integer value for the participant\n.";
        }
    }

    private void changeTimeEndTeam(String TimeEndTeam) {
        if (TimeEndTeam.equals(event.getEndTime())) {
            return;
        }

        try {
            LocalTime selectedTime = LocalTime.parse(TimeEndTeam, DateTimeFormatter.ofPattern("HH:mm"));
            String time = selectedTime.toString();
            event.setTimeTeam(time);
        } catch (DateTimeParseException e) {
            errorMessage += "INVALID TIME END TEAM:\nPlease use HH:mm format.\n";
        }
    }

    private void changeTimeEndParticipant(String TimeEndParticipant) {
        if (TimeEndParticipant.equals(event.getTimeParticipant())) {
            return;
        }

        try {
            LocalTime selectedTime = LocalTime.parse(TimeEndParticipant, DateTimeFormatter.ofPattern("HH:mm"));
            String time = selectedTime.toString();
            event.setTimeParticipant(time);
        } catch (DateTimeParseException e) {
            errorMessage += "INVALID TIME END PARTICIPANT:\nPlease use HH:mm format.\n";
        }
    }

    private void changeDetail(String detail) {
        try {
            event.setDetail(detail);
        } catch (Exception e) {
            showErrorAlert("DETAIL:\nInvalid Text.\n");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == confirmButton;
    }

    private void clearErrorMessage() {
        errorMessage = "";
    }

    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("event-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = LocalDateTime.now().format(formatter);
        account.setTime(time);
        Datasource<AccountList> dataSource = new AccountListDatasource("data","user-info.csv");
        dataSource.writeData(accountList);
        FXRouter.goTo("login-page");
    }
}
