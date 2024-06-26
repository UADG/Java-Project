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
import java.util.Optional;

public class CreateEventController {
    @FXML
    private TextField nameEvent;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private TextField timeStart;
    @FXML
    private TextField timeEnd;
    @FXML
    private TextField ticket;
    @FXML
    private TextArea detailTextArea;
    @FXML
    private DatePicker startJoinDate;
    @FXML
    private DatePicker endJoinDate;
    @FXML
    private AnchorPane slide;
    @FXML
    private Button menuButton;
    @FXML
    private BorderPane bPane;
    @FXML
    private AnchorPane parent;
    @FXML
    private ImageView logoImageView;
    @FXML
    private DatePicker teamStart;
    @FXML
    private DatePicker teamEnd;
    private LocalDate currentDate;
    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String startTime;
    private String endTime;
    private String ticketNum;
    private LocalDate startJoin;
    private LocalDate endJoin;
    private String detail;
    private LocalTime timeEndEvent;
    private LocalTime timeStartEvent;
    private LocalDate startTeamDate;
    private LocalDate endTeamDate;
    private int tickets;
    private Boolean confirmFinish;
    private DateTimeFormatter formatter;
    private String time;
    private Datasource<AccountList> accountListDatasource;
    private Datasource<EventList> eventListDatasource;
    private AccountList accountList;
    private EventList eventList;
    private Account account;
    private Alert alert;
    private ButtonType confirmButton;
    private ButtonType cancelButton;
    private Optional<ButtonType> result;
    private String errorText;
    private Event event;
    private Node source;
    private File file;
    private File destDir;
    private String[] fileSplit;
    private String filename;
    private Path target;
    private String cssPath;
    private FileChooser chooser;
    private TranslateTransition slideAnimate;
    private Boolean isLightTheme;
    private Object[] objects;
    private String specialChar;

    @FXML
    private void initialize(){
        errorText = "";

        accountListDatasource = new AccountListDatasource("data","user-info.csv");
        eventListDatasource = new EventListFileDatasource("data","event-list.csv");

        eventList = eventListDatasource.readData();
        accountList = accountListDatasource.readData();

        dateStart.setEditable(false);
        dateEnd.setEditable(false);
        startJoinDate.setEditable(false);
        endJoinDate.setEditable(false);
        teamStart.setEditable(false);
        teamEnd.setEditable(false);
        bPane.setVisible(false);
        slide.setTranslateX(-200);

        detailTextArea.setWrapText(true);

        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        isLightTheme = (Boolean) objects[1];
        loadTheme(isLightTheme);

        if (isLightTheme) {
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
        } else {
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-dark-theme.png").toExternalForm()));
        }
    }

    @FXML
    protected void onNextClick(ActionEvent events) throws IOException {
        currentDate = LocalDate.now();
        eventName = nameEvent.getText();
        startDate = dateStart.getValue();
        endDate = dateEnd.getValue();
        startTime = timeStart.getText();
        endTime = timeEnd.getText();
        ticketNum = ticket.getText();
        startJoin = startJoinDate.getValue();
        endJoin = endJoinDate.getValue();
        detail = detailTextArea.getText();
        startTeamDate = teamStart.getValue();
        endTeamDate = teamEnd.getValue();

        event = eventList.findEventByEventName(eventName);

        if (event != null) {
            errorText += "This event's name already in used.\n";
            clear(nameEvent);
        } else {
            if (eventName.equals("") || startDate == null || endDate == null || startTime.equals("") || endTime.equals("")
                    || ticketNum.equals("") || startJoin == null || endJoin == null || startTeamDate == null || endTeamDate == null) {
                errorText += "Please fill all information.\n";
            }

            if (isContainSpecialCharacter(eventName)) {
                errorText += "EVENT NAME:\nEvent name must not contain special character.\n";
            }

            if (eventName.length()<3) {
                errorText += "EVENT NAME:\nLength of name must be more than 3.\n";
            }

            try {
                if (!currentDate.isBefore(startDate) && (!endDate.isAfter(startDate) || !startDate.isEqual(endDate))) {
                    errorText += "DATE START:\nStart date must be after the current date.\n";
                }
            } catch (Exception e) {
                errorText += "DATE START:\nInvalid Date.\n";
            }

            try {
                if (!currentDate.isBefore(endDate) && (!endDate.isAfter(startDate) || !startDate.isEqual(endDate))) {
                    errorText += "DATE END:\nEnd date must be after the current date and the Start Date.\n";
                }
            } catch (Exception e) {
                errorText += "DATE END:\nInvalid Date.\n";
            }

            try {
                timeStartEvent = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
                timeEndEvent = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));

                if (timeStartEvent.isBefore(timeEndEvent)) {
                    if (!timeStartEvent.isBefore(timeEndEvent.minusHours(3))) {
                        errorText += "TIME EVENT:\nStart time must be at least 4 hours before the end date.\n";
                    }
                } else {
                    errorText += "TIME EVENT:\nEnd time must be after the Start Date.\n";
                }
            } catch (DateTimeParseException e) {
                errorText += "INVALID TIME EVENT:\nPlease use HH:mm format.\n";
            }

            try {
                tickets = Integer.parseInt(ticketNum);

                if (tickets < 20) {
                    errorText += "AMOUNT TICKET:\nTicket value cannot be less than 20.\n";
                }
            } catch (NumberFormatException e) {
                errorText += "INVALID AMOUNT TICKET:\nPlease enter a valid integer value for the ticket.\n";
            }

            try {
                if (currentDate.isAfter(startJoin) || startJoin.isAfter(endJoin) || startJoin.isAfter(endDate)) {
                    errorText += "JOIN EVENT START DATE:\nJoin event start date must be after the current date\nand before the end date.\n";
                }
            } catch (Exception e) {
                errorText += "JOIN EVENT START DATE:\nInvalid Date.\n";
            }

            try {
                if (currentDate.isAfter(endJoin) || endJoin.isAfter(endDate) || endJoin.isBefore(startJoin)) {
                    errorText += "JOIN EVENT END DATE:\nJoin event end date must be after the current date,\njoin event start date and before the end date.\n";
                }
            } catch (Exception e) {
                errorText += "JOIN EVENT END DATE:\nInvalid Date.\n";
            }

            try {
                if (currentDate.isAfter(startTeamDate) || startTeamDate.isAfter(endTeamDate) || startTeamDate.isAfter(endDate)) {
                    errorText += "JOIN TEAM START DATE:\nJoin team start date must be after the current date\nand before the end date.\n";
                }
            } catch (Exception e) {
                errorText += "JOIN TEAM START DATE:\nInvalid Date.\n";
            }
            try {
                if (currentDate.isAfter(endTeamDate) || endTeamDate.isAfter(endDate) || endTeamDate.isBefore(startTeamDate)) {
                    errorText += "JOIN TEAM END DATE:\nJoin team end date must be after the current date,\njoin team start date and before the end date.\n";
                }
            } catch (Exception e) {
                errorText += "JOIN TEAM END DATE:\nInvalid Date.\n";
            }
        }

        if(errorText.equals("")) {
            confirmFinish = showConfirmationDialog("Confirm Finish Event", "Are you sure you want to finish the event?");
            if (confirmFinish){
                tickets = Integer.parseInt(ticketNum);
                eventList.addNewEvent(eventName, startDate, endDate, startTime, endTime, tickets,
                        detail, startJoin, endJoin, 0, "/images/default-event.png", account.getUsername(), startTeamDate, endTeamDate);

                eventListDatasource.writeData(eventList);
                eventList = eventListDatasource.readData();

                event = eventList.findEventByEventName(eventName);

                chooser = new FileChooser();
                chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG GIF", "*.png", "*.jpg", "*.jpeg", "*.gif"));
                source = (Node) events.getSource();
                file = chooser.showOpenDialog(source.getScene().getWindow());

                if (file != null){
                    try {
                        destDir = new File("images");
                        if (!destDir.exists()) destDir.mkdirs();
                        fileSplit = file.getName().split("\\.");
                        filename = "Event_" + event.getEventName() + "_image" + "."
                                + fileSplit[fileSplit.length - 1];
                        target = FileSystems.getDefault().getPath(
                                destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                        );
                        Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                        event.setPicURL(destDir + "/" + filename);
                        eventListDatasource.writeData(eventList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
                eventList = eventListDatasource.readData();
                event = eventList.findEventByEventName(eventName);
                accountListDatasource = new AccountListDatasource("data", "user-info.csv");
                accountList = accountListDatasource.readData();

                FXRouter.goTo("event-history", objects);
            }
        } else {
            showErrorAlert(errorText);
            errorText = "";
        }
    }

    private void showErrorAlert(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clear(TextField name){
        name.setText("");
    }

    private boolean showConfirmationDialog(String title, String message) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        result = alert.showAndWait();
        return result.isPresent() && result.get() == confirmButton;
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

    public boolean isContainSpecialCharacter(String cha) {
        specialChar = "~`!@#$%^&*()={[}]|\\:;\"'<,>.?/";
        for(char c : cha.toCharArray()){
            if (specialChar.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }
}
