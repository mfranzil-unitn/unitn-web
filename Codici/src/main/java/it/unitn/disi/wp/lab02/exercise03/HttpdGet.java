/*
 * AA 2018-2019
 * Introdution to Web Programming
 * Lab 02 - Exercise 03
 * UniTN - DISI
 */
package it.unitn.disi.wp.lab02.exercise03;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * Simple HTTP GET echo.
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 1.0.0 2019-02-24
 */
public class HttpdGet {

    public static void main(String[] args) {
        if (args.length != 1) {
            // The application must have an argument: the port to bound
            System.out.println("Usage: HttpdGet <port>");
            return;
        }

        ServerSocket ss = null;
        try {
            /* 
                Open the server socket to the local machine and the port passedas
                argument
             */
            ss = new ServerSocket(Integer.parseInt(args[0]));
        } catch (IOException ex) {
            // Print the error message to the system error stream
            System.err.println(String.format("Error creating the server socket: %s", ex.getMessage()));
            // Print the exception stack trace to the system error stream
            ex.printStackTrace(System.err);

            // Exit the application with an error code
            System.exit(1);
        } catch (NumberFormatException ex) {
            // Print the error message to the system error stream
            System.err.println(String.format("Parameter is not a valid port number: %s", ex.getMessage()));
            // print the exception stack trace to the system error stream
            ex.printStackTrace(System.err);
            System.out.println("Usage: HttpdGet <port>");

            // Exit the application with no errors
            System.exit(0);
        }

        try {
            System.out.println(InetAddress.getLocalHost());
            System.out.println(ss.getLocalPort());

            // Inform the client how to test the http get inspector
            System.out.println(String.format("Perform an HTTP GET request to: %s: %d", InetAddress.getLocalHost(), ss.getLocalPort()));

            // Listen indefinetely to the incoming requests
            while (true) {
                new HttpdConnection(ss.accept());
            }
        } catch (UnknownHostException ex) {
            // Print the error message to the system error stream
            System.err.println(String.format("Error while resolving hostname: %s", ex.getMessage()));
            // print the exception stack trace to the system error stream
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            // Print the error message to the system error stream
            System.err.println("Error in accepting requests: " + ex.getMessage());
            // print the exception stack trace to the system error stream
            ex.printStackTrace(System.err);
        }
    }
}
