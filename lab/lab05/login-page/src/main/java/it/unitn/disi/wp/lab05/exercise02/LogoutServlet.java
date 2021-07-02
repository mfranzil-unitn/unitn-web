/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 05 - Homeworks
 * UniTN
 */
package it.unitn.disi.wp.lab05.exercise02;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Example of how to use Servlet to handle logout
 *
 * @author Stefano Chirico
 * @version 1.1.0
 * @since 1.0.0 2019.03.23
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if ((cookies != null) && (cookies.length > 0)) {
            for (Cookie cookie: cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        
        response.sendRedirect("index.html");
    }
    
}
