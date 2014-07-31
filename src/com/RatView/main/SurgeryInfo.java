/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.RatView.main;

import java.io.Serializable;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author Simon
 */
class SurgeryInfo implements Serializable{
    
    private LocalDate date;
    private LocalTime time;
    private float weight;
    private int surgeryNumber;
    private LocalTime aTime;
    private boolean bladderEmpty;
    private boolean eyes;
    private int enro;
    private int bupre;
    private int keto;
    private boolean shaved;
    private boolean clean;
    private LocalTime incision;
    private LocalTime injury;
    private LocalTime close;
    private int externalPoints;
    private LocalTime rehavilitation;
    private LocalTime awareness;
    private int ramsey;
    private String notes;

    /**
     * 
     * @param date
     * @param time
     * @param weight
     * @param surgeryNumber
     * @param aTime
     * @param bladderEmpty
     * @param eyes
     * @param enro
     * @param bupre
     * @param keto
     * @param shaved
     * @param clean
     * @param incision
     * @param injury
     * @param close
     * @param externalPoints
     * @param rehavilitation
     * @param awareness
     * @param ramsey
     * @param notes 
     */
    public SurgeryInfo(LocalDate date, LocalTime time, float weight, int surgeryNumber, LocalTime aTime, boolean bladderEmpty, boolean eyes, int enro, int bupre, int keto, boolean shaved, boolean clean, LocalTime incision, LocalTime injury, LocalTime close, int externalPoints, LocalTime rehavilitation, LocalTime awareness, int ramsey, String notes) {
        this.date = date;
        this.time = time;
        this.weight = weight;
        this.surgeryNumber = surgeryNumber;
        this.aTime = aTime;
        this.bladderEmpty = bladderEmpty;
        this.eyes = eyes;
        this.enro = enro;
        this.bupre = bupre;
        this.keto = keto;
        this.shaved = shaved;
        this.clean = clean;
        this.incision = incision;
        this.injury = injury;
        this.close = close;
        this.externalPoints = externalPoints;
        this.rehavilitation = rehavilitation;
        this.awareness = awareness;
        this.ramsey = ramsey;
        this.notes = notes;
    }
    

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public float getWeight() {
        return weight;
    }

    public int getSurgeryNumber() {
        return surgeryNumber;
    }

    public LocalTime getaTime() {
        return aTime;
    }

    public boolean isBladderEmpty() {
        return bladderEmpty;
    }

    public boolean isEyes() {
        return eyes;
    }

    public int getEnro() {
        return enro;
    }

    public int getBupre() {
        return bupre;
    }

    public int getKeto() {
        return keto;
    }

    public boolean isShaved() {
        return shaved;
    }

    public boolean isClean() {
        return clean;
    }

    public LocalTime getIncision() {
        return incision;
    }

    public LocalTime getInjury() {
        return injury;
    }

    public LocalTime getClose() {
        return close;
    }

    public int getExternalPoints() {
        return externalPoints;
    }

    public LocalTime getRehavilitation() {
        return rehavilitation;
    }

    public LocalTime getAwareness() {
        return awareness;
    }

    public int getRamsey() {
        return ramsey;
    }

    public String getNotes() {
        return notes;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setSurgeryNumber(int surgeryNumber) {
        this.surgeryNumber = surgeryNumber;
    }

    public void setaTime(LocalTime aTime) {
        this.aTime = aTime;
    }

    public void setBladderEmpty(boolean bladderEmpty) {
        this.bladderEmpty = bladderEmpty;
    }

    public void setEyes(boolean eyes) {
        this.eyes = eyes;
    }

    public void setEnro(int enro) {
        this.enro = enro;
    }

    public void setBupre(int bupre) {
        this.bupre = bupre;
    }

    public void setKeto(int keto) {
        this.keto = keto;
    }

    public void setShaved(boolean shaved) {
        this.shaved = shaved;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public void setIncision(LocalTime incision) {
        this.incision = incision;
    }

    public void setInjury(LocalTime injury) {
        this.injury = injury;
    }

    public void setClose(LocalTime close) {
        this.close = close;
    }

    public void setExternalPoints(int externalPoints) {
        this.externalPoints = externalPoints;
    }

    public void setRehavilitation(LocalTime rehavilitation) {
        this.rehavilitation = rehavilitation;
    }

    public void setAwareness(LocalTime awareness) {
        this.awareness = awareness;
    }

    public void setRamsey(int ramsey) {
        this.ramsey = ramsey;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
}
