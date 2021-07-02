/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 07 - Example 01
 * UniTN
 */
package it.unitn.disi.wp.lab07.example01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Example of how to communicate with database using JDBC
 *
 * @author Stefano Chirico
 * @version 1.0.0
 * @since 1.0.0 2019.04.01
 */
public class SQLExample {
    public static void main(String[] args) {

        
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Impossible to find the JavaDB Driver: " + ex.getMessage());
            System.exit(1);
        }

        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:derby:C:\\Users\\matte\\OneDrive\\Università\\Didattica\\Introduzione alla programmazione per il web\\Laboratorio\\Lab07\\example.db");
        } catch (SQLException ex) {
            System.err.println("Impossible to connect to database: " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(1);
            return;
        }

        Statement stmt;
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            System.err.println("Impossible to create the connection statement: " + ex.getMessage());
            System.exit(1);
            return;
        }
        
        try {
            ResultSet results = stmt.executeQuery("SELECT * FROM users");
            while (results.next()) {
                int id = results.getInt(1);
                String username = results.getString(2);
                String password = results.getString(3);
                String firstName = results.getString(4);
                String lastName = results.getString(5);
                
                System.out.println(username);
                System.out.println(String.format("\t%s %s: %s (%d)", firstName, lastName, password, id));
                System.out.println("");
            }
        } catch (SQLException ex) {
            System.err.println("Impossible to get the users: " + ex.getMessage());
        }

        try {
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.err.println("Impossible to release the resources: " + ex.getMessage());
        }
    }
}
