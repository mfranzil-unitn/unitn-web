/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 06 - Exercise 01
 * UniTN
 */
package it.unitn.disi.wp.lab06.exercise01;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Example of how to use Servlet to show session values
 *
 * @author Stefano Chirico
 * @version 1.0.0
 * @since 1.0.0 2019.03.24
 */
public class ShowSession extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.LONG);

        String heading;

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();

        Integer accessCount = (Integer) session.getAttribute("accessCount");
        if (accessCount == null) {
            accessCount = 0;
            heading = "Welcome, newcomer";
        } else {
            heading = "Welcome back";
            accessCount++;
        }

        session.setAttribute("accessCount", accessCount);

        response.getWriter().println(
                "<!DOCTYPE html>\n"
                + "<html>\n"
                + " <head>\n"
                + "     <title>Access Counter Example</title>\n"
                + "     <meta charset=\"UTF-8\">\n"
                + "     <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n"
                + "     <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n"
                + " </head>\n"
                + " <body>\n"
                + "     <h1 align=\"center\">" + heading + "</h1>\n"
                + "     <h2>Information on your session:</h2>\n"
                + "     <table class=\"table\">\n"
                + "         <thead><tr><th>Info type</th><th>Value</th></tr></thead>\n"
                + "         <tbody>\n"
                + "             <tr><td>ID</td><td>" + session.getId() + "</td></tr>\n"
                + "             <tr><td>Creation Time</td><td>" + dateFormat.format(new Date(session.getCreationTime())) + "</td></tr>\n"
                + "             <tr><td>Last Access Time</td><td>" + dateFormat.format(new Date(session.getLastAccessedTime())) + "</td></tr>\n"
                + "             <tr><td>Nr. of previous accesses</td><td>" + accessCount + "</td></tr>\n"
                + "         </tbody>\n"
                + "     </table>\n"
                + "     <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n"
                + "     <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n"
                + " </body>\n"
                + "</html>\n"
        );
    }
}
