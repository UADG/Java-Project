package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.CommentTeamListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.ArrayList;

public class CommentActivityController {
    @FXML
    private TextField commentTextField;
    @FXML
    private ComboBox<String> chooseActivity;
    @FXML
    private TableView<TeamList> teamListTableView;
    @FXML
    private Label activityLabel;
    @FXML
    private Label teamLabel;
    @FXML
    private TextFlow commentTextFlow;

    private Datasource<TeamList> commentDatasource;
    private TeamList teamList;
    private String selectedActivity;


    @FXML
    private void initialize() {
        commentDatasource = new CommentTeamListDatasource("data", "team-comment.csv");
        teamList = commentDatasource.readData();

        ArrayList<Team> teams = teamList.getTeams();
        ObservableList<String> teamNames = FXCollections.observableArrayList();
        teams.forEach(team -> teamNames.add(team.getTeamName()));
        chooseActivity.setItems(teamNames);

        chooseActivity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedName = newValue;
                selectedActivity = newValue;
            }
        });
    }

    @FXML
    private void onSentAction() {
        Account account = (Account) FXRouter.getData();

        String commentText = commentTextField.getText();
        String indentation = "    ";

        Text boldNameText = new Text(indentation + account.getName() + "\n");
        boldNameText.setStyle("-fx-font-weight: bold");

        Text commentTextElement = new Text(indentation + commentText + "\n" + "\n");

        commentTextFlow.getChildren().addAll(boldNameText, commentTextElement);
        clearCommentInfo();
    }

    public void clearCommentInfo(){
        commentTextField.setText("");
    }

    @FXML
    protected void backOnClick(){
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
