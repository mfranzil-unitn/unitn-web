/*
 * AA 2018-2019
 * Introdution to Web Programming
 * Lab 02 - Exercise 04
 * UniTN - DISI
 */
package it.unitn.disi.wp.lab02.homework;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * Simple HTTP WEB server (HTTP daemon).
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 1.0.0 2019-02-25
 */
public class TinyHttpd {

    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            /* 
                Open the socket on the current machine and associated to the input
                port.
             */
            ss = new ServerSocket(80);
        } catch (IOException ex) {
            /* print the error message to the system error stream */
            System.err.println("Error creating the server socket: " + ex.getMessage());
            /* print the exception stack trace to the system error stream */
            ex.printStackTrace(System.err);

            /* we exit the application with an error code */
            System.exit(1);
        } catch (NumberFormatException ex) {
            /* print the error message to the system error stream */
            System.err.println("Parameter is not a valid port number: " + ex.getMessage());
            /* print the exception stack trace to the system error stream */
            ex.printStackTrace(System.err);
            System.out.println("Usage: HttpdGet <port>");

            /* we exit the application with no errors */
            System.exit(0);
        }

        try {
            System.out.println(InetAddress.getLocalHost());
            System.out.println(ss.getLocalPort());

            /* We inform the client how to test the http get inspector */
            System.out.println("Request a file request to: " + InetAddress.getLocalHost() + ":" + ss.getLocalPort());

            /* Here we listen indefinetely to the incoming requests */
            while (true) {
                new TinyHttpdConnection(ss.accept());
            }
        } catch (UnknownHostException ex) {
            /* print the error message to the system error stream */
            System.err.println("Error while resolving hostname: " + ex.getMessage());
            /* print the exception stack trace to the system error stream */
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            System.err.println("Error in accepting requests: " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }
}
