/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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

    public int getTotalCaloriesRounded() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass() * i.getFoodstuff().getCalories();
        }
        return Math.round(sum);
    }

    public int getTotalCarbohydrateRounded() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass() * i.getFoodstuff().getCarbohydrate();
        }
        return Math.round(sum);
    }

    public int getTotalFatRounded() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass() * i.getFoodstuff().getFat();
        }
        return Math.round(sum);
    }

    public int getTotalProteinRounded() {
        float sum = 0;
        for (MealComponent i : components) {
            sum += i.getMass() * i.getFoodstuff().getProtein();
        }
        return Math.round(sum);
    }

    public String getName() {
        String string = "Meal: " + getDate().toLocalDateTime().getDayOfMonth() + "."
                + getDate().toLocalDateTime().getMonthValue() + "."
                + getDate().toLocalDateTime().getYear() + " "
                + getDate().toLocalDateTime().getHour() + ":";
        int minute = getDate().toLocalDateTime().getMinute();
        if (minute < 10) {
            string += "0" + minute;
        } else {
            string += minute;
        }
        return string;
    }

    public String getLocation() {
        return "";
    }

    public String getType() {
        return "meal";
    }

    public LocalDateTime getTime() {
        return date.toLocalDateTime().truncatedTo(ChronoUnit.MINUTES);
    }
    
    public float getMass() {
        float mass = 0;
        for (MealComponent c : getComponents()) {
            mass += c.getMass();
        }
        return mass;
    }
}
