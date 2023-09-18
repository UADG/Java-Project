package cs211.project.controllers;

import cs211.project.models.Event;
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
    private void initialize() {
        eventDatePickerEnd.setEditable(false);
        eventDatePickerStart.setEditable(false);
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

        if (!eventName.isEmpty() || eventDateStart != null || eventDateEnd != null ||
                !timeStartEvent.isEmpty() || !timeEndEvent.isEmpty() ||
                !amountTicket.isEmpty() || !amountParticipant.isEmpty() ||
                !detail.isEmpty() || !timeEndTeam.isEmpty() ||
                !timeEndParticipant.isEmpty()) {

            Event data = (Event) FXRouter.getData();
            changeNameDisplay(data, eventName);
            changeDateStart(data, eventDateStart);
            changeDateEnd(data, eventDateEnd);
            changeTimeStartEvent(data, timeStartEvent);
            changeTimeEndEvent(data, timeEndEvent);
            changeAmountTicket(data, amountTicket);
            changeAmountParticipant(data, amountParticipant);
            changeTimeEndTeam(data, timeEndTeam);
            changeTimeEndParticipant(data, timeEndParticipant);
            changeDetail(data, detail);
            clearInfo();

        } else {
            showErrorAlert("You must at least change one detail");
        }
    }

    private void changeNameDisplay(Event data,String name) {
        if (name == null || name.isEmpty()) {
            return;
        }
        try {
            if (name.equals(data.getEventName())) {
                showErrorAlert("Must not the same name");
            } else if (name.length() < 3) {
                showErrorAlert("Length of name be must more than 3");
            } else {
                data.setEventName(name);
            }
        } catch (Exception e) {
            showErrorAlert("Invalid name");
        }
    }

    private void changeDateStart(Event data, LocalDate date) {
        if (date == null){
            return;
        }
        try {
            data.setStartDate(date);
        } catch (Exception e) {
            showErrorAlert("Invalid Date");
        }
    }

    private void changeDateEnd(Event data, LocalDate date) {
        if (date == null){
            return;
        }
        try {
            data.setEndDate(date);
        } catch (Exception e) {
            showErrorAlert("Invalid Date");
        }
    }

    private void changeTimeStartEvent(Event data, String timeStartEvent) {
        if (timeStartEvent.isEmpty()) {
            return;
        }

        try {
            LocalTime selectedTime = LocalTime.parse(timeStartEvent, DateTimeFormatter.ofPattern("HH:mm"));
            String time = selectedTime.toString();
            data.setStartTime(time);
        } catch (DateTimeParseException e) {
            showErrorAlert("Invalid time format. Please use HH:mm format.");
        }
    }

    private void changeTimeEndEvent(Event data, String timeEndEvent){
        if (timeEndEvent.isEmpty()){
            return;
        }

        try {
            LocalTime selectedTime = LocalTime.parse(timeEndEvent, DateTimeFormatter.ofPattern("HH:mm"));
            String time = selectedTime.toString();
            data.setEndTime(time);
        } catch (DateTimeParseException e) {
            showErrorAlert("Invalid time format. Please use HH:mm format.");
        }
    }

    private void changeAmountTicket(Event data, String ticket) {
        if (ticket.isEmpty()) {
            return;
        }

        try {
            int newTicketValue = Integer.parseInt(ticket);
            int currentTicketValue = data.getTicket();

            if (newTicketValue == currentTicketValue) {
                showErrorAlert("Must not have the same ticket");
            } else if (newTicketValue < data.getTicketLeft()) {
                showErrorAlert("Ticket value cannot be less than the current ticket left");
            } else {
                data.setTicket(newTicketValue);
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Ticket: Please enter a valid integer value for the ticket");
        }
    }

    private void changeAmountParticipant(Event data, String participant) {
        if (participant.isEmpty()) {
            return;
        }

        try {
            int participantValue = Integer.parseInt(participant);
            int currentParticipantValue = data.getParticipantNum();

            if (participantValue == currentParticipantValue) {
                showErrorAlert("Must not have the same participant");
            } else if (participantValue < data.getParticipantLeft()) {
                showErrorAlert("Participant value cannot be less than the current participant left");
            } else {
                data.setParticipantNum(participantValue);
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Participant: Please enter a valid integer value for the participant");
        }
    }

    private void changeTimeEndTeam(Event data, String TimeEndTeam) {
        if (TimeEndTeam.isEmpty()) {
            return;
        }

        try {
            LocalTime selectedTime = LocalTime.parse(TimeEndTeam, DateTimeFormatter.ofPattern("HH:mm"));
            String time = selectedTime.toString();
            data.setTimeTeam(time);
        } catch (DateTimeParseException e) {
            showErrorAlert("Invalid time format. Please use HH:mm format.");
        }
    }

    private void changeTimeEndParticipant(Event data, String TimeEndParticipant) {
        if (TimeEndParticipant.isEmpty()) {
            return;
        }

        try {
            LocalTime selectedTime = LocalTime.parse(TimeEndParticipant, DateTimeFormatter.ofPattern("HH:mm"));
            String time = selectedTime.toString();
            data.setTimeParticipant(time);
        } catch (DateTimeParseException e) {
            showErrorAlert("Invalid time format. Please use HH:mm format.");
        }
    }

    private void changeDetail(Event data, String detail) {
        try {
            data.setDetail(detail);
        } catch (Exception e) {
            showErrorAlert("Invalid name");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearInfo() {
        eventNameTextField.setText("");
        eventDatePickerStart.setValue(null);
        eventDatePickerEnd.setValue(null);
        timeStartEventTextField.setText("");
        timeEndEventTextField.setText("");
        amountTicketTextField.setText("");
        amountParticipantTextField.setText("");
        detailTextField.setText("");
        timeEndTeamTextField.setText("");
        timeEndParticipantTextField.setText("");
    }

    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("event-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
