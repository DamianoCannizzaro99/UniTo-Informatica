package client.controller;

import bin.Email;
import bin.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class used by the client to establish a connection to the server and so send
 * and receive messages
 */
public class Server {
    private final int port;
    private final String address;
    private final User user;
    private boolean active;
    private ObjectInputStream clientIn;
    private ObjectOutputStream clientOut;
    private Socket socket;
   /**
     * Constructor for Server class
     *
     * @param user    represents the index of the current user (same used for
     *                UserModel)
     * @param port    represents the port of the server
     * @param address represents the address of the server
     */
    public Server(User user, int port, String address) {
        this.port = port;
        this.address = address;
        this.user = user;
        this.active=false;
    }

    /**
     * Function to establish a connection to the server. This method must be the
     * first one called, before any else
     */
    public void connect() {
        Thread clientConnect = new Thread(() -> {
            while (true) {
                if (!active) {
                    try {
                        socket = new Socket(address, port);
                        System.out.println("Connection to server established by user: [" + user.getEmail() + "]...");

                        clientOut = new ObjectOutputStream(socket.getOutputStream());
                        clientOut.flush();
                        clientIn = new ObjectInputStream(socket.getInputStream());

                        clientOut.writeObject(user);
                        clientOut.flush();
                        active = true;
                    } catch (IOException e) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e1) {
                            System.out.println("Error in class Server, unable to wake up the thread: " + e1.getMessage());
                        }
                    }
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        System.out.println("Error in class Server, unable to wake up the thread: " + e1.getMessage());
                    }
                }
            }
        });

        clientConnect.setDaemon(true);
        clientConnect.start();
    }

    /**
     * Function used to cut the connection between client and server and close
     * streams and socket. This should be the last function called before the end of
     * the application
     */
    public void finish() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (clientIn != null) {
                clientIn.close();
            }
            if (clientOut != null) {
                clientOut.close();
            }
            this.active = false;
        }catch (IOException e){
            System.out.println("errore:" +e.getMessage());
        }
    }


    public void sendEmail(Email email) throws Exception {
        try {
            System.out.println("trying to send email to: " + email.getReceivers());
            clientOut.writeObject(email);
            clientOut.flush();
        } catch (Exception e) {
            throw new Exception("Can not send the email, check your internet connection");
        }
    }

    /**
     * Function used to read an email coming from the server
     *
     * @return the email received. Return value can be null
     */
    public Email receiveEmail() {
        Email email = null;

        try {
            email = (Email) clientIn.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Error in class Server, unable to read email from stream: " + e.getMessage());
        } catch (IOException e) {
            this.finish();
        }

        return email;
    }


    /**
     * It checks if the server is connected or not
     *
     * @return true iff the server is connected, false otherwise
     */
    public boolean isConnected() {
        return this.active;
    }

    public void deleteEmail(Email e) throws IOException {
        System.out.println("delete_email_client_controller");
    clientOut.writeObject("delete");
    clientOut.writeObject(e);
    clientOut.flush();
    }
}
