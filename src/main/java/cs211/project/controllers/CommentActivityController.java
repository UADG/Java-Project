package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CommentActivityController {
    @FXML
    private TextField commentTextField;

    @FXML
    private TextArea commentTextArea;

    public void initialize() {
        commentTextArea.setEditable(false);
    }

    @FXML
    private void onSentAction() {
        String commentText = commentTextField.getText();
        commentTextArea.appendText(commentText + "\n");
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
