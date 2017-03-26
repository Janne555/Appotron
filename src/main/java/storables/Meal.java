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
public class Meal implements SearchResult {

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

    public float getEnergy() {
        float sum = 0;
        for (MealComponent i : components) {
            if (i.getNutritionalInfo() == null) {
                continue;
            }
            sum += i.getMass() / 100 * i.getNutritionalInfo().getCalories();
        }

        return sum;
    }

    public float getCarbohydrate() {
        float sum = 0;
        for (MealComponent i : components) {
            if (i.getNutritionalInfo() == null) {
                continue;
            }
            sum += i.getMass() / 100 * i.getNutritionalInfo().getCarbohydrate();
        }

        return sum;
    }

    public float getFat() {
        float sum = 0;
        for (MealComponent i : components) {
            if (i.getNutritionalInfo() == null) {
                continue;
            }
            sum += i.getMass() / 100 * i.getNutritionalInfo().getFat();
        }

        return sum;
    }

    public float getProtein() {
        float sum = 0;
        for (MealComponent i : components) {
            if (i.getNutritionalInfo() == null) {
                continue;
            }
            sum += i.getMass() / 100 * i.getNutritionalInfo().getProtein();
        }

        return sum;
    }

    @Override
    public String getName() {
        return "Meal: " + getDate().toLocalDateTime().getDayOfMonth() + "."
                + getDate().toLocalDateTime().getMonthValue() + "."
                + getDate().toLocalDateTime().getYear() + " "
                + getDate().toLocalDateTime().getHour() + ":"
                + getDate().toLocalDateTime().getMinute();
    }

    @Override
    public String getLocation() {
        return "";
    }

    @Override
    public String getType() {
        return "meal";
    }
    
    public String getTime() {
        return this.getDate().toLocalDateTime().toLocalTime().toString();
    }
}
