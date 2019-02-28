/*
 * AA 2018-2019
 * Introdution to Web Programming
 * Lab 02 - Exercise 02
 * UniTN - DISI
 */
package it.unitn.disi.wp.lab02.exercise02;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Simple NSLookup application
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * since 1.0.0 2019-02-24
 */
public class NSLookup {
    /**
     * The entry point of the application.
     * @param args <b>mandatory!</b> the host name to resolve
     * 
     * @since 1.0.0
     * @see InetAddress
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            //The application must have an argument: the host name to resolve
            System.out.println("Usage: NSLookup <hostname>");
            System.exit(1);
        }
        
        try {
            // Factory method that returns the IP address of the given host name
            InetAddress address = InetAddress.getByName(args[0]);
            
            // Print the host name to the system output stream
            System.out.println(String.format("Host name: %s", address.getHostName()));
            
            // Print the host address to the system output stream
            System.out.println(String.format("Host address: %s", address.getHostAddress()));
        } catch (UnknownHostException ex) {
            // Print the exception stack trace to the system error stream
            System.err.println(String.format("Error while resolving hostname: %s", ex.getMessage()));
            ex.printStackTrace(System.err);
        }
    }
}
