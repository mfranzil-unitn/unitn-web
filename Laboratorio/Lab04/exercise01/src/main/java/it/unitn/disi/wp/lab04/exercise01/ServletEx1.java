/**
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 04 - Exercise 01
 */
package it.unitn.disi.wp.lab04.exercise01;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Example of a Servlet that handle GET request containing form parameters
 * parameters
 *
 * @author Stefano Chirico
 * @version 1.0.0
 * @since 1.0.0 2019.03.09
 */
public class ServletEx1 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlets and forms: Exercise 01</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("First name " + request.getParameter("first_name"));
            out.println("<br>");
            out.println("Last name " + request.getParameter("last_name"));
            out.println("</body>");
            out.println("</html>");
        }
    }
}