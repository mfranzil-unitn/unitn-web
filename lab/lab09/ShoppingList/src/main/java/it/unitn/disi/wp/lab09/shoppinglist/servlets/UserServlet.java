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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet that handles user configuration.
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2019.04.14
 */
@MultipartConfig
public class UserServlet extends HttpServlet {

    private UserDAO userDao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for storage system");
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
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String avatarsFolder = getServletContext().getInitParameter("avatarsFolder");
        if (avatarsFolder == null) {
            throw new ServletException("Avatars folder not configured");
        }
        
        avatarsFolder = getServletContext().getRealPath(avatarsFolder);
        
        //TODO: If avatarsFolder doesn't exist, create it
        
        String cp = getServletContext().getContextPath();
        if (!cp.endsWith("/")) {
            cp += "/";
        }

        Integer userId = null;
        String userIdParam = request.getParameter("idUser");
        if (userIdParam != null) {
            userId = Integer.valueOf(userIdParam);
        }
        String lastName = request.getParameter("lastname");
        if (lastName == null) {
            lastName = "";
        }
        String firstName = request.getParameter("firstname");
        if (firstName == null) {
            firstName = "";
        }

        User user = null;
        if (userId != null) {
            try {
                user = userDao.getByPrimaryKey(userId);
            } catch (DAOException ex) {
                response.sendError(500, ex.getMessage());
                return;
            }
        }

        if (user == null) {
            response.sendRedirect(cp + "login.html");
            return;
        }

        boolean modified = false;
        if (!firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
            modified = true;
        }
        if (!lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
            modified = true;
        }

        Part filePart = request.getPart("avatar");
        if ((filePart != null) && (filePart.getSize() > 0)) {
            String filename = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();//MSIE  fix.
            try (InputStream fileContent = filePart.getInputStream()) {
                File file = new File(avatarsFolder, filename);
                Files.copy(fileContent, file.toPath());
                if (!filename.equals(user.getAvatarPath())) {
                    user.setAvatarPath(filename);
                    modified = true;
                }
            } catch (FileAlreadyExistsException ex) {
                getServletContext().log("File \"" + filename + "\" already exists on the server");
            } catch (RuntimeException ex) {
                //TODO: handle the exception
                getServletContext().log("impossible to upload the file", ex);
            }
            
        }
        if (modified) {
            try {
                userDao.update(user);
            } catch (DAOException ex) {
                response.sendError(500, ex.getMessage());
            }
            User authenticatedUser = (User) request.getSession(false).getAttribute("user");
            if (authenticatedUser.getId().equals(user.getId())) {
                authenticatedUser.setAvatarPath(user.getAvatarPath());
                authenticatedUser.setEmail(user.getEmail());
                authenticatedUser.setFirstName(user.getFirstName());
                authenticatedUser.setLastName(user.getLastName());
                authenticatedUser.setPassword(user.getPassword());
                authenticatedUser.setShoppingListsCount(user.getShoppingListsCount());
            }
        }
        
        if (user.getEmail().equals("stefano.chirico@unitn.it")) {
            response.sendRedirect(cp+"restricted/users.html?id="+user.getId());
        } else {
            response.sendRedirect(cp+"restricted/shopping.lists.html?id="+user.getId());
        }
    }
}
