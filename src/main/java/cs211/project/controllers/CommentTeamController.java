package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Team;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.CommentTeamListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import cs211.project.services.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CommentTeamController {
    @FXML
    private TextField commentTextField;
    @FXML
    private ComboBox<String> chooseTeam;
    @FXML
    private TableView<Activity> activityTableView;
    @FXML
    private Label eventLabel;
    @FXML
    private ScrollPane commentScrollPane;
    @FXML
    private TextFlow commentTextFlow;
    @FXML
    private Button sendClick;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private BorderPane bPane;
    @FXML private AnchorPane parent;
    private Datasource<TeamList> commentDatasource;
    private Datasource<AccountList> accountListDatasource;
    private Datasource<ActivityList> activityListDatasource;
    private ActivityList activityList;
    private TeamList commentTeam;
    private String selectedTeam;
    private Account account;
    private AccountList accountList;
    private Team team;
    private ThemeDatasource themeDatasource = new ThemeDatasource("data", "theme.csv");
    private String theme = themeDatasource.read();

    @FXML
    private void initialize() {
        loadTheme(theme);
        commentTextField.setEditable(false);
        sendClick.setVisible(false);
        eventLabel.setVisible(false);

        commentDatasource = new CommentTeamListDatasource("data", "team-comment.csv");
        accountListDatasource = new AccountListDatasource("data", "user-info.csv");
        activityListDatasource = new ActivityListFileDatasource("data", "activity-list.csv");
        activityList = activityListDatasource.readData();
        commentTeam = commentDatasource.readData();
        accountList = accountListDatasource.readData();
        account = (Account) FXRouter.getData();

        team = new Team("",1,1,"");
        chooseTeam.getItems().addAll(team.getListTeam(account.getId()));
        System.out.println(team.getEvent());

        chooseTeam.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                clearCommentTextFlow();
                commentTextField.setEditable(true);
                sendClick.setVisible(true);
                eventLabel.setVisible(true);
                selectedTeam = newValue;

                team = commentTeam.checkTeamExist(selectedTeam);
                showInfo();
                team.setFirstComment("");

                String[] commentLines = team.getComment().split("\\\\n");
                for (String line : commentLines) {
                    Text commentTextElement = new Text(line + "\n");
                    commentTextElement.setStyle("-fx-fill: white;");
                    if (accountList.findAccountByName(line.trim()) != null) {
                        commentTextElement.setStyle("-fx-font-weight: bold; -fx-fill: white;");
                    }
                    commentTextFlow.getChildren().add(commentTextElement);
                }
                commentScrollPane.setVvalue(1.0);

                activityTableView.getItems().clear();
                activityTableView.getColumns().clear();
                activityList = activityListDatasource.readData();
                activityList.findActivityInEvent(team.getEvent().getEventName());
                showTable(activityList);
            }
        });
        bPane.setVisible(false);
        slide.setTranslateX(-200);
    }

    @FXML
    private void onSentAction() {
        commentTextFlow.getChildren().clear();

        team = commentTeam.checkTeamExist(selectedTeam);

        String commentText = commentTextField.getText();
        if (!commentText.trim().equals("")) {
            if (team.getComment().equals("")) {
                commentText = account.getName() + "\\n" + commentText;
                team.addComment(commentText);
            } else if (team.checkFirstComment(account.getName())) {
                commentText = team.getComment() + "\\n" + "\\n" + account.getName() + "\\n" + commentText;
                team.addComment(commentText);
            } else {
                commentText = team.getComment() + "\\n" + commentText;
                team.addComment(commentText);
            }

            commentDatasource.writeData(commentTeam);
            commentDatasource.readData();

            team = commentTeam.checkTeamExist(selectedTeam);

        }
        String[] commentLines = team.getComment().split("\\\\n");

        for (String line : commentLines) {
            Text commentTextElement = new Text(line + "\n");
            commentTextElement.setStyle("-fx-fill: white;");
            if (accountList.findAccountByName(line.trim()) != null) {
                commentTextElement.setStyle("-fx-font-weight: bold; -fx-fill: white;");
            }
            commentTextFlow.getChildren().add(commentTextElement);
        }
        team.setFirstComment(account.getName());
        clearCommentInfo();

        commentScrollPane.setVvalue(1.0);
    }

    private void showTable(ActivityList activityList) {
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, String> dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));

        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));

        TableColumn<Activity, LocalTime> teamColumn = new TableColumn<>("team");
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<Activity, String> statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(cellData -> {
            int status = Integer.parseInt(cellData.getValue().getStatus());
            if (status == 1) {
                return new SimpleStringProperty("Finish");
            } else {
                return new SimpleStringProperty("Still Organize");
            }
        });

        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);
        activityTableView.getColumns().add(teamColumn);
        activityTableView.getColumns().add(statusColumn);

        for (Activity activity: activityList.getActivities()) {
            activityTableView.getItems().add(activity);
        }

    }

    public void clearCommentInfo() {
        commentTextField.setText("");
    }

    public void clearCommentTextFlow() {
        commentTextFlow.getChildren().clear();
    }

    public void showInfo() {
        eventLabel.setText(team.getEvent().getEventName());
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
        bPane.setVisible(true);
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
            bPane.setVisible(false);
        });
    }
    @FXML
    public void onHomeClick() throws IOException {
        FXRouter.goTo("events-list", account);
    }
    @FXML
    public void onProfileClick() throws IOException {
        FXRouter.goTo("profile-setting", account);
    }
    @FXML
    public void onCreateEvent() throws IOException {
        FXRouter.goTo("create-event", account);
    }
    @FXML
    public void onJoinHistory() throws IOException {
        FXRouter.goTo("joined-history", account);
    }
    @FXML
    public void onEventHis() throws IOException {
        FXRouter.goTo("event-history", account);
    }
    @FXML
    public void onPartiSchedule() throws IOException {
        FXRouter.goTo("participant-schedule", account);
    }
    @FXML
    public void onTeamSchedule() throws IOException {
        FXRouter.goTo("team-schedule", account);
    }
    @FXML
    public void onComment() throws IOException {
        FXRouter.goTo("comment-activity", account);
    }
    @FXML
    public void onLogOutButton() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = LocalDateTime.now().format(formatter);
        account.setTime(time);
        Datasource<AccountList> dataSource = new AccountListDatasource("data","user-info.csv");
        dataSource.writeData(accountList);
        FXRouter.goTo("login-page");
    }
    private void loadTheme(String themeName) {
        if (parent != null) {
            String cssPath = "/cs211/project/views/" + themeName;
            parent.getStylesheets().clear();
            parent.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
    }
}
