package cs211.project.controllers;

import javafx.event.ActionEvent;
import java.io.IOException;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

public class LoginPageController {
    @FXML
    public void loginButt(ActionEvent event) throws IOException {
        FXRouter.goTo("home-page");
    }
    public void registerLink(ActionEvent event) throws IOException {
        FXRouter.goTo("register-page");
    }
}
