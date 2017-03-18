/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;


/**
 *
 * @author Janne
 */
public class ListItem {
    private int id;
    private int shoppingList;
    private Item item;
    private int amount;

    public ListItem(int id, int shoppingList, Item item, int amount) {
        this.id = id;
        this.shoppingList = shoppingList;
        this.item = item;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(int shoppingList) {
        this.shoppingList = shoppingList;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

   
}
