/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 07 - Shopping List
 * UniTN
 */
package it.unitn.disi.wp.lab07.shoppinglist.listeners;

import it.unitn.disi.wp.lab07.shoppinglist.persistence.DBManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2018.04.01
 */
public class WebContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String dburl = sce.getServletContext().getInitParameter("dburl");

        try {

            DBManager manager = new DBManager(dburl);

            sce.getServletContext().setAttribute("dbmanager", manager);

        } catch (SQLException ex) {

            Logger.getLogger(getClass().getName()).severe(ex.toString());

            throw new RuntimeException(ex);

        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBManager.shutdown();
    }
}