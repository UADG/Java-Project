package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDetailsController {
    @FXML
    private AnchorPane parent;
    @FXML
    private Label nameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label ticketLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane slide;
    @FXML
    private Button menuButton;
    @FXML
    private BorderPane bPane;
    @FXML
    private HBox hBox;
    @FXML
    private Label bookDateLabel;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ScrollPane descriptionScroll;
    private Event event;
    private Datasource<AccountList> accountListDatasource;
    private AccountList accountList;
    private Account account;
    private Object[] objects;
    private Object[] objectsSent;
    private Boolean isLightTheme;
    private TranslateTransition slideAnimate;
    private DateTimeFormatter formatter;
    private String time;
    private String cssPath;

    public void initialize(){
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        accountList = accountListDatasource.readData();

        objects = (Object[]) FXRouter.getData();
        account = (Account) objects[0];
        event = (Event) objects[1];
        isLightTheme = (Boolean) objects[2];

        if (isLightTheme) {
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-light-theme.png").toExternalForm()));
        } else {
            logoImageView.setImage(new Image(getClass().getResource("/images/logo-dark-theme.png").toExternalForm()));
        }

        objectsSent = new Object[2];
        objectsSent[0] = account;
        objectsSent[1] = isLightTheme;
        loadTheme(isLightTheme);

        if (!event.getPicURL().equals("/images/default-event.png")) {
            imageView.setImage(new Image("file:"+event.getPicURL(), true));
        } else {
            imageView.setImage(new Image(getClass().getResource("/images/default-event.png").toExternalForm()));
        }
        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        nameLabel.setText(event.getEventName());
        dateLabel.setText(event.getStartDate() + " - " + event.getEndDate());
        bookDateLabel.setText(event.getStartJoinDate() + " - " + event.getEndJoinDate());
        timeLabel.setText(event.getStartTime() + " - " + event.getEndTime());
        ticketLabel.setText(event.getTicketLeft() + "/" + event.getTicket());
        descriptionLabel.setPrefWidth(667);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setText(event.getDetail());
        descriptionScroll.setContent(descriptionLabel);
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("events-list", objectsSent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void OnMenuBarClick() throws IOException {
        slideAnimate = new TranslateTransition();
        slideAnimate.setDuration(Duration.seconds(0.5));
        slideAnimate.setNode(slide);
        slideAnimate.setToX(0);
        slideAnimate.play();
        menuButton.setVisible(false);
        slide.setTranslateX(0);
        bPane.setVisible(true);
    }
    @FXML
    public void closeMenuBar() throws IOException {
        slideAnimate = new TranslateTransition();
        slideAnimate.setDuration(Duration.seconds(0.5));
        slideAnimate.setNode(slide);
        slideAnimate.setToX(-200);
        slideAnimate.play();
        slide.setTranslateX(-200);
        slideAnimate.setOnFinished(event-> {
            menuButton.setVisible(true);
            bPane.setVisible(false);
        });
    }

    @FXML
    public void onHomeClick() throws IOException {
        FXRouter.goTo("events-list", objectsSent);
    }

    @FXML
    public void onProfileClick() throws IOException {
        FXRouter.goTo("profile-setting", objectsSent);
    }

    @FXML
    public void onCreateEvent() throws IOException {
        FXRouter.goTo("create-event", objectsSent);
    }

    @FXML
    public void onJoinHistory() throws IOException {
        FXRouter.goTo("joined-history", objectsSent);
    }

    @FXML
    public void onEventHis() throws IOException {
        FXRouter.goTo("event-history", objectsSent);
    }

    @FXML
    public void onPartiSchedule() throws IOException {
        FXRouter.goTo("participant-schedule", objectsSent);
    }

    @FXML
    public void onTeamSchedule() throws IOException {
        FXRouter.goTo("team-schedule", objectsSent);
    }

    @FXML
    public void onComment() throws IOException {
        FXRouter.goTo("comment-activity", objectsSent);
    }

    @FXML
    public void onLogOutButton() throws IOException {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        time = LocalDateTime.now().format(formatter);
        account.setTime(time);
        accountListDatasource.writeData(accountList);
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
        if (parent != null) {
            cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
