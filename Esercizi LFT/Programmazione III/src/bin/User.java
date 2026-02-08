package bin;

import java.io.Serializable;

public class User implements Serializable {
    private static int counter = 0;
    private final int ID;
    private final String nome;
    private final String cognome;
    private final String username;
    private final String email;
    private final String image;

    /**
     * Constructor for User class
     *
     * @param nome    represents the name of the person to whom the account is
     *                linked (ex. Mario)
     * @param cognome represents the surname of the person to whom the account is
     *                linked (ex. Rossi)
     * @param email   represents the email address of the person to whom the account
     *                is linked (ex. mariorossi@example.it)
     * @param image  represents the path for the current user profile picture
     */
    public User(String nome, String cognome, String username, String email, String image) {
        this.ID = counter++;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.image = image;
    }

    public int getID() {
        return this.ID;
    }

    public String getNome() {
        return this.nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getImage() {
        return this.image;
    }

    @Override
    public String toString() {
        return "Utente con ID: " + this.ID + "; Indirizzo email: \"" + this.email + "\"; Nome: \"" + this.nome
                + "\"; Cognome: \"" + this.cognome + "\"";
    }
}
