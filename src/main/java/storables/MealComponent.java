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
    private int id;
    private int mealId;
    private float mass;
    private ItemInfo item;
    private NutritionalInfo nutritionalInfo;

    public MealComponent(int id, int mealId, float mass, ItemInfo item, NutritionalInfo nutritionalInfo) {
        this.id = id;
        this.mealId = mealId;
        this.mass = mass;
        this.item = item;
        this.nutritionalInfo = nutritionalInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void setItem(ItemInfo item) {
        this.item = item;
    }

    public ItemInfo getItem() {
        return item;
    }

    public NutritionalInfo getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(NutritionalInfo nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }
    
    public int getItemId() {
        return item.getId();
    }
    
    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
    
}
