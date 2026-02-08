package client.models;

import client.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;

public class clientMain extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        BorderPane root = new BorderPane();

        FXMLLoader userLoader = new FXMLLoader(clientMain.class.getResource("/client/fxml/User.fxml"));
        root.setTop(userLoader.load());
        UserController userBarController = userLoader.getController();

        FXMLLoader tableLoader = new FXMLLoader(clientMain.class.getResource("/client/fxml/table.fxml"));
        root.setLeft(tableLoader.load());
        TableController tableController = tableLoader.getController();

        FXMLLoader emailLoader = new FXMLLoader(clientMain.class.getResource("/client/fxml/emailRecived.fxml"));
        root.setCenter(emailLoader.load());
        EmailReceivedController emailReceivedController = emailLoader.getController();

        int index = 2;
        UserModel userModel = new UserModel(index);
        Server server=new Server(userModel.accountProperty().get(),7430, InetAddress.getLocalHost().getHostName());
        server.connect();

        EmailModel emailModel = new EmailModel();
        userBarController.initModel(userModel,server);
        tableController.initModel(emailModel,server);
        emailReceivedController.initModel(emailModel, userModel,server);


        Scene scene = new Scene(root, 1280, 700);
        stage.setTitle("Email Client v_4.20");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


    private int askIndex() {
        int index = 10;

        do {
            System.out.print("\nInsert the index [0...8] of the account you would like to use: ");
            try {
                index = Integer.parseInt(System.console().readLine());
            } catch (NumberFormatException e) {
                System.out.println("You must insert a NUMBER!!!\n");
            }
        } while (index < 0 || index > 8);

        return index;
    }
}
