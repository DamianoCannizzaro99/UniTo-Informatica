package server.model;

import java.util.ArrayList;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Representation of the Server Model class
 */
public class ServerModel {
    private final ObservableList<String> logContent;
    private final ListProperty<String> log;

    public ServerModel() {
        this.logContent = FXCollections.observableList(new ArrayList<>());
        this.log = new SimpleListProperty<>();
        this.log.set(this.logContent);
    }

    /**
     * @return a list of strings
     */
    public ListProperty<String> logProperty() {
        return this.log;
    }

    /**
     * It adds a log to the list of logs of the server
     *
     * @param log represents the string that needs to be added
     */
    public void addLog(String log) {

        this.logContent.add(0, log);
    }
}
