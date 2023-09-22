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
    private Account accounts = (Account) FXRouter.getData();
    Datasource<EventList> eventListDatasource = new EventListFileDatasource("data","event-list.csv");
    EventList eventList = eventListDatasource.readData();
    @FXML TextField nameEvent;
    @FXML DatePicker dateStart;
    @FXML DatePicker dateEnd;
    @FXML TextField timeStart;
    @FXML TextField timeEnd;
    @FXML TextField ticket;
    @FXML TextField parti;
    @FXML TextField detailLabel;
    @FXML TextField timeTeam;
    @FXML TextField timeParti;

    @FXML
    protected void onNextClick() {
        String eventName = nameEvent.getText();
        LocalDate startDate = dateStart.getValue();
        LocalDate endDate = dateEnd.getValue();
        String startTime = timeStart.getText();
        String endTime = timeEnd.getText();
        String tickets = ticket.getText();
        String participants = parti.getText();
        String detail = detailLabel.getText();
        String teamTime = timeTeam.getText();
        String partiTime = timeParti.getText();
        Event event = eventList.findEventByEventName(eventName);
        if(event==null) {
            if (!eventName.equals("") && startDate != null && endDate != null && !startTime.equals("") &&
                    !endTime.equals("") && !tickets.equals("") && !participants.equals("") && !detail.equals("") &&
                    !teamTime.equals("") && !partiTime.equals("")) {
                try {
                    LocalTime start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime teamT = LocalTime.parse(teamTime, DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime partiT = LocalTime.parse(partiTime, DateTimeFormatter.ofPattern("HH:mm"));
                    try {
                        int ticketNumber = Integer.parseInt(tickets);
                        int partiNumber = Integer.parseInt(participants);
                        eventList.addNewEvent(eventName,startDate,endDate,startTime, endTime,ticketNumber,
                                partiNumber, detail, teamTime, partiTime,0,0,"/images/default-profile.png", accounts.getUsername());
                        Datasource<EventList> dataSource = new EventListFileDatasource("data","event-list.csv");
                        dataSource.writeData(eventList);
                        try {
                            FXRouter.goTo("event-history", accounts);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (NumberFormatException e) {
                        showErrorAlert("Invalid number. Please enter a valid integer.");
                    }
                } catch (DateTimeParseException e){
                    showErrorAlert("Invalid time format. Please use HH:mm format.");
                }
            }else {
                showErrorAlert("Please fill all information.");
            }
        }else{
            showErrorAlert("This event's name already in used.");
        }
    }
    @FXML
    protected void onBackClick() {
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
        clear();
    }

    private void clear(){
        nameEvent.setText("");
        dateStart.setValue(null);
        dateEnd.setValue(null);
        timeStart.setText("");
        timeEnd.setText("");
        ticket.setText("");
        parti.setText("");
        detailLabel.setText("");
        timeTeam.setText("");
        timeParti.setText("");
    }
}
