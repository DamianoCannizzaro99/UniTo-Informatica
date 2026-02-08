package client.controller;

import bin.Email;
import client.models.UserModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class NewEmailController {
    @FXML
    private Label mittenteLabel;
    @FXML
    private Label errorMsgLabel;
    @FXML
    private TextField destinatarioTxt;
    @FXML
    private TextField oggettoTxt;
    @FXML
    private TextArea messaggioTxt;
    @FXML
    private Button sendBtn;
    @FXML
    private Button cancelBtn;

    private UserModel userModel;
    private Server server;

    public void initModel(UserModel userModel,Server server) {
        if (this.userModel != null) {
            throw new IllegalStateException("Error in class UserController: Model can only be initialized once");
        }else {

            this.userModel = userModel;
            this.server = server;
            this.mittenteLabel.textProperty().setValue(this.userModel.accountProperty().get().getEmail());
            this.destinatarioTxt.textProperty().setValue("");
            this.oggettoTxt.textProperty().setValue("");
            this.messaggioTxt.textProperty().setValue("");
            this.errorMsgLabel.setVisible(false);
        }
    }

    public void setDestinatari(String destinatari){this.destinatarioTxt.setText(destinatari);}
    public void setEmailText(String emailText){this.messaggioTxt.setText(emailText);}
    public void setOggetto(String oggetto){this.oggettoTxt.setText(oggetto);}

    @FXML
    protected void onSendButtonClick(){

        System.out.println("Send button pressed");

        String mittente = this.mittenteLabel.textProperty().getValue();

        String object = this.oggettoTxt.getText();
        String text = this.messaggioTxt.getText();
        GregorianCalendar date= (GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getDefault());
        List<String> receivers = this.getReceivers();
        Email sendEmail=new Email(mittente,receivers,object,text,date);
        try {
            if(object != "" && !receivers.isEmpty()){
            server.sendEmail(sendEmail);
            this.closeStage();
            } else throw  new IllegalStateException("ERROR: mail not sent because object and/or receivers are empty.");
        } catch (Exception e) {
            this.errorMsgLabel.setVisible(true);
            this.errorMsgLabel.setText(e.getMessage());

        }


    }
    private List<String> getReceivers(){
        String[] dest = this.destinatarioTxt.getText().split(", ",0);
        List<String> receivers =new ArrayList<>();
        for(int i=0;i<dest.length;i++){
            System.out.println(dest[i]); //fixme aggiunta stampa getReceivers
            if (this.isEmailValid(dest[i])){
                receivers.add(dest[i]);
            }
        }
        return receivers;
    }
    @FXML
    protected void onCancelButtonClick(){
        this.closeStage();
    }

    public boolean isEmailValid(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$";
        Pattern p = Pattern.compile(regex);
        return p.matcher(email).matches();
    }

    private void closeStage() {
        Stage stage = (Stage) this.cancelBtn.getScene().getWindow();
        stage.close();
    }

}
