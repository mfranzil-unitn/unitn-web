/*
 * AA 2018-2019
 * Introdution to Web Programming
 * Lab 02 - Exercise 04
 * UniTN - DISI
 */
package it.unitn.disi.wp.lab02.exercise04;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Processes http get requests, serving requested files.
 * <p>
 * This class handles the requests now does more than just echoing. It parses
 * the requests and serves the files in the local system.
 * </p>
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 1.0.0 2019-02-25
 */
public class TinyHttpdConnection extends Thread {

    private final Socket SOCKET;

    public TinyHttpdConnection(Socket socket) {
        this.SOCKET = socket;
        setPriority(NORM_PRIORITY - 1);

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
            OutputStream out = SOCKET.getOutputStream();

            /*
                Read the first line of the request, it should be:
                GET /path/to/file
             */
            String req = i.readLine();

            // Print the request for debugging purpose
            System.out.println("Request: " + req);

            // Separate request in tokens
            StringTokenizer st = new StringTokenizer(req);

            // Expect the GET verb and at least a name or a path separator
            if ((st.countTokens() >= 2) && st.nextToken().equals("GET")) {
                // Extract the path file, turning it into a local relative path
                if ((req = st.nextToken()).startsWith("/")) {
                    req = req.substring(1);
                }

                // If it is a directory, by default we get the index.html
                if (req.endsWith("/") || req.isEmpty()) {
                    req = req + "index.html";
                }

                try {
                    FileInputStream fis = new FileInputStream(req);
                    byte[] data = new byte[fis.available()];
                    fis.read(data);

                    // Send OK 200 using HTTP 1.1 protocol
                    out.write("HTTP/1.1 200 OK\r\n".getBytes());

                    // Send Content-Type and charset headers
                    out.write("Content-Type: text/html; charset=utf-8\r\n".getBytes());

                    // Send the end of the headers
                    out.write("\r\n".getBytes());

                    // Send requested file content
                    out.write(data);
                } catch (FileNotFoundException ex) {
                    // File not found
                    out.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                    out.write("\r\n".getBytes());
                    new PrintStream(out).println("404 Not Found");
                }
            } else {
                /*
                    Bad request. This tiny http daemon only accepts
                    GET /path/to/file requests
                 */
                out.write("HTTP/1.1 400 Bad Request\r\n".getBytes());
                out.write("\r\n".getBytes());

                new PrintStream(out).println("400 Bad Request");
            }
        } catch (IOException ex) {
            // Print the error message to the system error stream
            System.err.println("Error while read incoming requests: " + ex.getMessage());
            // print the exception stack trace to the system error stream
            ex.printStackTrace(System.err);
        } finally {
            try {
                // Let's make sure we close the socket
                SOCKET.close();
            } catch (IOException ex) {
                // Print the error message to the system error stream
                System.err.println("Error while closing the socket: " + ex.getMessage());
                // Print the exception stack trace to the system error stream
                ex.printStackTrace(System.err);
            }
        }
    }
}
