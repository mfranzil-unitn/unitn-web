/**
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 04 - Exercise 02
 */
package it.unitn.disi.wp.lab04.exercise02;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Example of a Servlet that handle POST request containing check-boxes
 * parameters
 *
 * @author Stefano Chirico
 * @version 1.0.0
 * @since 1.0.0 2019.03.08
 */
@WebServlet(name = "ServletEx2", urlPatterns = {"/ServletEx2"})
public class ServletEx2 extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @author Stefano Chirico
     * @since 1.0.0 2019.03.08
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlets and forms: Exercise 2</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<b>Picked subjects:</b>");
            out.println("<br>");
            out.println("<br>");
            out.println("<b>Maths flag:</b>" + request.getParameter("maths"));
            out.println("<br>");
            out.println("<b>Physics flag:</b>" + request.getParameter("physics"));
            out.println("<br>");
            out.println("<b>Chemistry flag:</b>" + request.getParameter("chemistry"));
            out.println("<br>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
