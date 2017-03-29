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

    public float getCalories() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getTotalCalories();
        }
        return sum / totalMass;
    }

    public float getCarbohydrate() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getTotalCarbohydrate();
        }
        return sum / totalMass;
    }

    public float getFat() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getTotalFat();
        }
        return sum / totalMass;
    }

    public float getProtein() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getTotalProtein();
        }
        return sum;
    }
    
        public float getTotalCalories() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getTotalCalories();
        }
        return sum;
    }

    public float getTotalCarbohydrate() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getTotalCarbohydrate();
        }
        return sum;
    }

    public float geTotaltFat() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getTotalFat();
        }
        return sum;
    }

    public float getTotalProtein() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getTotalProtein();
        }
        return sum;
    }

}
