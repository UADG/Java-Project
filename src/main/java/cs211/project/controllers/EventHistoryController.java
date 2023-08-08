package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class EventHistoryController {
    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onEditDetailClick() {
        try {
            FXRouter.goTo("create-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onFinishActivityClick() {
        try {
            FXRouter.goTo("finish-activity");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onFixScheduleClick() {
        try {
            FXRouter.goTo("fix-team-schedule");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
