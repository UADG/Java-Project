package cs211.project.controllers;

import cs211.project.models.Activity;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivityList;
import cs211.project.services.ActivityListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalTime;

public class FinishActivityController {
    @FXML public TableView activityTableView;
    @FXML public Label nameLabel;
    @FXML public Label timeStartLabel;
    @FXML public Label timeStopLabel;
    @FXML public Label dateLabel;
    private Activity selectedActivity;
    private ActivityListFileDatasource data;
    private ActivityList list;
    private String eventName;

    @FXML
    public void initialize(){
        clearInfo();
        updateData();
        eventName = ((Event)FXRouter.getData()).getEventName();
        list = data.readData();
        list.findActivityInEvent(eventName);
        showTable(list);

        activityTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Activity>() {
            @Override
            public void changed(ObservableValue observable, Activity oldValue, Activity newValue) {
                if (newValue == null) {
                    clearInfo();
                    selectedActivity = null;
                } else {
                    showInfo(newValue);
                    selectedActivity = newValue;
                }
            }
        });

    }

    public void showTable(ActivityList list){
        TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Activity Name");
        activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("activityName"));

        TableColumn<Activity, String> dateActivityColumn = new TableColumn<>("Date");
        dateActivityColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        TableColumn<Activity, LocalTime> startTimeActivityColumn = new TableColumn<>("Start-Time");
        startTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeActivity"));


        TableColumn<Activity, LocalTime> endTimeActivityColumn = new TableColumn<>("End-Time");
        endTimeActivityColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeActivity"));


        activityTableView.getColumns().clear();
        activityTableView.getColumns().add(activityNameColumn);
        activityTableView.getColumns().add(dateActivityColumn);
        activityTableView.getColumns().add(startTimeActivityColumn);
        activityTableView.getColumns().add(endTimeActivityColumn);

        activityTableView.getItems().clear();


        for (Activity activity: list.getActivities()) {
            System.out.println(activity.getActivityName());
            if(activity.getStatus().equals("0")) activityTableView.getItems().add(activity);
        }
    }

    public void showInfo(Activity activity){
        nameLabel.setText(activity.getActivityName());
        timeStartLabel.setText(activity.getStartTimeActivity());
        timeStopLabel.setText(activity.getEndTimeActivity());
        dateLabel.setText(activity.getDate());
    }

    public void clearInfo(){
        nameLabel.setText("");
        timeStopLabel.setText("");
        timeStartLabel.setText("");
        dateLabel.setText("");
    }

    public void updateData(){
        data = new ActivityListFileDatasource("data", "activity-list.csv");
    }

    public void finishActivity(){
        ActivityList list = data.readData();
        list.findActivityInEvent(selectedActivity.getEventName());
        list.setActivityStatus(selectedActivity.getActivityName(),"1");
        data.writeData(list);
        updateData();
        list = data.readData();
        list.findActivityInEvent(eventName);
        showTable(list);
    }


    @FXML
    protected void onBackClick() {
        try {
            FXRouter.goTo("event-history");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
