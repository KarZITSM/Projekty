package Mechanics;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The Mechanics.Server class handles communication between clients
 */
class Server {
    /** List which contains all connected clients */
    static ArrayList<ClientHandler> clients;
    /** Max number of players */
    private static int numOfPlayers = 1000;
    /** Indicator telling whether the game started */
    private static volatile boolean start = false;

    /** Indicator telling whether the game ended */
    private static volatile boolean end = false;

    /** Synchronized queue containing data which will be sent to client */
    static ConcurrentLinkedQueue<String> data = new ConcurrentLinkedQueue<>();
    /** Mechanics.Client who made move in game*/
    static ClientHandler sender;

    public static void main(String[] args)
    {
        AllSender send = new AllSender();
        new Thread(send).start();

        clients = new ArrayList<>();
        ServerSocket server = null;

        try {
            server = new ServerSocket(12345);
            server.setReuseAddress(true);

            while (true) {
                Socket client = server.accept();

                System.out.println("New client connected: "
                        + client.getInetAddress()
                        .getHostAddress());

                ClientHandler clientSock = new ClientHandler(client);
                clients.add(clientSock);
                System.out.println(clients);
                new Thread(clientSock).start();

                // DELETING NOT CONNECTED CLIENTS
                clients.removeIf(clt -> clt.clientSocket.isClosed());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ClientHandler class
    /**
     * Class responsible for handling client in multithreading.
     */
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        public PrintWriter out = null;
        public BufferedReader in = null;
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run()
        {
            try {
                // get the output stream of client
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                // get the input stream of client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                if(clients.size() == 1) {
                    out.println(0);
                    numOfPlayers = Integer.parseInt(in.readLine());
                    System.out.println(numOfPlayers + String.valueOf(clients.size()));
                } else {
                    out.println((clients.size()-1)+String.valueOf(numOfPlayers));
                    in.readLine();
                    if(clients.size()!=numOfPlayers) {
                        data.add(String.valueOf(clients.size() - 1));
                        sender = this;
                    }
                }

                while(!start){Thread.onSpinWait();}
                out.println("START");

                while(true){
                String temp = in.readLine();
                while(temp == null) temp = in.readLine();
                data.add(temp);
                sender = this;
                Thread.onSpinWait();}
            }
            catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    try {
                        in.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Class that handles operations on global variables
     */
    private static class AllSender implements Runnable {
        @Override
        public void run() {
            while(true) {
                if(data.peek()!=null) {
                    System.out.println("DATA: " + data.peek());
                    System.out.println(clients);
                }

                if (!data.isEmpty()) {
                    for (ClientHandler clt : clients) {
                        if(clt != sender) {
                            clt.out.println(data.peek());
                        }
                    }
                    System.out.println("SENT DATA: " + data.peek());
                    data.poll();
                    sender = null;
                }

                if(clients.size() == numOfPlayers && !start)
                    start = true;
                if(clients.size() == 0 || (start && clients.size() < numOfPlayers))
                    start = false;

                Thread.onSpinWait();
            }
        }
    }
}