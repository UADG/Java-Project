package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class CreateEventController {
    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onNextClick() {
        try {
            FXRouter.goTo("create-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
