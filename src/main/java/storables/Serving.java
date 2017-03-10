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
    private Item item;
    private NutritionalInfo nutritionalInfo;
    private float mass;
    private Timestamp date;

    public Serving(int id, String userUuid, User user, String identifier, Item item, NutritionalInfo nutritionalInfo, float mass, Timestamp date) {
        this.id = id;
        this.userUuid = userUuid;
        this.user = user;
        this.identifier = identifier;
        this.item = item;
        this.nutritionalInfo = nutritionalInfo;
        this.mass = mass;
        this.date = date;
    }

    public String getName() {
        if (item != null) {
            return item.getName();
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
        if (nutritionalInfo != null) {
            return mass * nutritionalInfo.getEnergy();
        }
        return 0;
    }

    public float getCarbohydrate() {
        if (nutritionalInfo != null) {
            return mass * nutritionalInfo.getCarbohydrates();
        }
        return 0;
    }

    public float getFat() {
        if (nutritionalInfo != null) {
            return mass * nutritionalInfo.getFat();
        }
        return 0;
    }

    public float getProtein() {
        if (nutritionalInfo != null) {
            return mass * nutritionalInfo.getProtein();
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public NutritionalInfo getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(NutritionalInfo nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
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
