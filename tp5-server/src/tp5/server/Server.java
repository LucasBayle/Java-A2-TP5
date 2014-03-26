/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp5.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private ServerSocket serverSocket;

    public Server() throws IOException, SQLException {
        // se co avec la base de données
        Bdd bdd = new Bdd();
        // se connecter avec le client
        connectoclient();
        Bdd.conn.close();
    }

    /*
     * pour se connecter au client
     */
    private void connectoclient() throws IOException {
        this.begin(6666);
    }

    public void begin(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            System.out.println("Waiting for clients to connect on port " + port + "...");
            new ProtocolThread(serverSocket.accept()).start();
            //Thread.start() calls Thread.run()
        }
    }

    class ProtocolThread extends Thread {

        Socket socket;
        ObjectOutputStream out_socket;
        BufferedReader in_socket;

        public ProtocolThread(Socket socket) {
            System.out.println("Accepting connection from " + socket.getInetAddress() + "...");
            this.socket = socket;
            try {
                out_socket = new ObjectOutputStream(socket.getOutputStream());
                in_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                waitingClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void waitingClient() {
            String userPlaceChoice;
            String userNumberStr;
            int userNumber = 0;
            String userName;

            try {
                String message;
                while (true) { // on fait tourner le serveur en boucle, jusqu'a ce que le client rentre quelque chose
                    message = in_socket.readLine();
                    if ("getList".equals(message)) { // on envois la liste des films côté client
                        out_socket.writeObject(Bdd.getList());
                    } else if ("reserver".equals(message)) {
                        userName = in_socket.readLine();
                        userPlaceChoice = in_socket.readLine();
                        userNumberStr = in_socket.readLine();
                        userNumber = Integer.parseInt(userNumberStr);

                        // on vérifie qu'il y a suffisement de place par rapport à la demande du client
                        int diff = Bdd.countPlaces(userPlaceChoice) - userNumber;
                        if (diff >= 0) {
                            Bdd.doUpdate(userNumber, userPlaceChoice); // mettre à jour le nombre de place restante
                            Bdd.doInsert(userName, userNumber, userPlaceChoice); // on met les infos dans la base
                        }
                        out_socket.writeObject(diff);
                    }
                }

            } catch (IOException ex) {
                System.out.println("Closing connection.");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}