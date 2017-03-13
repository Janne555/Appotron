/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;

/**
 *
 * @author Janne
 */
public class Serving {
    private int id;
    private String userUuid;
    private User user;
    private String identifier;
    private Meal meal;
    private float mass;
    private Timestamp date;

    public Serving(int id, String userUuid, User user, String identifier, Meal meal, float mass, Timestamp date) {
        this.id = id;
        this.userUuid = userUuid;
        this.user = user;
        this.identifier = identifier;
        this.meal = meal;
        this.mass = mass;
        this.date = date;
    }

    public String getName() {
        if (meal != null) {
            return meal.getName();
        }
        return "";
    }

    public String getTime() {
        return date.toString();
    }

    public float getAmount() {
        return mass;
    }

    public float getEnergy() {
        if (meal != null) {
            return mass * meal.getEnergy();
        }
        return 0;
    }

    public float getCarbohydrate() {
        if (meal != null) {
            return mass * meal.getCarbohydrate();
        }
        return 0;
    }

    public float getFat() {
        if (meal != null) {
            return mass * meal.getFat();
        }
        return 0;
    }

    public float getProtein() {
        if (meal != null) {
            return mass * meal.getProtein();
        }
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

}
