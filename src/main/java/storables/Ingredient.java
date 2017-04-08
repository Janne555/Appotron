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
    private int globalReferenceId;
    private int recipeId;
    private float mass;
    private float calories;
    private float carbohydrate;
    private float fat;
    private float protein;
    private Foodstuff foodstuff;

    public Ingredient(int id, int globalReferenceId, int recipeId, float mass, float calories, float carbohydrate, float fat, float protein, Foodstuff foodstuff) {
        this.id = id;
        this.globalReferenceId = globalReferenceId;
        this.recipeId = recipeId;
        this.mass = mass;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
        this.foodstuff = foodstuff;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGlobalReferenceId() {
        return globalReferenceId;
    }

    public void setGlobalReferenceId(int globalReferenceId) {
        this.globalReferenceId = globalReferenceId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
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

    public Foodstuff getFoodstuff() {
        return foodstuff;
    }

    public void setFoodstuff(Foodstuff foodstuff) {
        this.foodstuff = foodstuff;
    }

}
