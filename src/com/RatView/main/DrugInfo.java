/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.RatView.main;

import java.io.Serializable;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author Simon
 */
public class DrugInfo implements Serializable{

    private String drugName;
    private String dosis;
    private LocalDate date;
    private LocalTime time;

    public DrugInfo(LocalDate dateTime, LocalTime time, String drugName, String dosis) {
        this.drugName = drugName;
        this.dosis = dosis;
        this.date = dateTime;
        this.time = time;

    }

    public String getDrugName() {
        return drugName;
    }

    public String getDosis() {
        return dosis;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
    

}
