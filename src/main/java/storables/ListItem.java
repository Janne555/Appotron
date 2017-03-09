/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import util.Incrementer;

/**
 *
 * @author Janne
 */
public class ListItem implements Objectify {
    private int id;
    private int shoppingList;
    private String serialNumber;
    private String itemName;
    private int amount;

    public ListItem(int id, int shoppingList, String serialNumber, String itemName, int amount) {
        this.id = id;
        this.shoppingList = shoppingList;
        this.serialNumber = serialNumber;
        this.itemName = itemName;
        this.amount = amount;
    }


    public int getShoppingList() {
        return shoppingList;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public Object[] getObjs() {
        Object[] objs = new Object[3];
        Incrementer inc = new Incrementer();
        objs[inc.next()] = this.shoppingList;
        objs[inc.next()] = this.serialNumber;
        objs[inc.next()] = this.amount;
        return objs;
    }

    @Override
    public Object[] getObjsId() {
        Object[] objs = new Object[4];
        Incrementer inc = new Incrementer();
        objs[inc.next()] = this.shoppingList;
        objs[inc.next()] = this.serialNumber;
        objs[inc.next()] = this.amount;
        objs[inc.next()] = this.id;
        return objs;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }
    
    @Override
    public String toString() {
        return "id: " + id + "; "
                + "list_id: " + shoppingList + "; "
                + "serial: " + serialNumber + "; "
                + "name: " + itemName + "; "
                + "amount: " + amount;
    }
    
}
