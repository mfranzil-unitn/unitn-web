/*
 * AA 2017-2018
 * Introduction to Web Programming
 * Lab 06 - ShoppingList List
 * UniTN
 */
package it.unitn.disi.wp.lab07.shoppinglist.persistence;


/**
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2018.04.01
 */
public class ShoppingList {
    private Integer id;
    private String name;
    private String description;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
