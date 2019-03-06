/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 02 - Exercise 01
 * UniTN - DISI
 */
package it.unitn.disi.wp.lab02.exercise01;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Example on using InetAddress class
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn it&gt;
 * @since 1.0.0 2019-02-24
 */
public class InetAddressExample {

    /**
     * The entry point of the application.
     *
     * @param args not used. The application doesn't accept any command-line
     * parameter
     *
     * @since 1.0.0
     * @see InetAddress
     */
    public static void main(String[] args) {

        try {
            // Factory method that returns the address of the local host.
            InetAddress address = InetAddress.getLocalHost();

            // Print the address object to the system output stream using its toString() method
            System.out.println(String.format("toString(): %s", address));

            // Print the host name of the local host to the system output stream
            System.out.println(String.format("Host name: %s", address.getHostName()));

            // Print the local host IP to the system output stream
            System.out.println(String.format("Host address: %s", address.getHostAddress()));
        } catch (UnknownHostException ex) {
            // Print the exception stack trace to the system error stream
            System.err.println(String.format("Error while acquiring local host IP: %S", ex.getMessage()));
            ex.printStackTrace(System.err);
        }
    }
}