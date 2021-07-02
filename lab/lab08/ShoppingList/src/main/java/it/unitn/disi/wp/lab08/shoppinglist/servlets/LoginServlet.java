/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 08 - Shopping List Implementation
 * UniTN
 */
package it.unitn.disi.wp.lab08.shoppinglist.servlets;

import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import it.unitn.disi.wp.lab08.shoppinglist.persistence.dao.UserDAO;
import it.unitn.disi.wp.lab08.shoppinglist.persistence.entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles the login web page.
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2019.04.06
 */
public class LoginServlet extends HttpServlet {

    private UserDAO userDao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     * @author Stefano Chirico
     * @since 1.0.0.190407
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(
                "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Lab 08: Authentication Area</title>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n"
                    + "        <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n"
                    + "        <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.0.8/css/all.css\" crossorigin=\"anonymous\">\n"
                + "        <!-- Custom styles for this template -->\n"
                //+ "        <link href=\"css/signin.css\" rel=\"stylesheet\">\n"
                + "        <link href=\"css/floating-labels.css\" rel=\"stylesheet\">\n"
                + "    </head>\n"
                + "    <body>\n"
                //                + "    <div class=\"container\">\n"
                //                + "        <div class=\"jumbotron\">\n"
                + "        <form class=\"form-signin\" action=\"" + contextPath + "login.handler\" method=\"POST\">\n"
                + "            <div class=\"text-center mb-4\">\n"
                + "                <img class=\"mb-4\" src=\"images/unitn_logo_1024.png\"  width=\"128\" height=\"128\">\n"
                + "                <h3 class=\"h3 mb-3 font-weight-normal\">Authentication Area</h3>\n"
                + "                <p>You must authenticate to access, view, modify and share your Shopping Lists</p>\n"
                + "            </div>\n"
                + "            <div class=\"form-label-group\">\n"
                + "                <input type=\"email\" id=\"username\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required autofocus>\n"
                + "                <label for=\"username\">Username</label>\n"
                + "            </div>\n"
                + "            <div class=\"form-label-group\">\n"
                + "                <input type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required>\n"
                + "                <label for=\"password\">Password</label>\n"
                + "            </div>\n"
                + "            <div class=\"checkbox mb-3\">\n"
                + "                <label>\n"
                + "                    <input type=\"checkbox\" name=\"rememberMe\" value=\"true\"> Remember me\n"
                + "                </label>\n"
                + "            </div>\n"
                + "            <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n"
                + "        </form>\n"
                //                + "            </div>\n"
                //                + "        </div> <!-- /container -->\n"
                    + "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n"
                    + "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.1/moment.min.js\"></script>\n"
                    + "        <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n"
                + "    </body>\n"
                + "</html>"
        );
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     * @author Stefano Chirico
     * @since 1.0.0.190407
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("username");
        String password = request.getParameter("password");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            User user = userDao.getByEmailAndPassword(email, password);
            if (user == null) {
                response.sendRedirect(response.encodeRedirectURL(contextPath + "login.handler"));
            } else {
                request.getSession().setAttribute("user", user);
                if (user.getEmail().equals("stefano.chirico@unitn.it")) {
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/users.handler"));
                } else {
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/shopping.lists.handler?id=" + user.getId()));
                }
            }
        } catch (DAOException ex) {
            //TODO: log exception
            request.getServletContext().log("Impossible to retrieve the user", ex);
        }
    }
}
