package client.models;

import bin.Email;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmailModel {
    private final ObservableList<Email> inbox;
    private final ObjectProperty<Email> currentEmail;

    /**
     * Constructor for EmailModel class
     */
    public EmailModel() {
        this.inbox = FXCollections.observableArrayList();
        this.currentEmail = new SimpleObjectProperty<>(null);
    }

    /**
     * @return a list of emails
     */
    public ObservableList<Email> inboxProperty() {
        return this.inbox;
    }

    /**
     * @return the email currently active
     */
    public ObjectProperty<Email> currentEmailProperty() {
        return this.currentEmail;
    }

    /**
     * It sets the current email to a certain specific email
     *
     * @param email represents the email that is going to be activated
     */
    public void setCurrentEmail(Email email) {
        this.currentEmailProperty().set(email);
    }

    /**
     * It gets the currently selected email
     *
     * @return the email which represents the one that is going to be read
     */
    public Email getCurrentEmail() {
        return this.currentEmailProperty().get();
    }

    /**
     * It deletes an email from the list of emails of the current account
     */
    public void deleteMail() {
        this.inboxProperty().remove(this.currentEmailProperty().get());
        this.setCurrentEmail(null);
    }

    /**
     * It adds an email to the list of emails of the current account
     *
     * @param email represents the email that is going to be added
     */
    public void addMail(Email email) {
        if (!this.inboxProperty().contains(email)) {
            this.inboxProperty().add(0, email);
        }

    }
}
