package client.controller;

import client.models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.IOException;

public class UserController {
    @FXML
    private ImageView imageLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Button logOutBtn;
    @FXML
    private Button writeBtn;

    private UserModel userModel;
    private Server server;

    public void initModel(UserModel userModel,Server server) {
        if (this.userModel != null) {
            throw new IllegalStateException("Error in class UserController: Model can only be initialized once");
        }

        this.userModel = userModel;
        this.server=server;
        this.imageLabel.imageProperty()
                .set(new Image("/bin/image/"+this.userModel.accountProperty().get().getImage() ));
        this.usernameLabel.textProperty().setValue(this.userModel.accountProperty().get().getUsername());
        this.emailLabel.textProperty().setValue(this.userModel.accountProperty().get().getEmail());
    }

    /**
     * It creates a view to write an email and send it
     *
     * @throws IOException iff the scriviEmailLoader gives problems
     */
    @FXML
    private void onWriteButtonClick() throws IOException {
        BorderPane root = new BorderPane();

        FXMLLoader newEmailLoader = new FXMLLoader(getClass().getResource("/client/fxml/newEmail.fxml"));
        root.setLeft(newEmailLoader.load());

        NewEmailController newEmailController = newEmailLoader.getController();
        newEmailController.initModel(this.userModel,this.server);

        Scene scene = new Scene(root, 800, 600);
        Stage newStage = new Stage();
        newStage.setTitle("Scrivi email");
        newStage.setScene(scene);
        newStage.setResizable(false);

        Stage originalStage = (Stage) this.writeBtn.getScene().getWindow();
        originalStage.hide();

        newStage.showAndWait();

        originalStage.show();
    }

    /**
     * It closes the current window
     */
    @FXML
    private void onLogoutButtonClick() {
        Stage stage = (Stage) this.logOutBtn.getScene().getWindow();
        stage.close();
    }

}
