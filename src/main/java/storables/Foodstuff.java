/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author janne
 */
public class Foodstuff {

    private String name;
    private String identifier;
    private String producer;
    private String location;
    private float calories;
    private float carbohydrate;
    private float fat;
    private float protein;
    private int globalReferenceId;
    private int foodstuffMetaId;
    private int id;
    private Timestamp expiration;
    private Timestamp date;

    public Foodstuff(String name, String identifier, String producer, String location, float calories, float carbohydrate, float fat, float protein, int globalReference, int foodstuffMeta, int id, Timestamp expiration, Timestamp date) {
        this.name = name;
        this.identifier = identifier;
        this.producer = producer;
        this.location = location;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
        this.globalReferenceId = globalReference;
        this.foodstuffMetaId = foodstuffMeta;
        this.id = id;
        this.expiration = expiration;
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(float carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public int getGlobalReferenceId() {
        return globalReferenceId;
    }

    public void setGlobalReferenceId(int globalReference) {
        this.globalReferenceId = globalReference;
    }

    public int getFoodstuffMetaId() {
        return foodstuffMetaId;
    }

    public void setFoodstuffMetaId(int foodstuffMeta) {
        this.foodstuffMetaId = foodstuffMeta;
    }

    public Object getExpirationString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getExpires() {
        if (expiration == null) {
            return "unknown";
        }

        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59);
        LocalDateTime tomorrow = endOfDay.plusDays(1);

        if (expiration.toLocalDateTime().isBefore(endOfDay)) {
            return "Today";
        } else if (expiration.toLocalDateTime().isAfter(endOfDay) && expiration.toLocalDateTime().isBefore(tomorrow)) {
            return "Tomorrow";
        } else if (expiration.toLocalDateTime().isBefore(LocalDateTime.now())) {
            return "EXPIRED";
        } else {
            long until = LocalDateTime.now().until(expiration.toLocalDateTime(), ChronoUnit.DAYS);
            return "In " + until + " days";
        }
    }

    @Override
    public String toString() {
        return "NAME: " + getName() + ", "
                + "IDENTIFIER: " + getIdentifier() + ", "
                + "PRODUCER: " + getProducer() + ", "
                + "CREATED: " + getDate() + ", "
                + "EXPIRES: " + getExpiration() + ", "
                + "CALORIES: " + getCalories() + ", "
                + "CARBOHYDRATE: " + getCarbohydrate() + ", "
                + "FAT: " + getFat() + ", "
                + "PROTEIN: " + getProtein();
    }

    public float getRelativeCalories() {
        return this.getCalories() * 100;
    }

    public float getRelativeCarbohydrate() {
        return this.getCarbohydrate() * 100;
    }

    public float getRelativeFat() {
        return this.getFat() * 100;
    }

    public float getRelativeProtein() {
        return this.getProtein() * 100;
    }
}
