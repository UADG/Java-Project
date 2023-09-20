package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.EventList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class EventDetailsController {
    private Event event = (Event) FXRouter.getData();

    @FXML Label nameLabel;
    @FXML Label dateLabel;
    @FXML Label timeLabel;
    @FXML Label ticketLabel;
    @FXML Label descriptionLabel;
    @FXML ImageView imageView;
    public void initialize(){
        nameLabel.setText(event.getEventName());
        dateLabel.setText(event.getStartDate() + " - " + event.getEndDate());
        timeLabel.setText(event.getStartTime() + " - " + event.getEndTime());
        ticketLabel.setText(event.getTicketLeft() + "/" + event.getTicket());
        descriptionLabel.setText(event.getDetail());
    }
    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("events-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
