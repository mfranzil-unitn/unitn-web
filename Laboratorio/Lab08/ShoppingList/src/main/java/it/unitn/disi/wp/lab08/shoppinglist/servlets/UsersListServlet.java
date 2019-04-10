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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles the users web page.
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2019.04.06
 */
public class UsersListServlet extends HttpServlet {

    private UserDAO dao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            dao = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cp = getServletContext().getContextPath();
        if (!cp.endsWith("/")) {
            cp += "/";
        }
        final String contextPath = cp;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            List<User> users = dao.getAll();

            out.println(
                    "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <title>Lab 08: Users List</title>\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n"
                    + "        <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n"
                    + "        <link rel=\"stylesheet\" href=\"../css/floating-labels.css\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    //+ "        <div class=\"jumbotron jumbotron-fluid\">\n"
                    + "            <div class=\"container-fluid\">\n"
                    + "                <div class=\"card border-primary\">\n"
                    + "                    <div class=\"card-header bg-primary text-white\">\n"
                    + "                        <h5 class=\"card-title\">Users</h5>\n"
                    + "                    </div>\n"
                    + "                    <div class=\"card-body\">\n"
                    + "                        The following table lists all the users of the application.<br>\n"
                    + "                        For each user, you can see the count of shopping-lists shared with him.<br>\n"
                    + "                        Clicking on the number of shopping-lists, you can show the collection of shopping-lists shared with &quot;selected&quot; user.\n"
                    + "                    </div>\n"
                    + "\n"
                    + "                    <!-- Table -->\n"
                    + "                    <div class=\"table-responsive\">\n"
                    + "                        <table class=\"table table-sm table-hover\">\n"
                    + "                            <thead>\n"
                    + "                                <tr>\n"
                    + "                                    <th>Email</th>\n"
                    + "                                    <th>First name</th>\n"
                    + "                                    <th>Last name</th>\n"
                    + "                                    <th>Shopping Lists</th>\n"
                    + "                                </tr>\n"
                    + "                            </thead>\n"
                    + "                            <tbody>\n"
            );
            for (User user : users) {
                out.println(
                        "                                <tr>\n"
                        + "                                    <td>" + user.getEmail() + "</td>\n"
                        + "                                    <td>" + user.getFirstName() + "</td>\n"
                        + "                                    <td>" + user.getLastName() + "</td>\n"
                        + "                                    <td><a href=\"" + response.encodeURL(contextPath + "restricted/shopping.lists.handler?id=" + user.getId()) + "\"><span class=\"badge badge-primary badge-pill\">" + user.getShoppingListsCount() + "</span></a></td>\n"
                        + "                                </tr>\n"
                );
            }
            out.println(
                    "                            </tbody>\n"
                    + "                        </table>\n"
                    + "                  </div>\n"
                    + "                    \n"
                    + "                    <div class=\"card-footer\"><span class=\"float-left\">Copyright &copy; 2019 - Stefano Chirico</span><a href=\"" + (contextPath + "restricted/logout.handler") + "\" class=\"float-right\"><button type=\"button\" class=\"btn btn-primary btn-sm\">Logout</button></a></div>\n"
                    + "                </div>\n"
                    + "            </div>\n"
                    //+ "        </div>\n"
                    + "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n"
                    + "        <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n"
                    + "    </body>\n"
                    + "</html>"
            );

        } catch (DAOException ex) {
            out.println(
                    "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <title>Lab 08: Users List</title>\n"
                    + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n"
                    + "        <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <div class=\"jumbotron\">\n"
                    + "            <div class=\"container\">\n"
                    + "                <div class=\"card border-danger\">\n"
                    + "                    <div class=\"card-header bg-danger text-white\">\n"
                    + "                        <h3 class=\"card-title\">Users</h3>\n"
                    + "                    </div>\n"
                    + "                    <div class=\"card-body\">\n"
                    + "                        Error in retriving users list: " + ex.getMessage() + "<br>\n"
                    + "                    </div>\n"
                    + "                    <div class=\"card-footer\">Copyright &copy; 2019 - Stefano Chirico</div>\n"
                    + "                </div>\n"
                    + "            </div>\n"
                    + "        </div>\n"
                    + "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n"
                    + "        <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n"
                    + "    </body>\n"
                    + "</html>"
            );
        }
    }
}
