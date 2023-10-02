package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Team;
import cs211.project.models.collections.TeamList;
import cs211.project.services.CommentTeamListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamListFileDatasource;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class CommentTeamController {
    @FXML
    private TextField commentTextField;
    @FXML
    private ComboBox<String> chooseTeam;
    @FXML
    private TableView<TeamList> teamListTableView;
    @FXML
    private Label teamLabel;
    @FXML
    private ScrollPane commentScrollPane;
    @FXML
    private TextFlow commentTextFlow;
    @FXML
    private Button sendClick;
    @FXML private AnchorPane slide;
    @FXML private Button menuButton;
    @FXML private Button adminButton;
    @FXML private BorderPane bPane;
    private Datasource<TeamList> commentDatasource;
    private Datasource<TeamList> teamListDatasource;
    private TeamList teamList;
    private TeamList commentTeam;
    private String selectedTeam;
    private Account account;
    private Team team;


    @FXML
    private void initialize() {
        commentTextField.setEditable(false);
        sendClick.setVisible(false);
        teamLabel.setVisible(false);

        teamListDatasource = new TeamListFileDatasource("data", "team.csv");
        commentDatasource = new CommentTeamListDatasource("data", "team-comment.csv");
        teamList = teamListDatasource.readData();
        commentTeam = commentDatasource.readData();
        account = (Account) FXRouter.getData();

        ArrayList<Team> teams = teamList.getTeams();
        ObservableList<String> teamNames = FXCollections.observableArrayList();
        teams.forEach(team -> teamNames.add(team.getTeamName()));
        chooseTeam.setItems(teamNames);

        chooseTeam.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                commentTextField.setEditable(true);
                sendClick.setVisible(true);
                teamLabel.setVisible(true);
                selectedTeam = newValue;
                showInfo();

                team = commentTeam.checkTeamExist(selectedTeam);
                team.setFirstComment("");

                String[] commentLines = team.getComment().split("\\\\n");
                for (String line : commentLines) {
                    Text commentTextElement = new Text(line + "\n");
                    if (line.contains(account.getName())) {
                        commentTextElement.setFont(Font.font(commentTextElement.getFont().getFamily(), FontWeight.BOLD, commentTextElement.getFont().getSize()));
                    }
                    commentTextFlow.getChildren().add(commentTextElement);
                }
                commentScrollPane.setVvalue(1.0);
            }
        });
        bPane.setVisible(false);
        slide.setTranslateX(-200);
        adminButton.setVisible(false);
        if(account.isAdmin(account.getRole())){
            adminButton.setVisible(true);
        }
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

            if (line.contains(account.getName())) {
                commentTextElement.setFont(Font.font(commentTextElement.getFont().getFamily(), FontWeight.BOLD, commentTextElement.getFont().getSize()));
            }
            commentTextFlow.getChildren().add(commentTextElement);
        }
        team.setFirstComment(account.getName());
        clearCommentInfo();

        commentScrollPane.setVvalue(1.0);
    }

    public void clearCommentInfo() {
        commentTextField.setText("");
    }

    public void showInfo() {
        teamLabel.setText(selectedTeam);
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
    public void onUserClick() throws IOException {
        FXRouter.goTo("user-status", account);
    }
}
