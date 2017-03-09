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
    private int mass;
    private Timestamp date;

    public Serving(int id, String userUuid, User user, String identifier, Item item, NutritionalInfo nutritionalInfo, int mass, Timestamp date) {
        this.id = id;
        this.userUuid = userUuid;
        this.user = user;
        this.identifier = identifier;
        this.item = item;
        this.nutritionalInfo = nutritionalInfo;
        this.mass = mass;
        this.date = date;
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

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

}
