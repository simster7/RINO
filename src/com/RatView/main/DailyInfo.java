package com.RatView.main;

import java.io.Serializable;
import java.util.Date;
import org.joda.time.LocalDate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Simon
 */
public class DailyInfo implements Serializable{

    private LocalDate date;
    private float weight;
    private int ramsey;
    
    public DailyInfo(LocalDate date, float weight, int ramsey){
        this.date = date;
        this.weight = weight;
        this.ramsey = ramsey;
        
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getRamsey() {
        return ramsey;
    }

    public void setRamsey(int ramsey) {
        this.ramsey = ramsey;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
