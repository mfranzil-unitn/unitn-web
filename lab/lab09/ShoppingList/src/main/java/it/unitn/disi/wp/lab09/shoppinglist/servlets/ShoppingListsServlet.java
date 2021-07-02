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
import it.unitn.disi.wp.lab09.shoppinglist.persistence.dao.ShoppingListDAO;
import it.unitn.disi.wp.lab09.shoppinglist.persistence.dao.UserDAO;
import it.unitn.disi.wp.lab09.shoppinglist.persistence.entities.ShoppingList;
import it.unitn.disi.wp.lab09.shoppinglist.persistence.entities.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles user shopping lists.
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2019.04.14
 */
public class ShoppingListsServlet extends HttpServlet {

    private ShoppingListDAO shoppingListDao;
    private UserDAO userDao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            shoppingListDao = daoFactory.getDAO(ShoppingListDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for shopping-list storage system", ex);
        }
        try {
            userDao = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = null;
        try {
            userId = Integer.valueOf(request.getParameter("idUser"));
        } catch (RuntimeException ex) {
            //TODO: Handle the exception
        }
        Integer shoppingListId = null;
        try {
            shoppingListId = Integer.valueOf(request.getParameter("idShoppingList"));
        } catch (RuntimeException ex) {
            //TODO: Handle the exception
        }
        String name = request.getParameter("name");
        String descriptiopn = request.getParameter("description");

        try {
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setId(shoppingListId);
            shoppingList.setName(name);
            shoppingList.setDescription(descriptiopn);

            User user = userDao.getByPrimaryKey(userId);

            if (shoppingListId == null) {
                shoppingListDao.insert(shoppingList);
            } else {
                shoppingListDao.update(shoppingList);
            }
            shoppingListDao.linkShoppingListToUser(shoppingList, user);
        } catch (DAOException ex) {
            //TODO: log exception
        }

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/shopping.lists.html?id=" + userId));
    }
}
