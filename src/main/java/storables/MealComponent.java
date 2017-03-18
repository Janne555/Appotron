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
public class MealComponent {
    private String mealId;
    private String itemId;
    private float mass;
    private Item item;
    private NutritionalInfo nutritionalInfo;

    public MealComponent(String mealId, String itemId, float mass, Item item, NutritionalInfo nutritionalInfo) {
        this.mealId = mealId;
        this.itemId = itemId;
        this.mass = mass;
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

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
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
    
    
}
