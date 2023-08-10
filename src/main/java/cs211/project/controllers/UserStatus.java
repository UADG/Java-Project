package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;

import java.io.IOException;

public class UserStatus{
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("home-page");
    }
}
