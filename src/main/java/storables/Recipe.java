/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Janne
 */
public class Recipe {

    private int id;
    private String name;
    private String directions;
    private String description;
    private String type;
    private float totalMass;
    private Timestamp date;
    private List<Ingredient> ingredients;

    public Recipe(int id, String name, String directions, String description, String type, float totalMass, Timestamp date, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.directions = directions;
        this.description = description;
        this.type = type;
        this.totalMass = totalMass;
        this.date = date;
        this.ingredients = ingredients;
        if (totalMass <= 0 && ingredients != null) {
            for (Ingredient i : ingredients) {
                this.totalMass += i.getMass();
            }
        }
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public float getTotalMass() {
        return totalMass;
    }

    public void setTotalMass(float totalMass) {
        this.totalMass = totalMass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public float getTotalCalories() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getCalories();
        }
        return sum;
    }

    public float getTotalCarbohydrate() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getCarbohydrate();
        }
        return sum;
    }

    public float getTotalFat() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getFat();
        }
        return sum;
    }

    public float getTotalProtein() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getProtein();
        }
        return sum;
    }

    public float getCalories() {
        return getTotalCalories() / getTotalMass();
    }

    public float getCaloriesRelative() {
        return getTotalCalories() / getTotalMass() * 100;
    }

    public float getCarbohydrate() {
        return getTotalCarbohydrate() / getTotalMass();
    }

    public float getFat() {
        return getTotalFat() / getTotalMass();
    }

    public float getProtein() {
        return getTotalProtein() / getTotalMass();
    }

    public String getIdentifier() {
        String identifier = "recipe" + id;
        for (Ingredient i : ingredients) {
            identifier += "-" + i.getGlobalReferenceId();
        }
        return identifier;
    }

}
