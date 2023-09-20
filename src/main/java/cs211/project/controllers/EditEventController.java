package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.collections.EventList;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
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
    private Datasource<EventList> eventListDatasource;
    private EventList eventList;
    private Event event;
    private LocalDate currentDate;
    private String errorMessage;

    @FXML
    private void initialize() {
        clearErrorMessage();

        Event data = (Event) FXRouter.getData();
        currentDate = LocalDate.now();
        eventListDatasource = new EventListFileDatasource("data", "event-list.csv");
        eventList = eventListDatasource.readData();
        event = eventList.findEventByEventName(data.getEventName());
        showInfo(event);

        eventDatePickerEnd.setEditable(false);
        eventDatePickerStart.setEditable(false);
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
                    eventListDatasource.readData();
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

    private void changeNameDisplay(String name) {
        if (Objects.equals(name, event.getEventName())) {
            return;
        }
        try {
            if (name.length() < 3) {
                errorMessage += "EVENT NAME:\nLength of name be must more than 3.\n";
            } else {
                event.setEventName(name);
            }
        } catch (Exception e) {
            errorMessage += "EVENT NAME:\nInvalid name.\n";
        }
    }

    private void changeDateStart(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
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
        if (endDate == null){
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
        if (timeStartEvent.isEmpty()) {
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
        if (ticket.isEmpty()) {
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
        if (participant.isEmpty()) {
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
        if (TimeEndTeam.isEmpty()) {
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
        if (TimeEndParticipant.isEmpty()) {
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
            showErrorAlert("DETAIL:\nInvalid name.\n");
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
}
