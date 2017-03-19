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
    private Timestamp date;
    private List<ListItem> listItems;

    public ShoppingList(int id, String name, Timestamp date, List<ListItem> listItems) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.listItems = listItems;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getDate() {
        return date;
    }

    public List<ListItem> getListItems() {
        return listItems;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
