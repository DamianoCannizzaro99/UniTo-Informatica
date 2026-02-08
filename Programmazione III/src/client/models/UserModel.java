package client.models;

import bin.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private final ObjectProperty<User> account;

    public UserModel(int index) {
        this.account = new SimpleObjectProperty<User>();
        try {
            this.accountProperty().set(this.selectUser(index));
            System.out.println(this.accountProperty().get().getEmail());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the user of the current account
     */
    public ObjectProperty<User> accountProperty() {
        return this.account;
    }

    /**
     * It selects the user who is going to use the current mail box
     *
     * @param index represents the idex of the user to choose. it must be 0 <= index
     *              < 9
     * @throws IOException
     */
    protected User selectUser(int index) throws IOException {
        if (index < 0 || index > 8) {
            throw new IOException("Exception in class UserModel: index parameter must be in the range [0...9]");
        }
        return this.getUserFromTxt(index);
    }

    /**
     * Function used to get the selected user from the XML file containing every
     * account registered
     *
     * @param index represents the index of the chosen account
     * @return the user corresponding to the specified index
     */
    private User getUserFromTxt(int index) {
        return readFile().get(index);
    }

    public List<User> readFile(){
        List<User> userList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader
                ("src/bin/db.txt")))
        {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] information = sCurrentLine.split(" ");
                String nome = information[0];
                String cognome = information[1];
                String username = information[2];
                String email = information[3];
                String image = information[4];

                User u = new User(nome,cognome,username,email,image);
                userList.add(u);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
