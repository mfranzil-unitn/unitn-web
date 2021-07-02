/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 07 - Shopping List
 * UniTN
 */
package it.unitn.disi.wp.lab07.shoppinglist.servlets;

import it.unitn.disi.wp.lab07.shoppinglist.persistence.DBManager;
import it.unitn.disi.wp.lab07.shoppinglist.persistence.ShoppingList;
import it.unitn.disi.wp.lab07.shoppinglist.persistence.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Shopping lists handler
 *
 * @author Stefano Chirico
 * @version 1.0.0
 * @since 1.0.0 2019.04.01
 */
public class ShoppingListsServlet extends HttpServlet {
    private DBManager dbManager;

    @Override
    public void init() throws ServletException {
        dbManager = (DBManager) super.getServletContext().getAttribute("dbmanager");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = Integer.valueOf(request.getParameter("idUser"));
        String name = request.getParameter("name");
        String descriptiopn = request.getParameter("description");

        try {
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setName(name);
            shoppingList.setDescription(descriptiopn);

            User user = dbManager.getUser(userId);
            
            dbManager.addShoppingList(shoppingList);
            dbManager.linkShoppingListToUser(shoppingList, user);
        } catch (SQLException ex) {
            //TODO: log exception
            System.out.println(ex.getMessage());
        }

        response.sendRedirect("shopping.lists.handler?id=" + userId);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Integer userId = Integer.valueOf(request.getParameter("id"));
        try {
            User user = dbManager.getUser(userId);
            List<ShoppingList> shoppingLists = dbManager.getShoppingListsByUser(userId);

            out.println(
                    "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <title>Lab 07: Shopping lists shared with " + user.getFirstName() + " " + user.getLastName() + "</title>\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n"
                    + "        <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n"
                    + "        <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.0.8/css/all.css\" crossorigin=\"anonymous\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <div class=\"jumbotron\">\n"
                    + "            <div class=\"container\">\n"
                    + "                <div class=\"card border-primary\">\n"
                    + "                    <div class=\"card-header bg-primary text-white\">\n"
                    + "                        <h5 class=\"card-title float-left\">Shopping Lists</h5><button type=\"button\" class=\"btn btn-outline-light bg-light text-primary btn-sm float-right\" data-toggle=\"modal\" data-target=\"#myModal\"><span class=\"fas fa-plus\" aria-hidden=\"true\"></span></button>\n"
                    + "                    </div>\n"
                    + "                    <div class=\"card-body\">\n"
                    + "                        The following table lists all the shopping-lists shared with &quot;" + user.getFirstName() + " " + user.getLastName() + "&quot;.<br>\n"
                    + "                    </div>\n"
                    + "\n"
                    + "                    <!-- Table -->\n"
                    + "                    <table class=\"table\">\n"
                    + "                        <tr>\n"
                    + "                            <th>Name</th>\n"
                    + "                            <th>Description</th>\n"
                    + "                        </tr>\n"
            );
            for (ShoppingList shoppingList : shoppingLists) {
                out.println(
                        "                        <tr>\n"
                        + "                            <td>" + shoppingList.getName() + "</td>\n"
                        + "                            <td>" + shoppingList.getDescription() + "</td>\n"
                        + "                        </tr>\n"
                );
            }
            out.println(
                    "                    </table>\n"
                    + "                    \n"
                    + "                    <div class=\"card-footer\"><span class=\"float-left\">Copyright &copy; 2019 - Stefano Chirico</span><a class=\"float-right\" href=\"users.handler\"><button type=\"button\" class=\"btn btn-primary btn-sm\">Go to Users List</button></a></div>\n"
                    + "                </div>\n"
                    + "            </div>\n"
                    + "        </div>\n"
            );
            out.println(
                    "        <!-- Modal -->\n"
                    + "        <form action=\"shopping.lists.handler\" method=\"POST\">\n"
                    + "            <div class=\"modal fade\" id=\"myModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\">\n"
                    + "                <div class=\"modal-dialog\" role=\"document\">\n"
                    + "                    <div class=\"modal-content\">\n"
                    + "                        <div class=\"modal-header\">\n"
                    + "                            <h3 class=\"modal-title\" id=\"myModalLabel\">Create new Shopping List</h3>\n"
                    + "                            <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>\n"
                    + "                        </div>\n"
                    + "                        <div class=\"modal-body\">\n"
                    + "                            <input type=\"hidden\" name=\"idUser\" value=\"" + user.getId() + "\">\n"
                    + "                            <label for=\"name\" class=\"sr-only\">Name</label>\n"
                    + "                            <input type=\"text\" name=\"name\" id=\"name\" class=\"form-control\" placeholder=\"Name\" required autofocus>\n"
                    + "                            <label for=\"description\" class=\"sr-only\">Description</label>\n"
                    + "                            <input type=\"text\" name=\"description\" id=\"description\" class=\"form-control\" placeholder=\"Description\" required>\n"
                    + "                        </div>\n"
                    + "                        <div class=\"modal-footer\">\n"
                    + "                            <button type=\"submit\" class=\"btn btn-primary\">Create</button>\n"
                    + "                            <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Cancel</button>\n"
                    + "                        </div>\n"
                    + "                    </div>\n"
                    + "                </div>\n"
                    + "            </div>\n"
                    + "        </form>\n"
            );
            out.println(
                    "        <!-- Latest compiled and minified JavaScript -->\n"
                    + "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\n"
                    + "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.1/moment.min.js\"></script>\n"
                    + "        <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n"
                    + "    </body>\n"
                    + "</html>"
            );
        } catch (SQLException ex) {
            out.println(
                    "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <title>Lab 06: ToDo List</title>\n"
                    + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                    + "        <!-- Latest compiled and minified CSS -->\n"
                    + "        <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" crossorigin=\"anonymous\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <div class=\"jumbotron\">\n"
                    + "            <div class=\"container\">\n"
                    + "                <div class=\"card border-danger\">\n"
                    + "                    <div class=\"card-header\">\n"
                    + "                        <h3 class=\"card-title bg-danger text-white\">ToDos</h3>\n"
                    + "                    </div>\n"
                    + "                    <div class=\"card-body\">\n"
                    + "                        Error in retriving todos list: " + ex.getMessage() + "<br>\n"
                    + "                    </div>\n"
                    + "                    <div class=\"card-footer\">Copyright &copy; 2018 - Stefano Chirico</div>\n"
                    + "                </div>\n"
                    + "            </div>\n"
                    + "        </div>\n"
                    + "        <!-- Latest compiled and minified JavaScript -->\n"
                    + "        <script src=\"https://code.jquery.com/jquery-3.2.1.min.js\" crossorigin=\"anonymous\"></script>\n"
                    + "        <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" crossorigin=\"anonymous\"></script>\n"
                    + "    </body>\n"
                    + "</html>"
            );
        }
    }
}
