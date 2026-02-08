package client.controller;

import bin.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import client.models.*;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class TableController {

  @FXML
  public TableView<Email> tableView;
  @FXML
  private TableColumn<Email,String> mittenteColumn;
  @FXML
  public TableColumn<Email,String> argomentoColumn;
  @FXML
  public TableColumn<Email,GregorianCalendar> dataColumn;

  private EmailModel emailModel;
  private Server server;

  public void initModel(EmailModel emailModel,Server server) {
    if (this.emailModel != null) {
      throw new IllegalStateException("Error in class TableSideController: Model can only be initialized once");
    }

    this.emailModel = emailModel;
    this.server=server;
    final Label noMail = new Label("Nessuna Email ricevuta");

    this.tableView.setPlaceholder(noMail);
    this.tableView.setItems(this.emailModel.inboxProperty());
    this.tableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> this.emailModel.setCurrentEmail(newSelection));

    this.emailModel.currentEmailProperty().addListener((obs, oldEmail, newEmail) -> {
      if (newEmail == null) {
        tableView.getSelectionModel().clearSelection();
      } else {
        tableView.getSelectionModel().select(newEmail);
      }
    });

    this.initColumns();
    this.updateEmails();
  }

  /**
   * It sets each column of the table to its related value
   */
  private void initColumns() {
    mittenteColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
    argomentoColumn.setCellValueFactory(new PropertyValueFactory<>("object"));
    dataColumn.setCellValueFactory(new PropertyValueFactory<>("sendDate"));

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm");

    dataColumn.setCellFactory(col -> new TableCell<Email, GregorianCalendar>() {
      @Override
      protected void updateItem(GregorianCalendar date, boolean empty) {
        super.updateItem(date, empty);
        if (empty) {
          setText(null);
        } else {
          setText(dateFormat.format(date.getTime()));
        }
      }
    });
  }


  /**
   * It starts a thread which continuously checks for coming emails and, when it
   * finds one, it adds it to the list of emails of the current account
   */
  private void updateEmails() {
    Thread update = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          while (!server.isConnected()) {
            try {
              Thread.sleep(500);
            } catch (InterruptedException e) {
              System.out
                      .println("Exception in class TableSideController, unable to wake up the thread: " + e.getMessage());
            }
          }
          while (server.isConnected()) {
            Email newEmail = server.receiveEmail();
            if (newEmail != null)
              emailModel.addMail(newEmail);
          }
        }
      }
    });
    update.setDaemon(true);
    update.start();
  }

}
