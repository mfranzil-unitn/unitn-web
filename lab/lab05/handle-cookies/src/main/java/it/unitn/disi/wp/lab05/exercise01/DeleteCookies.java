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
 * Example of how to use Servlet to remove cookies
 *
 * @author Stefano Chirico
 * @version 1.0.0
 * @since 1.0.0 2019.03.21
 */
public class DeleteCookies extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        StringBuilder html = new StringBuilder();
        html.append("<html>\n");
        html.append("   <head>\n");
        html.append("       <title>Deleted cookies</title>\n");
        html.append("       <meta charset=\"UTF-8\">\n");
        html.append("       <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n");
        html.append("       <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n");
        html.append("   </head>\n");
        html.append("   <body>\n");
        if ((cookies != null) && (cookies.length > 0)) {
            html.append("       <table class=\"table table-bordered table-hover table-striped\">\n");
            html.append("           <caption>Deleted cookies</caption>\n");
            html.append("           <thead class=\"thead-dark\">\n");
            html.append("              <tr>\n");
            html.append("                   <th scope=\"col\">Nome</th>\n");
            html.append("                   <th scope=\"col\">Valore</th>\n");
            html.append("               </tr>\n");
            html.append("           </thead>\n");
            html.append("           <tbody>\n");
            for (Cookie cookie : cookies) {
                html.append("               <tr>\n");
                html.append("                   <th scope=\"row\">").append(cookie.getName()).append("</th>\n");
                html.append("                   <td>").append(cookie.getValue()).append("</td>\n");
                html.append("               </tr>\n");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            html.append("           </tbody>\n");
            html.append("       </table>\n");
        } else {
            html.append("       <h2>No Cookies found</h2>\n");
        }
        html.append("       <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n");
        html.append("       <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n");
        html.append("   </body>\n");
        html.append("</html>\n");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(html);
    }
}
