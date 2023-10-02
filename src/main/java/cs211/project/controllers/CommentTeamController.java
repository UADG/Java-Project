package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.Team;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.time.LocalTime;

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
    private Datasource<TeamList> commentDatasource;
    private Datasource<AccountList> accountListDatasource;
    private Datasource<ActivityList> activityListDatasource;
    private ActivityList activityList;
    private TeamList commentTeam;
    private String selectedTeam;
    private Account account;
    private AccountList accountList;
    private Team team;


    @FXML
    private void initialize() {
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
                    if (accountList.findAccountByName(line.trim()) != null) {
                        commentTextElement.setFont(Font.font(commentTextElement.getFont().getFamily(), FontWeight.BOLD, commentTextElement.getFont().getSize()));
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

            if (accountList.findAccountByName(line.trim()) != null) {
                commentTextElement.setFont(Font.font(commentTextElement.getFont().getFamily(), FontWeight.BOLD, commentTextElement.getFont().getSize()));
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
    protected void backOnClick() {
        try {
            FXRouter.goTo("home-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
