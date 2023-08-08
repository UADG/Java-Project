package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class EventDetailsController {
    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("events-list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
