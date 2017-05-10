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
    private Foodstuff foodstuff;

    public MealComponent(int id, int mealId, float mass, Foodstuff foodstuff) {
        this.id = id;
        this.mealId = mealId;
        this.mass = mass;
        this.foodstuff = foodstuff;
    }

    public MealComponent(int id, int mealId, float mass, int globalReferenceId) {
        this.id = id;
        this.mealId = mealId;
        this.mass = mass;
        this.foodstuff = new Foodstuff(null, null, null, null, 0, 0, 0, 0, globalReferenceId, 0, 0, null, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
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

    public String getName() {
        return getFoodstuff().getName();
    }

    public int getTotalCaloriesRounded() {
        return Math.round(getMass() * getFoodstuff().getCalories());
    }

    public int getTotalCarbohydrateRounded() {
        return Math.round(getMass() * getFoodstuff().getCarbohydrate());
    }

    public int getTotalFatRounded() {
        return Math.round(getMass() * getFoodstuff().getFat());
    }

    public int getTotalProteinRounded() {
        return Math.round(getMass() * getFoodstuff().getProtein());
    }

    public int getGlobalReferenceId() {
        return getFoodstuff().getGlobalReferenceId();
    }
    
    @Override
    public String toString() {
        return "ID:" + getId() + ";"
                + "NAME:" + getFoodstuff().getName() + ";"
                + "MASS:" + getMass();
    }
}
