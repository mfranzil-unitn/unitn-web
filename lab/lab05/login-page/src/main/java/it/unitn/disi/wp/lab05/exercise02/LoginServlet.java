/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 05 - Homeworks
 * UniTN
 */
package it.unitn.disi.wp.lab05.exercise02;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Example of how to use Servlet to handle login
 *
 * @author Stefano Chirico
 * @version 1.1.0
 * @since 1.0.0 2019.03.23
 */
public class LoginServlet extends HttpServlet {

    private final HashMap<String, String> authenticatedUsers;

    public LoginServlet() {
        super();
        authenticatedUsers = new HashMap<>();
    }

    private boolean authenticate(String username, String password) {
        return ((username != null) && (password != null));
    }

    private String retrieveUsername(String jSessionId) {
        return authenticatedUsers.get(jSessionId);
    }

    /**
     * If a cookie is present, then shows data about the authenticated user. If
     * a cookie is not present, then shows the login form.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     *
     * @since 1.0.0 build 2019-03-23
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jSessionId = null;

        Cookie[] cookies = request.getCookies();
        if ((cookies != null) && (cookies.length > 0)) {
            // I have at least 2 cookies. I check is there are authentication
            // cookies (username and password)
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "jsessionid":
                        jSessionId = cookie.getValue();
                        break;
                }
            }
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(
                "<html>\n"
                + " <head>\n"
                + "     <title>Login DEMO</title>\n"
                + "     <meta charset=\"UTF-8\">"
                + "     <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n"
                + "     <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n"
                + " </head>\n"
                + " <body>\n"
        );

        String username = retrieveUsername(jSessionId);
        if (username != null) {
            //The system stores an authenticated user
            out.println(
                    "       <table class=\"table table-bordered table-hover table-striped\">\n"
                    + "         <caption>User authenticated infos</caption>\n"
                    + "         <thead class=\"thead-dark\">\n"
                    + "             <tr>\n"
                    + "                 <th scope=\"col\">Username</th>\n"
                    + "             </tr>\n"
                    + "         </thead>\n"
                    + "         <tbody>\n"
                    + "             <tr>\n"
                    + "                 <td>" + username + "</td>\n"
                    + "             </tr>\n"
                    + "         </tbody>\n"
                    + "     </table>\n"
                    + "     <form id=\"logoutForm\" action=\"logout.html\" method=\"POST\">\n"
                    + "         <button class=\"btn btn-primary\" type=\"submit\">Logout</button>\n"
                    + "     </form>\n"
            );
        } else {
            out.println(
                    "       <div id=\"login\" class=\"card\">\n"
                    + "      <div class=\"card-header\">\n"
                    + "             Login"
                    + "         </div>\n"
                    + "        <div class=\"card-body\">\n"
                    + "             <form id=\"loginForm\" action=\"index.html\" method=\"POST\">\n"
                    + "                 <div class=\"form-group\">\n"
                    + "                     <label for=\"username\">Username</label>\n"
                    + "                     <input id=\"username\" class=\"form-control\" name=\"username\" type=\"text\" required><br>\n"
                    + "                 </div>\n"
                    + "                 <div class=\"form-group\">\n"
                    + "                     <label for=\"password\">Password</label>\n"
                    + "                     <input id=\"password\" class=\"form-control\" name=\"password\" type=\"password\" required><br>\n"
                    + "                 </div>\n"
                    + "                 <button class=\"btn btn-primary\" type=\"submit\">Login</button>\n"
                    + "             </form>\n"
                    + "         </div>\n"
                    + "     </div>\n"
            );
        }

        out.println(
                "       <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n"
                + "     <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n"
                + "   </body>\n"
                + "</html>\n"
        );
    }

    /**
     * Retrieve the post parameters (username and password) and authenticate the
     * user.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     *
     * @since 1.0.0 build 2019-03-23
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (authenticate(username, password)) {
            String jSessionId = Long.toHexString(Double.doubleToLongBits(Math.random()));

            authenticatedUsers.put(jSessionId, username);
            Cookie cookie = new Cookie("jsessionid", jSessionId);
            cookie.setMaxAge(60);
            response.addCookie(cookie);

        }
        response.sendRedirect("index.html");
    }
}
