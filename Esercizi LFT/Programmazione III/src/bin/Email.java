package bin;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

public class Email implements Serializable {
  private int id;
  private String sender;
  private List<String> receivers;
  private String object;
  private String text;
  private GregorianCalendar sendDate;
  private static int counter=0;

  public Email(String sender, List<String> receivers, String object, String text, GregorianCalendar sendDate) {
    this.id = counter++;
    this.sender = sender;
    this.receivers = receivers;
    this.object = object;
    this.text = text;
    this.sendDate = sendDate;
  }

  public int getId() {
    return id;
  }

  public String getSender() {
    System.out.println("Sender: " + sender);
    return sender;
  }

  public List<String> getReceivers() {
    return receivers;
  }
  public String getObject() {
    return object;
  }
  public String getText() {
    return text;
  }
  public GregorianCalendar getSendDate() {
    return sendDate;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return  " " + sender + " " + receivers + " " + object + " " + text + " " ;
  }
}
