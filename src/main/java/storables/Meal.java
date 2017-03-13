/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.Incrementer;

/**
 *
 * @author Janne
 */
public class Meal {

    private String id;
    private String name;
    private String type;
    private List<Ingredient> ingredients;

    public Meal(String id, String name, String type, Object... ingredients) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ingredients = new ArrayList<>();
        for (Object o : ingredients) {
            this.ingredients.add((Ingredient) o);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public float getEnergy() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getPercentage() * i.getNutritionalInfo().getEnergy();
        }

        return sum / 100;
    }

    public float getCarbohydrate() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getPercentage() * i.getNutritionalInfo().getCarbohydrates();
        }

        return sum / 100;
    }

    public float getFat() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getPercentage() * i.getNutritionalInfo().getFat();
        }

        return sum / 100;
    }

    public float getProtein() {
        float sum = 0;
        for (Ingredient i : ingredients) {
            sum += i.getPercentage() * i.getNutritionalInfo().getProtein();
        }

        return sum / 100;
    }

    public Object[] getObjs() {
        Object[] objs = new Object[4];
        Incrementer inc = new Incrementer();
        objs[inc.next()] = id;
        objs[inc.next()] = name;
        objs[inc.next()] = type;
        objs[inc.next()] = false;
        return objs;
    }

}
