package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class CommentActivityController {
    @FXML
    protected void backOnClick(){
        try {
            FXRouter.goTo("team-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
