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
 * Example of how to use Servlet to set cookies
 *
 * @author Stefano Chirico
 * @version 1.0.0
 * @since 1.0.0 2019.03.21
 */
public class SetCookies extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        for (int i = 0; i < 3; i++) {
            Cookie cookie = new Cookie("Session-Cookie-" + i, "Cookie-Value-S" + i);
            cookie.setMaxAge(-1);//Valid only for the current browsing session
            response.addCookie(cookie);

            cookie = new Cookie("Persistent-Cookie-" + i, "Cookie-Value-P" + i);
            cookie.setMaxAge(3600);//Expires in one hour.
            response.addCookie(cookie);
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String title = "Setting Cookies";
        out.println(
                "<html>\n"
                + " <head>\n"
                + "     <title>" + title + "</title>\n"
                + "     <meta charset=\"UTF-8\">\n"
                + "     <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n"
                + "     <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n"
                + " </head>\n"
                + " <body>\n"
                + "     <h1>" + title + "</h1>\n"
                + "     There are six cookies associated with this page.\n"
                + "   <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n"
                + "   <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n"
                + " </body>\n"
                + "</html>\n"
        );
    }
}
