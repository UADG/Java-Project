package cs211.project.controllers;

import cs211.project.models.Account;
import cs211.project.models.collections.AccountList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ProfileSetPageController {
    private Account accounts = (Account) FXRouter.getData();
    Datasource<AccountList> accountListDataSource = new AccountListDatasource("data","user-info.csv");
    AccountList accountList = accountListDataSource.readData();
    private Account account = accountList.findAccountByUsername(accounts.getUsername());
    @FXML Label usernameLabel;
    @FXML Label nameLabel;
    @FXML private Label myText;
    @FXML private Rectangle myRectangle;
    @FXML private ImageView imageView;
    @FXML public void initialize(){
        usernameLabel.setText(account.getUsername());
        nameLabel.setText(account.getName());
        myText.setVisible(false);
        myRectangle.setVisible(false);
        if(!account.getPictureURL().equals("/images/default-profile.png")){
            imageView.setImage(new Image("file:"+account.getPictureURL(), true));
        }else {
            imageView.setImage(new Image(getClass().getResource("/images/default-profile.png").toExternalForm()));
        }
    }

    @FXML
    private void onChooseButtonClick(ActionEvent event){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                String[] fileSplit = file.getName().split("\\.");
                String filename = "account_" + account.getName() + "_image" + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                imageView.setImage(new Image(target.toUri().toString()));
                account.setPictureURL(destDir + "/" + filename);
                accountListDataSource.writeData(accountList);
                myText.setVisible(true);
                myRectangle.setVisible(true);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                myText.setVisible(false);
                                myRectangle.setVisible(false);
                            }
                        },
                        1000 // 1 second
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void rePassButt(ActionEvent event) throws IOException {
        FXRouter.goTo("re-password", account);
    }
    @FXML
    public void onBackClick(ActionEvent event) throws IOException {
        FXRouter.goTo("home-page", account);
    }
}
