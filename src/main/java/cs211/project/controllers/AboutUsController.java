package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class AboutUsController {
    @FXML private ImageView bestImage;
    @FXML private ImageView winImage;
    @FXML private ImageView aiImage;
    @FXML private ImageView jimImage;
    @FXML private TabPane parent1;
    private Boolean isLightTheme;
    public void initialize(){
        isLightTheme = (Boolean) FXRouter.getData();
        loadTheme(isLightTheme);
        bestImage.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
        winImage.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
        aiImage.setImage(new Image(getClass().getResource("/images/ai-image.jpg").toExternalForm()));
        jimImage.setImage(new Image(getClass().getResource("/images/jim-image.jpg").toExternalForm()));
    }
    @FXML
    private void onBackClick() throws IOException {
        FXRouter.goTo("login-page");
    }
    private void loadTheme(Boolean theme) {
        if (theme) {
            loadTheme("st-theme.css");
        } else {
            loadTheme("dark-theme.css");
        }
    }

    private void loadTheme(String themeName) {
        if (parent1 != null) {
            String cssPath = "/cs211/project/views/" + themeName;
            parent1.getStylesheets().clear();
            parent1.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
