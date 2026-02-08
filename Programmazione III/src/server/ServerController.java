package server;




import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import javafx.scene.control.TextArea;
import server.model.ServerModel;
import server.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerController {

    @FXML
    private Button exitBtn;
    @FXML
    private Button connectBtn;
    @FXML
    private Button disconnectBtn;
    @FXML
    private ListView<String> listLog;

    private ServerModel serverModel;
    private Server server;

    /**
     * initModel initializes the server model.
     * Local model can only be initialized once
     *
     * @param server represents the server model used. It cannot be null
     */
    public void initModel(Server server, ServerModel serverModel) {
        if (this.serverModel != null) {
            throw new IllegalStateException("Class ServerController error: ServerModel can only be initialized once");
        }
        if (this.server != null) {
            throw new IllegalStateException("Class ServerController error: server cannot be null");
        }
        this.server = server;
        this.serverModel = serverModel;

        this.listLog.itemsProperty().bind(this.serverModel.logProperty());

        this.connectBtn.setVisible(true);
        this.disconnectBtn.setVisible(false);

        }

    @FXML
    private void onConnectButtonClick() {
        this.server.startRunning();
        this.connectBtn.setVisible(false);
        this.disconnectBtn.setVisible(true);
        // this.activeButton(false);
    }
    @FXML
    private void onDisconnectButtonClick(){
        this.server.stopRunning();
        this.connectBtn.setVisible(true);
        this.disconnectBtn.setVisible(false);
        //this.activeButton(true);
    }
}