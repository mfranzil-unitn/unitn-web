/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 05 - Exercise 01
 * UniTN
 */
package it.unitn.disi.wp.lab05.exercise01;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Example of how to use Servlet to show cookies
 *
 * @author Stefano Chirico
 * @version 1.0.0
 * @since 1.0.0 2019.03.21
 */
public class ShowCookies extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String title = "Active Cookies";
        out.println(
                "<!DOCTYPE html>\n"
                + "<html>\n"
                + " <head>\n"
                + "     <title>" + title + "</title>\n"
                + "     <meta charset=\"UTF-8\">\n"
                + "     <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n"
                + "     <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n"
                + " </head>\n"
                + " <body>\n"
                + "     <table class=\"table table-bordered table-hover table-striped\">\n"
                + "         <caption>" + title + "</caption>\n"
                + "         <thead class=\"thead-dark\">\n"
                + "             <tr>\n"
                + "                 <th scope=\"col\">Cookie Name</th>\n"
                + "                 <th scope=\"col\">Cookie Value</th>\n"
                + "             </tr>\n"
                + "         </thead>\n"
                + "         <tbody>\n"
        );
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            out.println(
                    "         <tr>\n"
                    + "             <th scope=\"row\">" + cookie.getName() + "</th>\n"
                    + "             <td>" + cookie.getValue() + "</td>\n"
                    + "         </tr>\n"
            );
        }
        out.println(
                "         </tbody>\n"
                + "     </table>\n"
                + "   <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n"
                + "   <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n"
                + " </body>\n"
                + "</html>\n"
        );
    }
}
