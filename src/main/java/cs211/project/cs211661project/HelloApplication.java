package cs211.project.cs211661project;

import javafx.application.Application;
import javafx.stage.Stage;
import cs211.project.services.FXRouter;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        configRoute();

        FXRouter.bind(this, stage, "CS211 661 Project");
        FXRouter.goTo("events-list");
    }

    private static void configRoute() {
        String viewPath = "cs211/project/views/";
        FXRouter.when("hello", viewPath + "hello-view.fxml");
        FXRouter.when("event-details", viewPath + "event-details.fxml");
        FXRouter.when("event-schedule", viewPath + "event-schedule.fxml");
        FXRouter.when("events-list", viewPath + "events-list.fxml");
        FXRouter.when("joined-history", viewPath + "joined-history.fxml");
        FXRouter.when("team-schedule", viewPath + "team-schedule.fxml");
        FXRouter.when("participant-schedule", viewPath + "participant-schedule.fxml");
        FXRouter.when("ban-team", viewPath+"ban-team.fxml");
        FXRouter.when("comment-activity", viewPath+"comment-activity.fxml");
        FXRouter.when("create-team", viewPath+"create-team.fxml");
        FXRouter.when("create-schedule", viewPath+"create-schedule.fxml");
        FXRouter.when("fix-team-schedule", viewPath+"fix-team-schedule.fxml");
        FXRouter.when("create-event", viewPath+"create-event.fxml");
        FXRouter.when("edit-event", viewPath+"edit-event.fxml");
        FXRouter.when("event-history", viewPath+"event-history.fxml");
        FXRouter.when("finish-activity", viewPath+"finish-activity.fxml");
        FXRouter.when("home-page", viewPath+"home-page.fxml");
    }


    public static void main(String[] args) {
        launch();
    }
}