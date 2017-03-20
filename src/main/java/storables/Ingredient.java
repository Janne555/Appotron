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
public class Ingredient {
    private int id;
    private int recipeId;
    private float amount;
    private String unit;
    private ItemInfo itemInfo;
    private int orderNumber;

    public Ingredient(int id, int recipeId, float amount, String unit, ItemInfo itemInfo, int orderNumber) {
        this.id = id;
        this.recipeId = recipeId;
        this.amount = amount;
        this.unit = unit;
        this.itemInfo = itemInfo;
        this.orderNumber = orderNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public int getItemInfoId() {
        return this.getItemInfo().getId();
    }
}
