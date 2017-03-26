/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.util.Map;

/**
 *
 * @author janne
 */
public class Foodstuff {
    private String name;
    private String identifier;
    private String producer;
    private float calories;
    private float carbohydrate;
    private float fat;
    private float protein;
    private int globalReference;
    private int foodstuffMeta;

    public Foodstuff(String name, String identifier, String producer, float calories, float carbohydrate, float fat, float protein, int globalReference, int foodstuffMeta) {
        this.name = name;
        this.identifier = identifier;
        this.producer = producer;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
        this.globalReference = globalReference;
        this.foodstuffMeta = foodstuffMeta;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(float carbohydrate) {
        this.carbohydrate = carbohydrate;
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

    public int getGlobalReference() {
        return globalReference;
    }

    public void setGlobalReference(int globalReference) {
        this.globalReference = globalReference;
    }

    public int getFoodstuffMeta() {
        return foodstuffMeta;
    }

    public void setFoodstuffMeta(int foodstuffMeta) {
        this.foodstuffMeta = foodstuffMeta;
    }
    
    
}
