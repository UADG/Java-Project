package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Team;
import cs211.project.models.collections.TeamList;
import cs211.project.services.CommentTeamListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamListFileDatasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
    protected void backOnClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
