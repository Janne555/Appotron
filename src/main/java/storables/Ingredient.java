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
    private float unitsPerGram;
    private NutritionalInfo nutritionalInfo;

    public Ingredient(int id, int recipeId, float amount, String unit, ItemInfo itemInfo, float unitsPerGram, NutritionalInfo nutritionalInfo) {
        this.id = id;
        this.recipeId = recipeId;
        this.amount = amount;
        this.unit = unit;
        this.itemInfo = itemInfo;
        this.nutritionalInfo = nutritionalInfo;
        this.unitsPerGram = unitsPerGram;
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

    public int getItemInfoId() {
        return this.getItemInfo().getId();
    }

    public void setUnitsPerGram(float unitsPerGram) {
        this.unitsPerGram = unitsPerGram;
    }

    public float getUnitsPerGram() {
        return unitsPerGram;
    }

    public float getMass() {
        return getUnitsPerGram() * getAmount();
    }

    public void setNutritionalInfo(NutritionalInfo nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }

    public NutritionalInfo getNutritionalInfo() {
        return nutritionalInfo;
    }

    public float getCalories() {
        return nutritionalInfo.getCalories() / 100 * unitsPerGram * amount;
    }

    public float getCarbohydrate() {
        return nutritionalInfo.getCarbohydrate() / 100 * unitsPerGram * amount;
    }

    public float getFat() {
        return nutritionalInfo.getFat() / 100 * unitsPerGram * amount;
    }
    
    public float getProtein() {
        return nutritionalInfo.getProtein() / 100 * unitsPerGram * amount;
    }
}
