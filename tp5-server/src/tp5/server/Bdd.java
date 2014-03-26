/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp5.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Lucas
 */
public class Bdd {
    
        static Connection conn;

    public Bdd() throws IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost/theatre";
            conn = DriverManager.getConnection(url, "root", ""); // connexion à la base de donnée
            
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IllegalAccessException ex) {
            System.err.println(ex.getMessage());
        } catch (InstantiationException ex) {
            System.err.println(ex.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    /*
 * ce qui est lié à la base de données
 */
        public static void doSelect() { // list of all pieces and number of seats available
            System.out.println("ce qu'il y a dans la table\n");
            String query = "SELECT name, place FROM piece";
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    String s = rs.getString("name");
                    int n = rs.getInt("place");
                    System.out.println(s + " à " + n + " places restante");
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }

        public static void doInsert(String name, int placeNumber, String pieceName) { //insert the name and number of seats booked ...
            System.out.println("\n[Performing INSERT] ... \n");
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO reservation (name, placeNumber, pieceName) VALUES ('" + name + "', " + placeNumber + ", '" + pieceName + "')");
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }

        public static void doUpdate(int userNumber, String userPlaceChoice) { //update the number of seats still available
            // rajouter la piece qu'il faut update ( string pieceName)
            System.out.print("\n[Performing UPDATE] ... ");
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("UPDATE piece SET place = place - " + userNumber + " WHERE name = '" + userPlaceChoice + "'");
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }

        // pas encore utilisé
        public static int countPlaces(String userPlaceChoice) { //compter le nombre de siège dispo
            int seatsAv = 0;
            String query = "SELECT place FROM piece where name = '" + userPlaceChoice + "'"; 
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    int place = rs.getInt("place");
                    seatsAv = place;
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            return seatsAv;
        }
        // fonction pour mettre dans un tableau, les film associé avec le nombre de place restante
        public static ArrayList<String> getList() { // list of all pieces and number of seats available
            ArrayList<String> list = new ArrayList<String>();
            String query = "SELECT name, place FROM piece";
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    String s = rs.getString("name");
                    list.add(s);
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            return list;
        }
}
