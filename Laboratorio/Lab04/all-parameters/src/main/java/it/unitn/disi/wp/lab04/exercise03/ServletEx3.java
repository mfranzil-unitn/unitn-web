/**
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 04 - Exercise 03
 */
package it.unitn.disi.wp.lab04.exercise03;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Example of a Servlet that handle POST request containing form parameters
 *
 * @author Stefano Chirico
 * @version 1.0.0
 * @since 1.0.0 2019.03.09
 */
public class ServletEx3 extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @author Stefano Chirico
     * @since 1.0.0 2019.03.09
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlets and forms: Exercise 3</title>");
            out.println("</head>");
            out.println("<body>");

            Enumeration<String> paramNames = request.getParameterNames();

            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();

                out.println("<br><br>");
                out.println("<b>" + paramName + ":</b>");
                out.println("<br><br>");

                String[] paramValues = request.getParameterValues(paramName);

                if (paramValues.length == 1) {
                    String paramValue = paramValues[0];
                    if (paramValue.isEmpty()) {
                        out.println("No value");
                    } else {
                        out.println(paramValue);
                    }
                } else {
                    for (String paramValue : paramValues) {
                        out.println(paramValue);
                        out.println("<br>");
                    }
                }
            }
            out.println("</body>");
            out.println("</html>");
        }
    }
}
