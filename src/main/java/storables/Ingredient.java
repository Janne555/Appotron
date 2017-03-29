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
    private Foodstuff foodstuff;

    public Ingredient(int id, int globalReferenceId, int recipeId, float mass, Foodstuff foodstuff) {
        this.id = id;
        this.globalReferenceId = globalReferenceId;
        this.recipeId = recipeId;
        this.mass = mass;
        this.foodstuff = foodstuff;
    }

    public float getTotalCalories() {
        return getFoodstuff().getCalories() * getMass();
    }

    public float getTotalCarbohydrate() {
        return getFoodstuff().getCarbohydrate() * getMass();
    }

    public float getTotalFat() {
        return getFoodstuff().getFat() * getMass();
    }

    public float getTotalProtein() {
        return getFoodstuff().getProtein() * getMass();
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

    public Foodstuff getFoodstuff() {
        return foodstuff;
    }

    public void setFoodstuff(Foodstuff foodstuff) {
        this.foodstuff = foodstuff;
    }

}
