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
public class NutritionalInfo {
    private ItemInfo itemInfo;
    private float energy;
    private float carbohydrates;
    private float fat;
    private float protein;

    public NutritionalInfo(ItemInfo itemInfo, float energy, float carbohydrates, float fat, float protein) {
        this.itemInfo = itemInfo;
        this.energy = energy;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.protein = protein;
    }
    
    public int getItemInfoId() {
        return this.itemInfo.getId();
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }
}
