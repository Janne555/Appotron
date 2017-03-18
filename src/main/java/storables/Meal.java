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
public class Meal {
    private String id;
    private User user;
    private Timestamp date;
    private List<MealComponent> components;

    public Meal(String id, User user, Timestamp date, List<MealComponent> components) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.components = components;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public List<MealComponent> getComponents() {
        return components;
    }

    public void setComponents(List<MealComponent> components) {
        this.components = components;
    }

    public float getEnergy() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass() * i.getNutritionalInfo().getEnergy();
        }

        return sum;
    }

    public float getCarbohydrate() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass()* i.getNutritionalInfo().getCarbohydrates();
        }

        return sum;
    }

    public float getFat() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass()* i.getNutritionalInfo().getFat();
        }

        return sum;
    }

    public float getProtein() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass()* i.getNutritionalInfo().getProtein();
        }

        return sum;
    }
}
