<%-- 
    Document   : shoppingLists
    Created on : Apr 14, 2019
    Author     : Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
--%>
<%@page import="it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException"%>
<%@page import="java.util.List"%>
<%@page import="it.unitn.disi.wp.lab09.shoppinglist.persistence.entities.User"%>
<%@page import="it.unitn.disi.wp.lab09.shoppinglist.persistence.entities.ShoppingList"%>
<%@page import="it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException"%>
<%@page import="it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory"%>
<%@page import="it.unitn.disi.wp.lab09.shoppinglist.persistence.dao.UserDAO"%>
<%@page import="it.unitn.disi.wp.lab09.shoppinglist.persistence.dao.ShoppingListDAO"%>
<%!
    private ShoppingListDAO shoppingListDao;
    private UserDAO userDao;

    public void jspInit() {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new RuntimeException("Impossible to get dao factory for user storage system");
        }
        try {
            shoppingListDao = daoFactory.getDAO(ShoppingListDAO.class);
        } catch (DAOFactoryException ex) {
            throw new RuntimeException("Impossible to get dao factory for shopping-list storage system", ex);
        }
        try {
            userDao = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new RuntimeException("Impossible to get dao factory for user storage system", ex);
        }
    }

    public void jspDestroy() {
        if (userDao != null) {
            userDao = null;
        }
        if (shoppingListDao != null) {
            shoppingListDao = null;
        }
    }
%>
<%
    if (response.isCommitted()) {
        getServletContext().log("shopping.lists.html is already committed");
    }
    String contextPath = getServletContext().getContextPath();
    if (!contextPath.endsWith("/")) {
        contextPath += "/";
    }

    Integer userId = null;
    User user = null;
    User authenticatedUser = null;
    if (session != null) {
        authenticatedUser = (User) session.getAttribute("user");
    }
    try {
        userId = Integer.valueOf(request.getParameter("id"));
    } catch (RuntimeException ex) {
        if (session != null) {
            user = authenticatedUser;
        }
        if (user != null) {
            userId = user.getId();
        }
    }
    if (userId == null) {
        if (!response.isCommitted()) {
            response.sendRedirect(response.encodeRedirectURL(contextPath + "login.html"));
        }
    }

    try {

        if (user == null) {
            user = userDao.getByPrimaryKey(userId);
        }
        List<ShoppingList> shoppingLists = shoppingListDao.getByUserId(userId);
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Lab 09: Shopping lists shared with <%=user.getFirstName() + " " + user.getLastName()%></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" crossorigin="anonymous">
        <link rel="stylesheet" href="../css/floating-labels.css">
    </head>
    <body>
        <div class="container-fluid">
            <div class="card border-primary">
                <div class="card-header bg-primary text-white">
                    <h5 class="card-title float-left">Shopping Lists</h5>
                    <div class="btn-group float-right" role="group">
                        <button type="button" class="btn btn-outline-light bg-light text-primary btn-sm float-right" data-target="<%=contextPath%>restricted/export2PDF?id=<%=userId%>">
                            <a href="<%=contextPath%>restricted/export2PDF?id=<%=userId%>" class="far fa-file-pdf fa-2x" aria-hidden="true"></a>
                        </button>
                        <button type="button" class="btn btn-outline-light bg-light text-primary btn-sm float-right" data-toggle="modal" data-target="#editDialog">
                            <span class="fas fa-plus" aria-hidden="true"></span>
                        </button>
                    </div>
                </div>
                <div class="card-body">
                    The following table lists all the shopping-lists shared with &quot;<%=user.getFirstName() + "  " + user.getLastName()%>&quot;.<br>
                </div>

                <!-- Shopping Lists cards -->
                <div id="accordion">
                    <%
                        if (shoppingLists.isEmpty()) {
                    %>
                    <div class="card">
                        <div class="card-body">
                            This collection is empty.
                        </div>
                    </div>
                    <%
                    } else {
                        int index = 1;
                        for (ShoppingList shoppingList : shoppingLists) {
                    %>
                    <div class="card">
                        <div class="card-header" id="heading<%=index%>">
                            <h5 class="mb-0">
                                <button class="btn btn-link" data-toggle="collapse" data-target="#collapse<%=index%>" aria-expanded="true" aria-controls="collapse<%=index%>">
                                    <%=shoppingList.getName()%> 
                                </button>
                                <div class="float-right"><a href="<%=contextPath%>restricted/edit.shopping.list.html?id=<%=shoppingList.getId()%>" class="fas fa-pen-square" title="edit &quot;<%=shoppingList.getName()%>&quot; shopping list" data-toggle="modal" data-target="#editDialog" data-shopping-list-id="<%=shoppingList.getId()%>" data-shopping-list-name="<%=shoppingList.getName()%>" data-shopping-list-description="<%=shoppingList.getDescription()%>"></a></div>
                            </h5>
                        </div>
                        <div id="collapse<%=index%>" class="collapse<%=(index == 1 ? " show" : "")%>" aria-labelledby="heading<%=(index++)%>" data-parent="#accordion">
                            <div class="card-body">
                                <%=shoppingList.getDescription()%>
                            </div>
                        </div>
                    </div>
                    <%
                            }
                        }
                    %>
                </div>

                <div class="card-footer">
                    <span class="float-left">Copyright &copy; 2019 - Stefano Chirico</span>
                    <%
                        if ((authenticatedUser != null) && authenticatedUser.getEmail().equals("stefano.chirico@unitn.it")) {
                    %>
                    <a class="float-right" href="users.html"><button type="button" class="btn btn-primary btn-sm">Go to Users List</button></a>
                    <%
                    } else {
                    %>
                    <a class="float-right" href="logout.handler"><button type="button" class="btn btn-primary btn-sm">Logout</button></a>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
        <!-- Modal -->
        <form action="shopping.lists.handler" method="POST">
            <div class="modal fade" id="editDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3 class="modal-title" id="titleLabel">Create new Shopping List</h3>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="idUser" value="<%=user.getId()%>">
                            <input type="hidden" name="idShoppingList" id="idShoppingList">
                            <div class="form-label-group">
                                <input type="text" name="name" id="name" class="form-control" placeholder="Name" required autofocus>
                                <label for="name">Name</label>
                            </div>
                            <div class="form-label-group">
                                <input type="text" name="description" id="description" class="form-control" placeholder="Description" required>
                                <label for="description">Description</label>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button id="editDialogSubmit" type="submit" class="btn btn-primary">Create</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.1/moment.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(function () {
                $("#editDialog").on("show.bs.modal", function (e) {
                    var target = $(e.relatedTarget);
                    var shoppingListId = target.data("shopping-list-id");
                    if (shoppingListId !== undefined) {
                        var shoppingListName = target.data("shopping-list-name");
                        var shoppingListDescription = target.data("shopping-list-description");

                        $("#titleLabel").html("Edit Shopping List (" + shoppingListId + ")");
                        $("#editDialogSubmit").html("Update");
                        $("#idShoppingList").val(shoppingListId);
                        $("#name").val(shoppingListName);
                        $("#description").val(shoppingListDescription);
                    } else {
                        $("#titleLabel").html("Create new Shopping List");
                        $("#editDialogSubmit").html("Create");
                    }
                });
            });
        </script>
    </body>
</html>
<%
} catch (DAOException ex) {
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Lab 09: Shopping list</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    </head>
    <body>
        <div class="jumbotron">
            <div class="container">
                <div class="card border-danger">
                    <div class="card-header">
                        <h3 class="card-title bg-danger text-white">Shopping Lists</h3>
                    </div>
                    <div class="card-body">
                        Error in retrieving shopping lists: <%=ex.getMessage()%><br>
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