package client.controller;

import bin.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import client.models.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.GregorianCalendar;

public class EmailReceivedController {

  @FXML
  public BorderPane borderPane;
  @FXML
  public Label mittenteLabel;
  @FXML
  public Label destinatarioLabel;
  @FXML
  public Label argomentoLabel;
  @FXML
  public Label dataLabel;
  @FXML
  public TextArea testoTextArea;
  @FXML
  public Button replyButton;
  @FXML
  public Button replyallButton;
  @FXML
  public Button forwardButton;
  @FXML
  public Button deleteButton;

  private EmailModel emailModel;
  private UserModel userModel;
  private Server server;

  public void initModel(EmailModel emailModel, UserModel userModel,Server server ) {
    if (this.emailModel != null) {
      throw new IllegalStateException("Error in class EmailSideController: Email Model can only be initialized once");
    }
    if (this.userModel != null) {
      throw new IllegalStateException("Error in class EmailSideController: User Model can only be initialized once");
    }

    this.userModel = userModel;
    this.emailModel = emailModel;
    this.server=server;
    this.emailModel.currentEmailProperty().addListener((obs, oldEmail, newEmail) -> {
      if (oldEmail != null) {
        this.updateView(oldEmail);
      }
      if (newEmail == null) {
        this.updateView(null);
      } else {
        this.updateView(newEmail);
      }
    });

    this.updateView(null);
  }

  private void updateView(Email email) {
    if (email == null) {
      this.setAllInvisible();
    } else {
      this.updateEmail(email);
      this.setAllVisible();
    }
  }

  private void updateEmail(Email email) {
    final String receivers = this.makeDestinatari(email.getReceivers());
    final String date = this.format(email.getSendDate());
    this.mittenteLabel.textProperty().setValue(email.getSender());
    this.destinatarioLabel.setText(receivers);
    this.argomentoLabel.setText(email.getObject());
    this.dataLabel.setText(date);
    this.testoTextArea.setText(email.getText());
  }

  private void setAllInvisible() {
    this.borderPane.setVisible(false);
  }

  private void setAllVisible() {
    this.borderPane.setVisible(true);
  }

  private String makeDestinatari(List<String> destinatari) {
    String dest = "";
    for (String d : destinatari) {
      if (d != destinatari.get(destinatari.size() - 1)) {
        dest = dest+ d + ", ";
      }else
      dest =dest+d;
    }
    return dest;
  }

  private String takeDestinatari(List<String> destinatari) {
    String dest = "";
    for (String d : destinatari) {
      if (d != this.userModel.accountProperty().get().getEmail()) {
        dest += d;
        if (d != destinatari.get(destinatari.size() - 1)) {
          dest += ", ";
        }
      }
    }

    return dest;
  }

  private String format(GregorianCalendar date) {
    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
    formatDate.setCalendar(date);
    String dateFormatted = formatDate.format(date.getTime());

    return dateFormatted;
  }

  private void replyWindow(Button button, String destinatari, String arg, String text) throws IOException {
    BorderPane root = new BorderPane();

    FXMLLoader newEmailLoader = new FXMLLoader(getClass().getResource("/client/fxml/newEmail.fxml"));
    root.setLeft(newEmailLoader.load());

    NewEmailController newEmailController = newEmailLoader.getController();
    newEmailController.initModel(this.userModel,this.server);

    if(destinatari!=null)
      newEmailController.setDestinatari(destinatari);
    if (text!=null) {
      newEmailController.setEmailText(text);
      newEmailController.setOggetto(arg);
    }
    Scene scene = new Scene(root, 800, 600);
    Stage newStage = new Stage();
    newStage.setTitle("Scrivi email");
    newStage.setScene(scene);
    newStage.setResizable(false);

    Stage originalStage = (Stage) button.getScene().getWindow();
    originalStage.hide();

    newStage.showAndWait();

    originalStage.show();
  }

  @FXML
  private void onReplyButtonClick() throws IOException {
    String dest=this.mittenteLabel.getText();
    String arg="RE: " +this.argomentoLabel.getText();
    String text="\n\n" +
            "\n--- REPLY OF ---\n" +
            "\n\nEmail sent by: " + this.mittenteLabel.getText() +
            "\n\tto: " + this.makeDestinatari(this.emailModel.currentEmailProperty().get().getReceivers()) +
            "\n\tin date: " + this.dataLabel.getText() +
            "\n" + this.testoTextArea.getText() +
            "\n--- END OF EMAIL ---";
    this.replyWindow(this.replyButton,dest,arg,text);
  }

  @FXML
  private void onReplyAllButtonClick() throws IOException {
    String text="\n\n\n" +
            "\n--- REPLY OF ---\n" +
            "\n\nEmail sent by: " + this.mittenteLabel.getText() +
            "\n\tto: " + this.makeDestinatari(this.emailModel.currentEmailProperty().get().getReceivers()) +
            "\n\tin date: " + this.dataLabel.getText() +
            "\n" + this.testoTextArea.getText() +
            "\n--- END OF EMAIL ---";
    List<String>receivers=this.emailModel.currentEmailProperty().get().getReceivers();
    receivers.remove(this.userModel.accountProperty().get().getEmail());
    String allDest = takeDestinatari(receivers);
    String dest;
    String arg="RE: " +this.argomentoLabel.getText();
    if (allDest !=null)
    dest=this.mittenteLabel.getText()+", "+ allDest;
    else
      dest=this.mittenteLabel.getText();
    this.replyWindow(this.replyButton,dest,arg,text);
  }

  @FXML
  private void onForwardButtonClick() throws IOException {
    String arg=this.argomentoLabel.getText();
    String text="\n--- EMAIL FORWARDED ---\n" +
              "\n\nFrom: " + this.mittenteLabel.getText() +
              "\n\tin date: " + this.dataLabel.getText() +
              "\n\tObject: " + this.argomentoLabel.getText() +
              "\n\tto: " + this.makeDestinatari(this.emailModel.currentEmailProperty().get().getReceivers()) +
              this.testoTextArea.getText() +
              "\n--- END OF FORWARDED EMAIL ---";

      this.replyWindow(this.replyButton,null,arg,text);
  }

  @FXML
  private void onDeleteButtonClick() {
    Email eToDel = this.emailModel.getCurrentEmail();
    System.out.println("Delete email button pressed");
    this.emailModel.deleteMail();
    this.updateView(null);
    try{
      this.server.deleteEmail(eToDel);
      }catch(IOException e){
      throw new RuntimeException(e);
    }
  }



}
