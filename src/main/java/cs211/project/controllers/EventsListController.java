package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class EventsListController {
    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onDetailClick() {
        try {
            FXRouter.goTo("event-details");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onBookTicketsClick() {
        try {
            FXRouter.goTo("event-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
