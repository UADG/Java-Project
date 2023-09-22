package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.Event;
import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.EventList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
    public void OnSelectButton(ActionEvent events){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) events.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                String[] fileSplit = file.getName().split("\\.");
                String filename = "Event_" + event.getEventName() + "_image" + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                imageView.setImage(new Image(target.toUri().toString()));
                event.setPicURL(destDir + "/" + filename);
                eventListDatasource.writeData(eventList);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
