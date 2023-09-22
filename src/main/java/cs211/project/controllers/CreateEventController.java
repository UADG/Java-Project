package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.EventList;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CreateEventController {
    private Account account = (Account) FXRouter.getData();
    private Datasource<EventList> eventListDatasource = new EventListFileDatasource("data","event-list.csv");
    private EventList eventList = eventListDatasource.readData();
    private String errorText = "";

    @FXML private TextField nameEvent;
    @FXML private DatePicker dateStart;
    @FXML private DatePicker dateEnd;
    @FXML private TextField timeStart;
    @FXML private TextField timeEnd;
    @FXML private TextField ticket;
    @FXML private TextField parti;
    @FXML private TextField detailLabel;
    @FXML private TextField timeTeam;
    @FXML private TextField timeParti;
    @FXML
    protected void onNextClick() {
        String eventName = nameEvent.getText();
        LocalDate startDate = dateStart.getValue();
        LocalDate endDate = dateEnd.getValue();
        String startTime = timeStart.getText();
        String endTime = timeEnd.getText();
        String ticketNum = ticket.getText();
        String participantNum = parti.getText();
        String detail = detailLabel.getText();
        String teamTime = timeTeam.getText();
        String partiTime = timeParti.getText();
        Event event = eventList.findEventByEventName(eventName);
        if(event!=null){
            errorText += "This event's name already in used.";
            clear(nameEvent);
            errorText = "";
        }else {

            if (eventName.equals("") || startDate == null || endDate == null || startTime.equals("") || endTime.equals("")
                    || ticketNum.equals("") || participantNum.equals("") || detail.equals("") || teamTime.equals("") || partiTime.equals("")) {
                errorText += "Please fill all information.\n";
            }

//            try {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                startDate.format(formatter);
//                endDate.format(formatter);
//            } catch (DateTimeParseException e) {
//                errorText += "Invalid date format. Please use yyyy-MM-dd format.\n";
//            }

            try {
                LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime.parse(teamTime, DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime.parse(partiTime, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                errorText += "Invalid time format. Please use HH:mm format.\n";
            }

            try {
                int tickets = Integer.parseInt(ticketNum);
                int participants = Integer.parseInt(participantNum);
            } catch (NumberFormatException e) {
                errorText += "Invalid number. Please enter a valid integer.";
            }
        }
        if(errorText.equals("")) {
            int tickets = Integer.parseInt(ticketNum);
            int participants = Integer.parseInt(participantNum);
            eventList.addNewEvent(eventName, startDate, endDate, startTime, endTime, tickets,
                    participants, detail, teamTime, partiTime, 0, 0, "/images/default-profile.png", account.getUsername());
            Datasource<EventList> dataSource = new EventListFileDatasource("data", "event-list.csv");
            dataSource.writeData(eventList);
            try {
                FXRouter.goTo("event-image", eventList.findEventByEventName(eventName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            showErrorAlert(errorText);
            errorText = "";
        }
    }

    @FXML
    private void onBackClick() {
        try {
            FXRouter.goTo("home-page");
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

    private void clear(TextField name){
        name.setText("");
    }
}
