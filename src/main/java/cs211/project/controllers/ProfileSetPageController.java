package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class ProfileSetPageController {
    @FXML
    private Label myText;
    @FXML
    private Rectangle myRectangle;
    @FXML
    public  void initialize(){
        myText.setVisible(false);
        myRectangle.setVisible(false);
    }
    public void onSaveClick(ActionEvent event) throws IOException {
        myText.setVisible(true);
        myRectangle.setVisible(true);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        myText.setVisible(false);
                        myRectangle.setVisible(false);
                    }
                },
                1000 // 1 second
        );
    }
    @FXML
    private ImageView imageView;

    @FXML
    private void onChooseButtonClick(ActionEvent event) {

    }
    public void rePassButt(ActionEvent event) throws IOException {
        FXRouter.goTo("re-password");
    }
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("home-page");
    }
}
