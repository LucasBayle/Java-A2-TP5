/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp5.theatre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucas
 */
public class Client {

    private ObjectInputStream inSocket;
    private PrintWriter outSocket;
    private Socket socket;

    // se connecter au serveur
    public Client() {
        try {
            socket = new Socket("localhost", 6666);
            //to get the ip address
            System.out.println((java.net.InetAddress.getLocalHost()).toString());
            //true: it will flush the output buffer
            outSocket = new PrintWriter(socket.getOutputStream(), true);
            inSocket = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getList() {
        outSocket.println("getList");
        ArrayList<String> list = null;
        try {
            // On met dans "list" ce que le serveur à envoyé (liste des films)
            list = (ArrayList<String>) inSocket.readObject();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // une fois que le client a remplis l'interface graphique, il va pouvoir faire sa reservation
    public int reserver(String userName, String pieceName, int numberPlace) {
        outSocket.println("reserver");
        outSocket.println(userName);
        outSocket.println(pieceName);
        outSocket.println(numberPlace);
        int diff = Integer.MIN_VALUE; 
        try {
            // on reccupere le diff du serveur (positif, negatif) et on envois à l'interface graphique
            diff = (int) inSocket.readObject(); 
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return diff;
    }
}
