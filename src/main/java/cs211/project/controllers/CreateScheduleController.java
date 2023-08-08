package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class CreateScheduleController {
    @FXML
    protected void backOnClick(){
        try {
            FXRouter.goTo("create-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void nextOnClick(){
        try {
            FXRouter.goTo("create-team");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
