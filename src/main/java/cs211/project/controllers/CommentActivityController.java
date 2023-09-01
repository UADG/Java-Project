package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;

public class CommentActivityController {
    @FXML
    private TextField commentTextField;

    @FXML
    private TextFlow commentTextFlow;

    @FXML
    private void initialize() {
        String endLine = "\n";
        commentTextFlow.getChildren().add(new Text(endLine));
    }

    @FXML
    private void onSentAction() {
        String commentText = commentTextField.getText();
        Account account = (Account) FXRouter.getData();

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
