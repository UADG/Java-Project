package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.EventList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class EventImageController {
    private Event event = (Event) FXRouter.getData();
    private Datasource<EventList> eventListDatasource = new EventListFileDatasource("data","event-list.csv");
    private EventList eventList = eventListDatasource.readData();
    private Datasource<AccountList> accountListDatasource = new AccountListDatasource("data","user-info.csv");
    private AccountList accountList = accountListDatasource.readData();
    private Account account = accountList.findAccountByUsername(event.getEventManager());
    @FXML private ImageView imageView;

    public void initialize(){
        Image image = new Image(getClass().getResource(event.getPicURL()).toString());
        imageView.setImage(image);
    }
    public void OnSelectButton(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            event.setPicURL(selectedFile.toURI().toString());
            System.out.println(event.getPicURL());
            imageView.setImage(image);
            eventListDatasource.writeData(eventList);
        }
    }
    public void OnNextClick(){
        try {
            FXRouter.goTo("event-history", account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
