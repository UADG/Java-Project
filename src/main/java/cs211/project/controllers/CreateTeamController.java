package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class CreateTeamController {
    @FXML
    protected void back(){
        try {
            FXRouter.goTo("create-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void createTeam(){
        try {
            FXRouter.goTo("event-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
