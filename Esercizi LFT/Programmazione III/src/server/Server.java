package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bin.Email;
import bin.User;
import javafx.application.Platform;
import server.model.ServerModel;

/**
 * Representation of the Server class, used to manage the connection between
 * client and server
 */
public class Server {
    private final int port;
    private Socket socket;
    private ServerSocket serverSocket;
    private FileWriter txt;
    private File xml;
    private ExecutorService executor;
    private List<InnerServer> innerServerList;
    private boolean run;
    private final ReadWriteLock lock;
    private final Lock writeLock;
    private final Lock readLock;
    private final ServerModel serverModel;
    private final int NUM_EXEC = 3;

    /**
     * Constructor for Server class
     * @param port represents the port where the server is connected
     */
    public Server(int port, ServerModel serverModel) {
        this.innerServerList = new ArrayList<InnerServer>();

        this.serverModel = serverModel;
        this.port = port;

        this.lock = new ReentrantReadWriteLock();  //create a lock for thread control
        this.writeLock = lock.writeLock();
        this.readLock = lock.readLock();
        try {
            this.createTxtLog(); // create a txt file for server log
            this.createXML();
        } catch (IOException e) {
            System.out.println("Exception in class Server, unable to create a Log file: " +e.getMessage());
        }
    }

    /**
     * function startRunning - turns the server up, allowing clients to connect
     */
    public void startRunning() {
        try {
            if (this.serverSocket == null || this.serverSocket.isClosed()) {
                this.serverSocket = new ServerSocket(this.port);    //Server activation, giving a specified port to ServerSocket
                this.serverSocket.setReuseAddress(true);            // enabling SO_REUSEADDR, that allows binding if the connection is in a timeout state
            }
            this.addViewLog("Server attivato con successo.");
            this.updateXML();
            this.updateTxtLog("Server activation completed! Listening on address: " + InetAddress.getLocalHost().getHostAddress()
                    + " on port:" + this.port+" for new connection...");

            this.executor = Executors.newFixedThreadPool(NUM_EXEC); // creating a thread pool
            this.run = true;

            Thread server = new Thread(() -> {
                try {
                    while (run) {
                        goOnline(); // connection to server started
                    }
                } catch (IOException e) {
                    System.out.println("ServerSocket disconnected: " + e.getMessage());
                } finally {
                    try {
                        if (socket != null) {
                            for (InnerServer innerServer : innerServerList) {
                                innerServer.shutdown(); //closing connection between the socket and the current client
                            }
                        }
                        if (executor != null) {
                            executor.shutdownNow(); //closing the thread pool
                        }
                    } catch (IOException e) {
                        System.out.println("Exception in class Server, unable to close the socket: " + e.getMessage());
                    }
                    if (!innerServerList.isEmpty()) {
                        innerServerList.clear();
                    }
                }
            });
            server.setDaemon(true);
            server.start();
        } catch (IOException e) {
            System.out.println("Exception in class Server, unable to create ServerSocket: " + e.getMessage());
        }
    }

    /**
     * Function that connect the server to the network. It allows multiple clients connection
     * @throws IOException if an I/O error occurs when waiting for a connection on ServerSocket
     */
    private void goOnline() throws IOException {
        this.socket = this.serverSocket.accept();           //socket starts to listen for a client connection

        InnerServer inner = new InnerServer(this.socket);   //creating a single thread for the current client
        this.innerServerList.add(inner);                    //adding current thread to the thread list
        this.executor.execute(inner);                       //adding current thread to the thread pool
    }

    /**
     * Function that stops the server and disconnects the connection between client
     * and server
    */
     public void stopRunning() {
        if (run) {
            this.run = false;

            try {
                if (this.serverSocket != null) {
                    this.serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Exception in class Server, unable to close the ServerSocket: " + e.getMessage());
            }

            this.writeLock.lock();  //acquire write lock


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(this.xml);
                doc.getDocumentElement().normalize();

                NodeList serverNodes = doc.getDocumentElement().getElementsByTagName("server");
                if (serverNodes.getLength() > 0) {
                    Element server = (Element) serverNodes.item(0);
                    Element disconnect = doc.createElement("disconnect");
                    disconnect.appendChild(doc.createTextNode(this.currentTime()));
                    server.appendChild(disconnect);

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                    DOMSource domSource = new DOMSource(doc);
                    StreamResult streamResult = new StreamResult(this.xml);
                    transformer.transform(domSource, streamResult);
            }   else{
                    System.out.println("No <server> element found in the XML.");
                }
            }
                catch (Exception e) {
                System.out.println("Exception in class Server, unable to sign the server stop on the xml document: " + e.getMessage());
            }
            finally {
                updateTxtLog("Server disconnected!");
                this.writeLock.unlock();    //write lock ready for a new connection
            }
            this.addViewLog("Server disconnesso con successo!");

        }

    }

    /**
     * Function used to add a string to the log list in server view
     * @param message represents the string to be displayed
     */
    private void addViewLog(String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        serverModel.addLog(message);
                    }
                });
            }
        }).start();
    }
    /**Function used to create a local log.txt file, to keep track of the server action
     * after its activation.
     */
    private void createTxtLog() throws IOException {
        this.txt = new FileWriter("src/server/resources/log.txt",false);
    }


    /**function used to update the log.txt, adding infos like the Server address, port
     * and the time when the log has been updated
     * */

    //FIXME updateLog - aggiornare exception
    private void updateTxtLog(String log){
        this.writeLock.lock();
        try {

        this.txt = new FileWriter("src/server/resources/log.txt",true);
        this.txt.write("| "+this.currentTime()+" | "+"address: "+ InetAddress.getLocalHost().getHostAddress()+" | "+"port: "+ this.port+" | "+log+"\n");
        this.txt.close();
        writeLock.unlock();
        } catch (IOException e){
            System.out.println("Unable to load or update log file: " +e.getMessage());
        }
    }

    /**
     * It creates the xml file used for the server log file used in server view
     * If the xml file already exists, it deletes it and creates a new one
     */
    //FIXME createXML()
    private void createXML() {
        this.xml = new File("src/server/resources/log.xml");

        if (this.xml.exists()) {
            if (!this.xml.delete()) {
                System.out.println("Exception in class Server, unable to delete existing XML file.");
                return;
            }
        }
        try {
            if (!this.xml.exists()) {
                if (!this.xml.createNewFile()) {
                    System.out.println("Exception in class Server, unable to create a new XML file.");
                    return;
                }
            }
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            // root element
            Element root = doc.createElement("Log");
            doc.appendChild(root);
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            Element server = doc.createElement("server");
            Element clients = doc.createElement("clients");
            Element emails = doc.createElement("emails");

            root.appendChild(server);
            root.appendChild(clients);
            root.appendChild(emails);

            removeWhitespaceNodes(doc.getDocumentElement());

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(this.xml);

            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException e) {
            System.out.println("Exception in class Server, unable to create the document builder: " + e.getMessage());
        } catch (TransformerException e) {
            System.out.println("Exception in class Server, unable to copy into the xml file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Exception in class Server, unable to create the consistency file: " + e.getMessage());
        }
    }

    /**
     * Function used to update the XML file, adding server infos e.g. the time when
     * the server open the communication, its ip address and its port.
     */
    //fixme updateXML() - server info
    private void updateXML() {
        this.writeLock.lock();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(this.xml);
            doc.getDocumentElement().normalize();

            NodeList serverNodes = doc.getElementsByTagName("server");
            if (serverNodes.getLength() > 0) {
                Element server = (Element) serverNodes.item(0);
                server.setAttribute("address", InetAddress.getLocalHost().getHostAddress());
                server.setAttribute("port", String.valueOf(this.port));

                Node connect = doc.createElement("connect");
                connect.appendChild(doc.createTextNode(this.currentTime()));
                server.appendChild(connect);

                removeWhitespaceNodes(doc.getDocumentElement());

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource domSource = new DOMSource(doc);
                StreamResult streamResult = new StreamResult(this.xml);
                transformer.transform(domSource, streamResult);
            }else System.out.println("No <server> element found in the XML.");
        } catch (ParserConfigurationException e) {
            System.out.println("Exception in class Server, unable to create the document builder: " + e.getMessage());
        } catch (TransformerException e) {
            System.out.println("Exception in class Server, unable to copy into the xml file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Exception in class Server, unable to create the consistency file: " + e.getMessage());
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } finally {
            this.writeLock.unlock();
        }
    }

    private void removeWhitespaceNodes(Element element) {
        NodeList childNodes = element.getChildNodes();
        for (int i = childNodes.getLength() - 1; i >= 0; i--) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().isEmpty()) {
                element.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeWhitespaceNodes((Element) child);
            }
        }
    }

    /**
     * It creates a string made of each person to whose the email has been sent
     * without the current user
     *
     * @param receivers represents the list of email addresses
     * @return a string made of each person to whose the email has been sent without
     *         the current user
     */
    private synchronized String convertReceivers(List<String> receivers) {
        String rec = "";
        for (String r : receivers) {
            rec += r;
            if (r != receivers.get(receivers.size() - 1)) {
                rec += ", ";
            }
        }

        return rec;
    }

    /**
     * It converts a string of emails into a list of strings
     *
     * @param receivers represents the string of emails
     * @return a list made of the given string splitting it by ","
     *
     */
    private synchronized List<String> convertReceivers(String receivers) {
        String[] rStrings = receivers.split(",");
        List<String> rec = new ArrayList<>();

        for (String string : rStrings) {
            rec.add(string);
        }

        return rec;
    }

    /**
     * Function used to convert the date from a string format into a gregorian
     * calendar
     *
     * @param source represents the string source, which date is in form
     *               "dd/MM/yyyy, HH:mm"
     * @return a gregorian calendar made of the given date
     * @throws ParseException iff an exception happens when parsing the string into
     *                        a date
     */
    private synchronized GregorianCalendar convertDate(String source) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss.SSS"); // 01/04/2022, 17:38:51.579
        Date date = df.parse(source);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        return cal;
    }

    /**
     * It converts the date from GregorianCalendar into String
     *
     * @param calendar is the gregorian calendar used to store the date
     * @return the given date in string form
     */
    private synchronized String convertDate(GregorianCalendar calendar) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss.SSS");
        fmt.setCalendar(calendar);
        String dateFormatted = fmt.format(calendar.getTime());

        return dateFormatted;
    }

    /**
     * Function used to get the current time
     *
     * @return a string representing the current date and time
     */
    private synchronized String currentTime() {
        String time = this.convertDate(new GregorianCalendar());

        return time;
    }

    /**
     * InnerServer class created in order to have a single thread for each client
     * connected
     */
    public class InnerServer implements Runnable {
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private Socket clientSocket;
        private User user;
        private boolean active;

        /**
         * Constructor for InnerServer class, used to get the socket connection between
         * server and client
         *
         * @param clientSocket represents the socket which allow the connection between
         *                     client and server
         */
        public InnerServer(Socket clientSocket) {
            this.clientSocket = clientSocket;
            this.active = true;
        }

        /**
         * Function used to run the current task as a thread, allowing multiple
         * executions
         */
        @Override
        public void run() {
            this.openStreams();                     //opening the communication stream

            while (this.active) {
                if (this.user == null) {
                    this.registerUser();
                    updateTxtLog("New user client connected! ->");
                    addViewLog("Nuovo utente connesso al Server: " + this.getCurrentClientMail());
                    this.updateXML();
                    this.sendPreviousEmails();

                } else {
                    receiveEmail();
                }
            }
            this.closeStreams();
            //this.finalUpdateXML();
            updateTxtLog("User disconnected: " + this.user.getEmail());
        }

        /**
         * Function invoked when the server is waiting for a new email. This function
         * reads the email, stores it in the xml file and finally sends it to the
         * corresponding client/s
         */
        private synchronized void receiveEmail() {
            boolean valid = true;
            try {
                Object emailObj = in.readObject();
                if (emailObj.getClass() == Email.class) {
                    Email email = (Email) emailObj;
                    updateTxtLog("New email from: " + this.getCurrentClientMail() + " to: " + convertReceivers(email.getReceivers()));

                    for (int index = 0; index < email.getReceivers().size(); index++) {
                        String dest = email.getReceivers().get(index);

                        if (!this.checkUserExists(dest)) {
                            valid = false;

                            final List<String> receivers = new ArrayList<>();
                            receivers.add(email.getSender());
                            final String sender = "noreply@server.it";
                            final String object = "EMAIL INESISTENTE";
                            final String text = "L'indirizzo email \"" + dest + "\" è inesistente. Riprova!\n\nEmail:\n"
                                    + email.getText();
                            final GregorianCalendar date = (GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getDefault());

                            this.routeEmail(new Email(sender, receivers, object, text, date));
                        }
                    }

                    if (valid) {
                        System.out.println("New email received!");
                        this.updateEmailXML(email);
                        this.routeEmail(email);
                    }
                }else{
                    deleteEmail();
                }
                } catch(ClassNotFoundException e){
                    System.out.println("Exception in class Server, unable to read from the stream: " + e.getMessage());
                } catch(IOException e){
                    this.active = false;
                System.out.println("Error in class Server, ObjectStream reader error: " +e.getMessage());
                }

        }
        /**
         * Function used to update the XML file, adding an email whenever the server
         * reads one in input from a client
         *
         * @param newEmail represents the email just received
         */
        //FIXME metodo updateXML lista email
        private synchronized void updateEmailXML(Email newEmail) {
            writeLock.lock();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(xml);
                doc.getDocumentElement().normalize();

                NodeList emailNodes = doc.getElementsByTagName("emails");
                if (emailNodes.getLength() == 0) {
                    throw new RuntimeException("No <emails> element found in the document.");
                }
                Element documentElement = (Element) emailNodes.item(0);

                String allReceivers = convertReceivers(newEmail.getReceivers());

                for (String receiver : newEmail.getReceivers()) {
                    Element email = doc.createElement("email");
                    email.setAttribute("id", String.valueOf(newEmail.getId()));
                    documentElement.appendChild(email);

                    Element user = doc.createElement("user");
                    user.setAttribute("userId", String.valueOf(this.user.getID()));
                    email.appendChild(user);

                    Element sender = doc.createElement("mittente");
                    sender.appendChild(doc.createTextNode(newEmail.getSender()));
                    email.appendChild(sender);

                    Element receivers = doc.createElement("destinatari");
                    receivers.appendChild(doc.createTextNode(receiver)); // Set all receivers
                    email.appendChild(receivers);


                    Element object = doc.createElement("argomento");
                    object.appendChild(doc.createTextNode(newEmail.getObject()));
                    email.appendChild(object);

                    Element text = doc.createElement("testo");
                    text.appendChild(doc.createTextNode(newEmail.getText()));
                    email.appendChild(text);

                    Element data = doc.createElement("data");
                    data.appendChild(doc.createTextNode(convertDate(newEmail.getSendDate())));
                    email.appendChild(data);
                }
                removeWhitespaceNodes(doc.getDocumentElement());


                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource domSource = new DOMSource(doc);
                StreamResult streamResult = new StreamResult(xml);

                transformer.transform(domSource, streamResult);
            }  catch (ParserConfigurationException e) {
                System.out.println("Exception in class Server, unable to create the document builder: " + e.getMessage());
            } catch (TransformerException e) {
                System.out.println("Exception in class Server, unable to copy into the xml file: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Exception in class Server, unable to create the consistency file: " + e.getMessage());
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } finally {
                writeLock.unlock();
            }
        }

        /**
         * Function used to update the XML file, adding client infos whenever a new
         * client connection is established
         */
        private synchronized void updateXML() {
            writeLock.lock();

            String email = this.user.getEmail();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(xml);
                doc.getDocumentElement().normalize();

                Element documentElement = (Element) doc.getDocumentElement().getChildNodes().item(1);
                Node client = doc.createElement("client");
                ((Element) client).setAttribute("email", email);
                documentElement.appendChild(client);

                Node connect = doc.createElement("connect");
                connect.appendChild(doc.createTextNode(currentTime()));
                client.appendChild(connect);

                Node disconnect = doc.createElement("disconnect");
                disconnect.appendChild(doc.createTextNode("--- still connected ---"));
                client.appendChild(disconnect);

                removeWhitespaceNodes(doc.getDocumentElement());


                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource domSource = new DOMSource(doc);
                StreamResult streamResult = new StreamResult(xml);

                transformer.transform(domSource, streamResult);
            } catch (ParserConfigurationException e) {
                System.out.println("Exception in class Server, unable to create the document builder: " + e.getMessage());
            } catch (TransformerException e) {
                System.out.println("Exception in class Server, unable to copy into the xml file: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Exception in class Server, unable to create the consistency file: " + e.getMessage());
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } finally {
                writeLock.unlock();
            }
        }

        /**
         * Function used to update the XML file, adding client infos whenever a new
         * client connection is established
         */
        private synchronized void finalUpdateXML() {
            writeLock.lock();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(xml);
                doc.getDocumentElement().normalize();

                Node clients = doc.getDocumentElement().getChildNodes().item(1);
                String userMail = this.user.getEmail();
                for (int index = 0; index < clients.getChildNodes().getLength(); index++) {
                    Node client = clients.getChildNodes().item(index);
                    Element clientElement = (Element) client;
                    String currentMail = clientElement.getAttribute("email");
                    if (currentMail.equals(userMail)) {
                        client.getChildNodes().item(1).getFirstChild().setNodeValue(currentTime());
                    }
                }
                removeWhitespaceNodes(doc.getDocumentElement());

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource domSource = new DOMSource(doc);
                StreamResult streamResult = new StreamResult(xml);

                transformer.transform(domSource, streamResult);
            } catch (ParserConfigurationException e) {
                System.out.println("Exception in class Server, unable to create the document builder: " + e.getMessage());
            } catch (TransformerException e) {
                System.out.println("Exception in class Server, unable to copy into the xml file: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Exception in class Server, unable to create the consistency file: " + e.getMessage());
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } finally {
                writeLock.unlock();
            }
        }

        public synchronized void deleteEmailXML(Email email, User user){
            writeLock.lock();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try{
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(xml);

                NodeList list = doc.getElementsByTagName("email");
                for (int i = 0; i < list.getLength(); i++) {
                    Element emailElement = (Element) list.item(i);
                    int id = Integer.parseInt(emailElement.getAttribute("id"));
                    if (id == email.getId()) {
                        for (String receiver:email.getReceivers()) {
                            if(receiver.equals(user.getEmail())) {
                                System.out.println("user email found: " +receiver);
                                emailElement.getParentNode().removeChild(emailElement);
                                break;
                            }
                        }
                    }
                }

                removeWhitespaceNodes(doc.getDocumentElement());

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "2");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(xml);
                transformer.transform(source, result);

            }  catch (ParserConfigurationException e) {
                System.out.println("Exception in class Server, unable to create the document builder: " + e.getMessage());
            } catch (TransformerException e) {
                System.out.println("Exception in class Server, unable to copy into the xml file: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Exception in class Server, unable to create the consistency file: " + e.getMessage());
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }finally {
                writeLock.unlock();
            }
        }

        //TODO aggiungere metodo eliminaspazi a tutti i metodi xml


        /**
         * Function used to choose the clients which correspond to the given email
         *
         * @param email represents the email to route
         */
        private void routeEmail(Email email) {
            final List<String> receivers = email.getReceivers();

            for (String receiver : receivers) {
                for (InnerServer inner : innerServerList) {
                    if (receiver.equals(inner.getCurrentClientMail())) {
                        inner.sendEmail(email);
                    }
                }
            }
        }

        /**
         * Function used to send the given email to the corresponding client/s
         *
         * @param email represents the email to send
         */
        public void sendEmail(Email email) {
            try {
                this.out.writeObject(email);
                this.out.flush();
                addViewLog("Email inviata da: " + email.getSender() + " per: " + convertReceivers(email.getReceivers()));
            } catch (IOException e) {
                System.out.println("Exception in class Server, unable to write on the stream: " + e.getMessage());
            }
        }

        /**
         * Function to recover the current user's email address
         *
         * @return the current user's email address
         */
        public String getCurrentClientMail() {
            return this.user.getEmail();
        }

        public synchronized void deleteEmail(){
            try {
                    Email eToDel = (Email) in.readObject();
                    System.out.println("Deleting mail: " + eToDel.toString());
                    deleteEmailXML(eToDel, this.user);
                    System.out.println("email deleted");

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


        /**
         * Function used to open the streams, in order to allow the communication
         * between client and server
         */
        private void openStreams() {
            try {
                this.in = new ObjectInputStream(this.clientSocket.getInputStream());
                this.out = new ObjectOutputStream(this.clientSocket.getOutputStream());

                this.out.flush();
            } catch (IOException e) {
                System.out.println("Exception in class Server, unable to open streams: " + e.getMessage());
            }
        }

        /**
         * Function to send all the email received by the current client to its
         * destination
         */
        private void sendPreviousEmails() {
            List<Email> emails = this.getEmailList();

            try {
                if (!emails.isEmpty()) {
                    for (Email email : emails) {
                        this.out.writeObject(email);
                        this.out.flush();
                    }
                    addViewLog("Previously stored emails sent to the user: " + this.user.getEmail());
                }
            } catch (IOException e) {
                System.out.println("Exception in class Server, unable to write on streams: " + e.getMessage());
            }
        }

        /**
         * Function used to connect the current server's task to a specified user
         * account
         */
        private void registerUser() {
            try {
                User currentUser = (User) this.in.readObject();

                if (currentUser == null) {
                    throw new IllegalStateException("Error in receiving user: expected User but received something else!");
                }

                this.user = currentUser;
            } catch (ClassNotFoundException | IOException e) {
                System.out.println("Exception in class Server, unable to accept the current user: " + e.getMessage());
            }
        }

        /**
         * Function used to close the streams, in order to end the communication between
         * client and server
         */
        private void closeStreams() {
            try {
                if (this.in != null) {
                    this.in.close();
                }
                if (this.out != null) {
                    this.out.close();
                }
                if (innerServerList.contains(this)) {
                    innerServerList.remove(this);
                }
            } catch (IOException e) {
                System.out.println("Exception in class Server, unable to close streams: " + e.getMessage());
            }
        }

        /**
         * Function used to shutdown the socket connected to the current user
         *
         * @throws IOException if an I/O error occurs when closing this socket
         */
        private void shutdown() throws IOException {
            this.clientSocket.close();
        }

        /**
         * Function used to check and get each email sent to the current client (if
         * there is any)
         *
         * @return a list of emails if there is any, an empty list otherwise
         */
        private List<Email> getEmailList() {
            readLock.lock();

            List<Email> emails = new ArrayList<Email>();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(xml);

                doc.getDocumentElement().normalize();

                NodeList list = doc.getElementsByTagName("email");

                for (int temp = 0; temp < list.getLength(); temp++) {

                    Node node = list.item(temp);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        Element element = (Element) node;

                        int id = Integer.parseInt(element.getAttribute("id"));
                        String sender = element.getElementsByTagName("mittente").item(0).getTextContent();
                        String receivers = element.getElementsByTagName("destinatari").item(0).getTextContent();

                        if (receivers.contains(this.user.getEmail())) {
                            String argomento = element.getElementsByTagName("argomento").item(0).getTextContent();
                            String text = element.getElementsByTagName("testo").item(0).getTextContent();
                            String data = element.getElementsByTagName("data").item(0).getTextContent();

                            List<String> dest = convertReceivers(receivers);
                            GregorianCalendar date = convertDate(data);

                            Email email = new Email(sender, dest, argomento, text, date);


                            emails.add(email);
                        }
                    }
                }
            } catch (ParserConfigurationException e) {
                System.out.println("Exception in class Server, unable to create document builder: " + e.getMessage());
            } catch (SAXException | IOException e) {
                System.out.println("Exception in class Server, unable to parse the xml file: " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("Exception in class Server, unable to convert the date: " + e.getMessage());
            } finally {
                readLock.unlock();
            }

            return emails;
        }

        /**
         * Function used to check if the given user is registered in the XML file
         * containing every account registered
         *
         * @param userEmail represents the email of the user to search
         * @return true if the user exists, false otherwise
         */
        private boolean checkUserExists(String userEmail) {
            try (BufferedReader reader = new BufferedReader(new FileReader("src/bin/db.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(userEmail)) {
                        return true;
                    }
                }

            } catch (IOException e) {
                System.out.println("Unable to find the user mail or the file: " + e.getMessage());
            }
            return false;
        }
    }

}
