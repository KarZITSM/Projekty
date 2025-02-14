package Mechanics;

import java.io.*;
import java.net.*;

/**
 * The Mechanics.Client class handles the connection to the server
 */
class Client {
    /** Address IP of server */
    private final String address;
    /** Mechanics.Server port */
    private final int port;
    /** Instance of the class to handle output stream */
    private PrintWriter out;
    /** Instance of the class to handle input stream */
    private BufferedReader in;
    /** Instance of the class to store socket information */
    private Socket socket = null;

    /**
     * Constructor
     */
    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * Method responsible for starting connection to the server
     */
    public void start() throws IOException {
        socket = new Socket(address, port);
        // writing to server
        out = new PrintWriter(socket.getOutputStream(), true);
        // reading from server
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Method responsible for sending data
     * @param mess is a data which we want to send
     */
    public void sendData(String mess) {
        out.println(mess);
    }

    /**
     * Method responsible for receiving data
     * @return received data
     */
    public String receiveData() throws IOException {
        String temp = in.readLine();
        while(temp == null) temp = in.readLine();
        return temp;
    }

    /**
     * Method responsible for ending connection with server
     */
    public void end() throws IOException {
        if(socket != null)
            socket.close();
    }
    // driver code
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 12345);
        client.start();
        System.out.println("Mechanics.Server:: " + client.receiveData());
        System.out.println("Mechanics.Server:: " + client.receiveData());
        client.sendData("test");
        client.end();
    }
}