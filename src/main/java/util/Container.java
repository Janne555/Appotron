/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.time.LocalDate;
import storables.Meal;

/**
 *
 * @author Janne
 */
public class Container {
    Integer ordinal;
    LocalDate date;
    float mass;
    float carbohydrate;
    float fat;
    float protein;
    float calories;

    public Container(Integer ordinal, LocalDate date) {
        this.ordinal = ordinal;
        this.date = date;
        this.mass = 0;
        this.carbohydrate = 0;
        this.fat = 0;
        this.protein = 0;
        this.calories = 0;
    }

    public Container(LocalDate date, float carbohydrate, float fat, float protein, float calories) {
        this.date = date;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
        this.calories = calories;
    }


    
    public Container add(Meal meal) {
        setMass(getMass() + meal.getMass());
        setCalories(getCalories()+ meal.getTotalCalories());
        setFat(getFat()+ meal.getTotalFat());
        setCarbohydrate(getCarbohydrate()+ meal.getTotalCarbohydrate());
        setProtein(getProtein() + meal.getTotalProtein());
        return this;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
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

    public float getCalories() {
        return calories;
    }

    public Container setCalories(float calories) {
        this.calories = calories;
        return this;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getDate() {
        return date.getDayOfMonth() + "." + date.getMonthValue();
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void round() {
        setMass(Math.round(getMass()));
        setCalories(Math.round(getCalories()));
        setCarbohydrate(Math.round(getCarbohydrate()));
        setFat(Math.round(getMass()));
        setProtein(Math.round(getProtein()));
    }

}
