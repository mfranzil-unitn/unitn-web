/*
 * AA 2018-2019
 * Introdution to Web Programming
 * Lab 02 - Exercise 03
 * UniTN - DISI
 */
package it.unitn.disi.wp.lab02.exercise03;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Echoes incoming requests from a socket stream.
 * <p>
 * This class will process the individual requests, displaying in the standard
 * output the details of the HPPT GET requests.
 * </p>
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 1.0.0 2019-02-24
 */
public class HttpdConnection extends Thread {

    private final Socket SOCKET;

    public HttpdConnection(Socket socket) {
        this.SOCKET = socket;

        // Avoid to call overrideable method in public constructors
        initInstance();
    }

    private void initInstance() {
        // Start of the thread
        start();
    }

    @Override
    public void run() {
        try {
            // Create a reader to consume the socket stream
            BufferedReader i = new BufferedReader(new InputStreamReader(SOCKET.getInputStream()));

            // The output stream will allow us to write a response
            DataOutputStream o = new DataOutputStream(SOCKET.getOutputStream());

            // Here essentially we echo what we have read
            while (i.ready()) {
                String s = i.readLine();
                System.out.println(String.format("Received: %s", s));
            }
        } catch (IOException ex) {
            // Print the error message to the system error stream
            System.err.println(String.format("Error while read incoming requests: %s", ex.getMessage()));
            // Print the exception stack trace to the system error stream
            ex.printStackTrace(System.err);
        } finally {
            try {
                // Let's make sure we close the socket
                SOCKET.close();
            } catch (IOException ex) {
                // Print the error message to the system error stream
                System.err.println(String.format("Error while closing the socket: %s", ex.getMessage()));
                // Print the exception stack trace to the system error stream
                ex.printStackTrace(System.err);
            }
        }
    }
}
