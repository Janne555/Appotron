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
    private int id;
    private User user;
    private Timestamp date;
    private List<MealComponent> components;

    public Meal(int id, User user, Timestamp date, List<MealComponent> components) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.components = components;
    }

    public String getUserId() {
        return this.getUser().getId();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public float getTotalCalories() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass() * i.getFoodstuff().getCalories();
        }

        return sum;
    }

    public float getTotalCarbohydrate() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass() * i.getFoodstuff().getCarbohydrate();
        }

        return sum;
    }

    public float getTotalFat() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass() * i.getFoodstuff().getFat();
        }

        return sum;
    }

    public float getTotalProtein() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass() * i.getFoodstuff().getProtein();
        }

        return sum;
    }

    public String getName() {
        return "Meal: " + getDate().toLocalDateTime().getDayOfMonth() + "."
                + getDate().toLocalDateTime().getMonthValue() + "."
                + getDate().toLocalDateTime().getYear() + " "
                + getDate().toLocalDateTime().getHour() + ":"
                + getDate().toLocalDateTime().getMinute();
    }

    public String getLocation() {
        return "";
    }

    public String getType() {
        return "meal";
    }
    
    public String getTime() {
        return this.getDate().toLocalDateTime().toLocalTime().toString();
    }
}
