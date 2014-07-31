package com.RatView.main;

import java.io.Serializable;
import org.joda.time.LocalDate;

public class FoodInfo implements Serializable{
    
    private LocalDate date;
    private String food;
    private float given;
    private float eaten;

    public FoodInfo(LocalDate date, String food, float given, float eaten) {
        this.date = date;
        this.food = food;
        this.given = given;
        this.eaten = eaten;
    }

    public String getFood() {
        return food;
    }

    public float getGiven() {
        return given;
    }

    public float getEaten() {
        return eaten;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public void setGiven(float given) {
        this.given = given;
    }

    public void setEaten(float eaten) {
        this.eaten = eaten;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    


}
