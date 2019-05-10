<%-- 
    Document   : users
    Created on : Apr 14, 2019
    Author     : Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
--%>

<%@page import="java.util.List"%>
<%@page import="it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException"%>
<%@page import="it.unitn.disi.wp.lab09.shoppinglist.persistence.entities.User"%>
<%@page import="it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException"%>
<%@page import="it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory"%>
<%@page import="it.unitn.disi.wp.lab09.shoppinglist.persistence.dao.UserDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    private UserDAO dao;

    public void jspInit() {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new RuntimeException(new ServletException("Impossible to get dao factory for user storage system"));
        }
        try {
            dao = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new RuntimeException(new ServletException("Impossible to get dao factory for user storage system", ex));
        }
    }

    public void jspDestroy() {
        if (dao != null) {
            dao = null;
        }
    }
%>
<%

    User authenticatedUser = (User) request.getSession(false).getAttribute("user");

    String cp = getServletContext().getContextPath();
    if (!cp.endsWith("/")) {
        cp += "/";
    }

    String avatarPath = "../images/avatars/" + authenticatedUser.getAvatarPath();

    final String contextPath = cp;

    try {
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Lab 09: Users List</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">
        <link rel="stylesheet" href="../css/floating-labels.css">
    </head>
    <body>
        <div class="container-fluid">
            <div class="card border-primary">
                <div class="card-header bg-primary text-white">
                    <h5 class="card-title">Users</h5>
                </div>
                <div class="card-body">
                    The following table lists all the users of the application.<br>
                    For each user, you can see the count of shopping-lists shared with him.<br>
                    Clicking on the number of shopping-lists, you can show the collection of shopping-lists shared with &quot;selected&quot; user.
                </div>

                <!-- Table -->
                <div class="table-responsive">
                    <table class="table table-sm table-hover">
                        <thead>
                            <tr>
                                <th>Email</th>
                                <th>First name</th>
                                <th>Last name</th>
                                <th>Shopping Lists</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<User> users = dao.getAll();
                                for (User user : users) {
                            %>
                            <tr>
                                <td><%=user.getEmail()%></td>
                                <td><%=user.getFirstName()%></td>
                                <td><%=user.getLastName()%></td>
                                <td><a href="<%=response.encodeURL(contextPath + "restricted/shopping.lists.html?id=" + user.getId())%>"><span class="badge badge-primary badge-pill"><%=user.getShoppingListsCount()%></span></a></td>
                                <td>
                                    <%
                                        if ((user.getEmail().equals(authenticatedUser.getEmail()))) {
                                    %>
                                    <a href="<%=response.encodeURL(contextPath + "restricted/editUser.html?id=" + user.getId())%>" title="edit user" data-toggle="modal" data-target="#editUserModal"><i class="fas fa-pen-square fa-lg"></i></a>
                                    <%
                                        }
                                    %>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>

                <div class="card-footer"><span class="float-left">Copyright &copy; 2019 - Stefano Chirico</span><a href="" + (contextPath restricted/logout.handler") " class="float-right"><button type="button" class="btn btn-primary btn-sm">Logout</button></a></div>
            </div>
        </div>
                        
        <!-- Modal -->
        <form action="<%=response.encodeURL(contextPath + "restricted/user.handler")%>" method="POST"  enctype="multipart/form-data">
            <div class="modal fade" id="editUserModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title" id="myModalLabel">Configure <%=authenticatedUser.getEmail()%></h4>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-window-close red-window-close"></i></span></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="idUser" value="<%=authenticatedUser.getId()%>">
                            <div class="form-label-group">
                                <input type="file" name="avatar" id="avatar" placeholder="Avatar" value="<%=authenticatedUser.getAvatarPath()%>">
                                <label for="avatar">Avatar</label>
                                <img src="<%=avatarPath%>" class="img-thumbnail">
                            </div>
                            <div class="form-label-group">
                                <input type="text" name="lastname" id="lastname" class="form-control" placeholder="Last name" value="<%=authenticatedUser.getLastName()%>" required autofocus>
                                <label for="lastname">Last name</label>
                            </div>
                            <div class="form-label-group">
                                <input type="text" name="firstname" id="firstname" class="form-control" placeholder="First name" value="<%=authenticatedUser.getFirstName()%>" required>
                                <label for="firstname">Name</label>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Save</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>                        

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    </body>
</html>
<%
} catch (DAOException ex) {
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Lab 09: Users List</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    </head>
    <body>
        <div class="jumbotron">
            <div class="container">
                <div class="card border-danger">
                    <div class="card-header bg-danger text-white">
                        <h3 class="card-title">Users</h3>
                    </div>
                    <div class="card-body">
                        Error in retrieving users list: <%=ex.getMessage()%><br>
                    </div>
                    <div class="card-footer">Copyright &copy; 2019 - Stefano Chirico</div>
                </div>
            </div>
        </div>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    </body>
</html>
<%
    }
%>