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
public class Ingredient {
    private String mealId;
    private String itemId;
    private float percentage;
    private Item item;
    private NutritionalInfo nutritionalInfo;

    public Ingredient(String mealId, String itemId, float percentage, Item item, NutritionalInfo nutritionalInfo) {
        this.mealId = mealId;
        this.itemId = itemId;
        this.percentage = percentage;
        this.item = item;
        this.nutritionalInfo = nutritionalInfo;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public NutritionalInfo getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(NutritionalInfo nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }
    
    public Object[] getObjs() {
        Object[] objs = new Object[4];
        Incrementer inc = new Incrementer();
        objs[inc.next()] = mealId;
        objs[inc.next()] = itemId;
        objs[inc.next()] = percentage;
        objs[inc.next()] = false;
        return objs;
    }
}
