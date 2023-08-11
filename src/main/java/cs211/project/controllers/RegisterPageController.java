package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;

import java.io.IOException;

public class RegisterPageController {
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("login-page");
    }
    public void onConfirmClick(ActionEvent event) throws IOException {
        FXRouter.goTo("login-page");
    }
}
