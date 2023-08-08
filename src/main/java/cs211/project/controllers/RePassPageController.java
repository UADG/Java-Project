package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;

import java.io.IOException;

public class RePassPageController {
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("profile-setting");
    }
    public void onConfirmClick(ActionEvent event) throws IOException {
        FXRouter.goTo("profile-setting");
    }
}
