/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 09 - Shopping List Implementation
 * UniTN
 */
package it.unitn.disi.wp.lab09.shoppinglist.servlets;

import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import it.unitn.disi.wp.lab09.shoppinglist.persistence.dao.UserDAO;
import it.unitn.disi.wp.lab09.shoppinglist.persistence.entities.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles the login web page.
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2019.04.14
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
                response.sendRedirect(response.encodeRedirectURL(contextPath + "login.html"));
            } else {
                request.getSession().setAttribute("user", user);
                if (user.getEmail().equals("stefano.chirico@unitn.it")) {
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/users.html"));
                } else {
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/shopping.lists.html?id=" + user.getId()));
                }
            }
        } catch (DAOException ex) {
            //TODO: log exception
            request.getServletContext().log("Impossible to retrieve the user", ex);
        }
    }
}
