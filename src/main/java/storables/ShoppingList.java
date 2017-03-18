/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Janne
 */
public class ShoppingList {
    private int id;
    private String name;
    private Timestamp createdOn;
    private List<ListItem> listItems;

    public ShoppingList(int id, String name, Timestamp createdOn, List<ListItem> listItems) {
        this.id = id;
        this.name = name;
        this.createdOn = createdOn;
        this.listItems = listItems;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public List<ListItem> getListItems() {
        return listItems;
    }
}
