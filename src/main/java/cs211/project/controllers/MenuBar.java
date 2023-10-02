package cs211.project.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;

public class MenuBar {
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    public void initialize(){
        slide.setTranslateX(-200);
    }

    @FXML
    public void OnMenuBarClick() throws IOException {
        TranslateTransition slideAnimate = new TranslateTransition();
        slideAnimate.setDuration(Duration.seconds(0.5));
        slideAnimate.setNode(slide);
        slideAnimate.setToX(0);
        slideAnimate.play();
        menuButton.setVisible(false);
        slide.setTranslateX(0);
    }
    @FXML
    public void closeMenuBar() throws IOException {
        TranslateTransition slideAnimate = new TranslateTransition();
        slideAnimate.setDuration(Duration.seconds(0.5));
        slideAnimate.setNode(slide);
        slideAnimate.setToX(-200);
        slideAnimate.play();
        slide.setTranslateX(-200);
        slideAnimate.setOnFinished(event-> {
            menuButton.setVisible(true);
        });
    }
    @FXML
    public void onHomeClick() throws IOException {

    }
    @FXML
    public void onProfileClick() throws IOException {

    }
    @FXML
    public void onCreateEvent() throws IOException {

    }
    @FXML
    public void onJoinHistory() throws IOException {

    }
    @FXML
    public void onEventHis() throws IOException {

    }
    @FXML
    public void onPartiSchedule() throws IOException {

    }
    @FXML
    public void onTeamSchedule() throws IOException {

    }
    @FXML
    public void onComment() throws IOException {

    }
    @FXML
    public void onUserClick() throws IOException {

    }
}
