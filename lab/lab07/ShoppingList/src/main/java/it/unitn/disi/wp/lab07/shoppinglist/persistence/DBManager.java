/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 07 - Shopping List
 * UniTN
 */
package it.unitn.disi.wp.lab07.shoppinglist.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2019.04.01
 */
public class DBManager {

    private final transient Connection CON;

    public DBManager(String dbUrl) throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver", true, getClass().getClassLoader());
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe.getMessage(), cnfe.getCause());
        }

        CON = DriverManager.getConnection(dbUrl);
    }

    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException sqle) {
            Logger.getLogger(DBManager.class.getName()).info(sqle.getMessage());
        }
    }

    public List<User> getUsers() throws SQLException {
        List<User> users = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM users ORDER BY lastname")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setFirstName(rs.getString("name"));
                    user.setLastName(rs.getString("lastname"));

                    try (PreparedStatement slstm = CON.prepareStatement("SELECT count(id_shopping_list) FROM users_shopping_lists WHERE id_user = ?")) {
                        slstm.setInt(1, user.getId());
                        try (ResultSet slrs = slstm.executeQuery()) {
                            slrs.next();
                            user.setShoppingListsCount(slrs.getInt(1));
                        }
                    }

                    users.add(user);
                }
            }
        }

        return users;
    }

    public User getUser(Integer userId) throws SQLException {
        if (userId == null) {
            throw new SQLException("userId is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            stm.setInt(1, userId);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("name"));
                user.setLastName(rs.getString("lastname"));

                try (PreparedStatement slstm = CON.prepareStatement("SELECT count(id_shopping_list) FROM users_shopping_lists WHERE id_user = ?")) {
                    slstm.setInt(1, user.getId());
                    try (ResultSet slrs = slstm.executeQuery()) {
                        slrs.next();
                        user.setShoppingListsCount(slrs.getInt(1));
                    }
                }

                return user;
            }
        }
    }

    public List<ShoppingList> getShoppingListsByUser(Integer userId) throws SQLException {
        if (userId == null) {
            throw new SQLException("userId is null");
        }

        List<ShoppingList> shoppingLists = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM shopping_lists WHERE id IN (SELECT id_shopping_list FROM users_shopping_lists WHERE id_user = ?) ORDER BY name")) {
            stm.setInt(1, userId);
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    ShoppingList shoppingList = new ShoppingList();
                    shoppingList.setId(rs.getInt("id"));
                    shoppingList.setDescription(rs.getString("description"));
                    shoppingList.setName(rs.getString("name"));

                    shoppingLists.add(shoppingList);
                }
            }
        }

        return shoppingLists;
    }

    public void addShoppingList(ShoppingList shoppingList) throws SQLException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO shopping_lists (name, description) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, shoppingList.getName());
            ps.setString(2, shoppingList.getDescription());

            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                shoppingList.setId(rs.getInt(1));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void linkShoppingListToUser(ShoppingList shoppingList, User user) throws SQLException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO users_shopping_lists (id_user, id_shopping_list, can_read, can_write, is_owner, id_creation_user, id_last_modification_user, timestamp_creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            ps.setInt(1, user.getId());
            ps.setInt(2, shoppingList.getId());
            ps.setBoolean(3, true);
            ps.setBoolean(4, true);
            ps.setBoolean(5, true);
            ps.setInt(6, user.getId());
            ps.setInt(7, user.getId());
            ps.setTimestamp(8, Timestamp.from(Instant.now()));

            ps.executeUpdate();            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
