package server;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.stage.WindowEvent;
import server.ServerController;
import server.model.ServerModel;

public class ServerMain extends Application {
   @Override
    public void start(Stage stage) throws IOException{
       BorderPane root = new BorderPane();

      FXMLLoader serverLoader = new FXMLLoader(getClass().getResource("/server/fxml/server.fxml"));
      root.setCenter(serverLoader.load());
      ServerController serverController = serverLoader.getController();

      ServerModel serverModel = new ServerModel();
      Server server = new Server(7430,serverModel);
      serverController.initModel(server,serverModel);

      Scene scene = new Scene(root,580,580);
      stage.setTitle("server");
      stage.setScene(scene);
      stage.setResizable(false);
      stage.show();
      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent arg0) {
              server.stopRunning();
          }
      });
   }

   public static void main(String[]args){
       System.out.println("Server initialization started...");

       launch(args);
   }
}
