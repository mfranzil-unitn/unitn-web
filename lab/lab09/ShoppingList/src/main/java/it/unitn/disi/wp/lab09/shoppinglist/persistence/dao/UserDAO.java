/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 09 - Shopping List Implementation
 * UniTN
 */
package it.unitn.disi.wp.lab09.shoppinglist.persistence.dao;

import it.unitn.disi.wp.commons.persistence.dao.DAO;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.lab09.shoppinglist.persistence.entities.User;

/**
 * All concrete DAOs must implement this interface to handle the persistence 
 * system that interact with {@link User users}.
 * 
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2019.04.14
 */
public interface UserDAO extends DAO<User, Integer> {
    /**
     * Returns the {@link User user} with the given {@code email} and
     * {@code password}.
     * @param email the email of the user to get.
     * @param password the password of the user to get.
     * @return the {@link User user} with the given {@code username} and
     * {@code password}..
     * @throws DAOException if an error occurred during the information
     * retrieving.
     * 
     * @author Stefano Chirico
     * @since 1.0.0.190414
     */
    public User getByEmailAndPassword(String email, String password) throws DAOException;
    
    /**
     * Update the user passed as parameter and returns it.
     * @param user the user used to update the persistence system.
     * @return the updated user.
     * @throws DAOException if an error occurred during the action.
     * 
     * @author Stefano Chirico
     * @since 1.0.0.190414
     */
    public User update(User user) throws DAOException;
}
