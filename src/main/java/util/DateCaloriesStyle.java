/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.time.LocalDate;

/**
 *
 * @author Janne
 */
public class DateCaloriesStyle {
    LocalDate date;
    float calories;
    String style;

    public DateCaloriesStyle(LocalDate date, float calories, String style) {
        this.date = date;
        this.calories = calories;
        this.style = style;
    }

    public String getDate() {
        return date.getDayOfMonth() + "." + date.getMonthValue();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
    
}
