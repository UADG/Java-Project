package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class ProfileSetPageController {
    private Account account = (Account) FXRouter.getData();
    @FXML Label usernameLabel;
    @FXML Label nameLabel;
    @FXML private Label myText;
    @FXML private Rectangle myRectangle;
    @FXML private ImageView imageView;
    @FXML public void initialize(){
        usernameLabel.setText(account.getUsername());
        nameLabel.setText(account.getName());
        myText.setVisible(false);
        myRectangle.setVisible(false);
        Image image = new Image(getClass().getResource("/images/default-profile.png").toExternalForm());
        //ต้องมี csv
        imageView.setImage(image);
    }

    @FXML
    private void onChooseButtonClick(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            account.setPictureURL(selectedFile.toURI().toString());
            imageView.setImage(image);
        }

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
    public void rePassButt(ActionEvent event) throws IOException {
        FXRouter.goTo("re-password", account);
    }
    @FXML
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("home-page", account);
    }
}
