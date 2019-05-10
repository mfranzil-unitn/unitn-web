/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 09 - Shopping List Implementation
 * UniTN
 */
package it.unitn.disi.wp.lab09.shoppinglist.persistence.dao;

import it.unitn.disi.wp.commons.persistence.dao.DAO;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.lab09.shoppinglist.persistence.entities.ShoppingList;
import it.unitn.disi.wp.lab09.shoppinglist.persistence.entities.User;
import java.util.List;

/**
 * All concrete DAOs must implement this interface to handle the persistence 
 * system that interact with {@link ShoppingList shopping-lists}.
 * 
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2019.04.14
 */
public interface ShoppingListDAO extends DAO<ShoppingList, Integer> {
    /**
     * Persists the new {@link ShoppingList shopping-list} passed as parameter
     * to the storage system.
     * @param shoppingList the new {@code shopping-list} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     * 
     * @author Stefano Chirico
     * @since 1.0.0.190414
     */
    public Integer insert(ShoppingList shoppingList) throws DAOException;
    
    /**
     * Persists the already existing {@link ShoppingList shopping-list} passed
     * as parameter to the storage system.
     * @param shoppingList the {@code shopping-list} to persist.
     * @return the old record.
     * @throws DAOException if an error occurred during the persist action.
     * 
     * @author Stefano Chirico
     * @since 1.0.0.190414
     */
    public ShoppingList update(ShoppingList shoppingList) throws DAOException;
    
    /**
     * Links the passed {@code shopping_list} with the passed {@code user}.
     * @param shoppingList the shopping_list to link.
     * @param user the user to link.
     * @return {@code true} if the link has correctly persisted on the storage
     * system.
     * @throws DAOException if an error occurred during the persist action.
     * 
     * @author Stefano Chirico
     * @since 1.0.0.190414
     */
    public boolean linkShoppingListToUser(ShoppingList shoppingList, User user) throws DAOException;
    
    /**
     * Returns the list of {@link ShoppingList shopping-lists} with the
     * {@code id_user} is the one passed as parameter.
     *
     * @param userId the {@code id} of the {@code user} for which retrieve the
     * shopping-lists list.
     * @return the list of {@code shopping-list} with the user id equals to the
     * one passed as parameter or an empty list if user id is not linked to any
     * to-dos.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Stefano Chirico
     * @since 1.0.0.190414
     */
    public List<ShoppingList> getByUserId(Integer userId) throws DAOException;
}
