/*
 * AA 2018-2019
 * Introduction to Web Programming
 * Lab 06 - Exercise 02
 * UniTN
 */
package it.unitn.disi.wp.lab06.exercise02;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Example of how to use Servlet and session to handle an items collection
 *
 * @author Stefano Chirico
 * @version 1.0.0
 * @since 1.0.0 2019.03.24
 */
public class ShowItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        if (previousItems == null) {
            previousItems = new ArrayList<>();
            session.setAttribute("previousItems", previousItems);
        }

        String newItem = request.getParameter("newItem");

        synchronized (previousItems) {
            if (newItem != null) {
                previousItems.add(newItem);
            }
        }

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println(
                "<!DOCTYPE html>\n"
                + "<html>\n"
                + " <head>\n"
                + "     <title>Simple Cart Handler: Items list</title>\n"
                + " </head>\n"
                + " <body>\n"
                + "     <h1 align=\"center\">Items purchased</h1>\n"
        );

        if (previousItems.isEmpty()) {
            out.println("       <p>No items</p>\n");
        } else {
            out.println("       <ul>\n");
            for (String item : previousItems) {
                out.println("           <li>".concat(item).concat("</li>\n"));
            }
            out.println("       </ul>\n");
        }

        out.println(
                "       <p>To order new items, click<a href=\"".concat(response.encodeURL("index.html").concat("\">here</a></p>\n"))
                + " </body>\n"
                + "</html>\n"
        );
    }

}
