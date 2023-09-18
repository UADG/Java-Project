package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Activity;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.CommentActivityListDatasource;
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

    private Datasource<ActivityList> commentDatasource;
    private ActivityList ActivityList;
    private String selectedActivity;


    @FXML
    private void initialize() {
        commentDatasource = new CommentActivityListDatasource("data", "team-comment.csv");
        ActivityList = commentDatasource.readData();

        ArrayList<Activity> activities = ActivityList.getAllActivities();
        ObservableList<String> activityNames = FXCollections.observableArrayList();
        activities.forEach(activity -> activityNames.add(activity.getActivityName()));
        chooseActivity.setItems(activityNames);

        chooseActivity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedName = newValue;
                selectedActivity = newValue;
                displayTextFlow(selectedName);
            }
        });
    }

    private void displayTextFlow(String name) {
        Activity activity = ActivityList.findActivityByName(name);

        if (activity != null) {
            String comment = activity.getComment();

            commentTextFlow.getChildren().clear();

            Text commentText = new Text(comment);
            commentTextFlow.getChildren().add(commentText);
        }
    }

    @FXML
    private void onSentAction() {
        Activity activity = ActivityList.findActivityByName(selectedActivity);
        Account account = (Account) FXRouter.getData();

        String commentText = commentTextField.getText();
        String indentation = "    ";

        Text boldNameText = new Text(indentation + account.getName() + "\n");
        boldNameText.setStyle("-fx-font-weight: bold");

        Text commentTextElement = new Text(indentation + commentText + "\n" + "\n");

        commentTextFlow.getChildren().addAll(boldNameText, commentTextElement);
        updateComment(selectedActivity, commentText);
        clearCommentInfo();
    }

    private void updateComment(String activityName, String comment) {
        Activity activity = ActivityList.findActivityByName(activityName);

        if (activity != null) {
            if (!activity.checkFirstComment(activity.getActivityName())) {
                activity.addComment(comment);
            } else {
                activity.addComment(activityName + "\n" + comment);
            }
            commentDatasource.writeData(ActivityList);
        }
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
