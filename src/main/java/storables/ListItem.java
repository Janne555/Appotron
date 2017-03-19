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
    private int shoppingListId;
    private ItemInfo itemInfo;
    private int amount;

    public ListItem(int id, int shoppingList, ItemInfo itemInfo, int amount) {
        this.id = id;
        this.shoppingListId = shoppingList;
        this.itemInfo = itemInfo;
        this.amount = amount;
    }

    public int getIteminfoId() {
        return this.getItemInfo().getId();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

   
}
