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
    private String identifier;
    private float energy;
    private float carbohydrates;
    private float fat;
    private float protein;

    public NutritionalInfo(String identifier, float energy, float carbohydrates, float fat, float protein) {
        this.identifier = identifier;
        this.energy = energy;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.protein = protein;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
}
