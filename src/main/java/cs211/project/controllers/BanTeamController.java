package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class BanTeamController {
    @FXML
    protected void backOnClick(){
        try {
            FXRouter.goTo("event-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
