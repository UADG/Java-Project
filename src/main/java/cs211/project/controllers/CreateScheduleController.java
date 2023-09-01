package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.collections.EventList;
import cs211.project.services.EventHardCode;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class CreateScheduleController {
//    @FXML
//    private Label activityNameLabel;
//    @FXML
//    private Label dateLabel;
//    @FXML
//    private Label timeStartLabel;
//    @FXML
//    private Label timeStopLabel;
//    @FXML
//    private ListView<ArrayList<ArrayList<String>>> activityListView;
//    private ArrayList<ArrayList<String>> activityList;
//
//    @FXML private Label errorLabel;
//
//    private ArrayList<String> selectedActivity;
//
//    @FXML
//    public void initialize() {
//        errorLabel.setText("");
//        clearEventInfo();
//        activityList = new ArrayList<ArrayList<String>>();
//        activityListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ArrayList<String>>() {
//            @Override
//            public void changed(ObservableValue<? extends ArrayList<String>> observable, ArrayList<String> oldValue, ArrayList<String> newValue) {
//                if (newValue.equals("")){
//                    clearEventInfo();
//                    selectedActivity = ;
//                } else {
//                    errorLabel.setText("");
//                    showEventInfo(newValue);
//                    selectedActivity = newValue;
//                }
//            }
//        });
//    }
//
//    private void showList(ArrayList<ArrayList<String>> activityList) {
//        activityListView.getItems().clear();
//        activityListView.getItems().addAll(activityList.());
//    }
//
//    private void showEventInfo(ArrayList<String> activity) {
//        activityNameLabel.setText(selectedActivity.get(0).);
//        dateLabel.setText("");
//        timeStartLabel.setText("");
//        timeStopLabel.setText("");
//    }
//
//    private void clearEventInfo() {
//        activityNameLabel.setText("");
//        dateLabel.setText("");
//        timeStartLabel.setText("");
//        timeStopLabel.setText("");
//    }
//
    @FXML
    protected void backOnClick(){
        try {
            FXRouter.goTo("create-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void nextOnClick(){
        try {
            FXRouter.goTo("create-team");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
