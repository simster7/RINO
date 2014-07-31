package com.RatView.main;

import java.io.Serializable;
import java.util.ArrayList;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class Rat implements Serializable {

    private int id;
    private int number;
    private boolean dead = false;
    private String box;
    private ArrayList<DailyInfo> preSurgeryDailyInfo = new ArrayList<>();
    private ArrayList<DailyInfo> postDailyInfo = new ArrayList<>();
    private String preSurgeryNotes;
    private String surgeryNotes;
    private String postNotes;
    private ArrayList<DrugInfo> drugs = new ArrayList<>();
    private ArrayList<DrugInfo> postDrugs = new ArrayList<>();
    private ArrayList<SurgeryInfo> surgery = new ArrayList<>();
    private ArrayList<FoodInfo> diet = new ArrayList<>();
    private ArrayList<BBBInfo> bbb = new ArrayList<>();
    private LocalDate dateOfBirth;
    private LocalDate dateOfSurgery;
    private LocalTime timeOfSurgery;

    public Rat(int number, String box, String notes, LocalDate dateOfBirth) {
        this.number = number;
        this.box = box;
        this.preSurgeryNotes = notes;
        this.dateOfBirth = dateOfBirth;

    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public boolean isDead() {
        return dead;
    }

    public String getBox() {
        return box;
    }

    public ArrayList<DailyInfo> getDailyInfo() {
        return preSurgeryDailyInfo;
    }

    public String getPreSurgeryNotesNotes() {
        return preSurgeryNotes;
    }

    public ArrayList<DrugInfo> getDrugs() {
        return drugs;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getDateOfSurgery() {
        return dateOfSurgery;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public void setDailyInfo(ArrayList<DailyInfo> weight) {
        this.preSurgeryDailyInfo = weight;
    }

    public void setPreSurgeryNotesNotes(String notes) {
        this.preSurgeryNotes = notes;
    }

    public void setDrugs(ArrayList<DrugInfo> drugs) {
        this.drugs = drugs;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDateOfSurgery(LocalDate lastSurgery) {
        this.dateOfSurgery = lastSurgery;
    }

    public String toString() {
        return "" + this.number;
    }

    public LocalTime getTimeOfSurgery() {
        return timeOfSurgery;
    }

    public void setTimeOfSurgery(LocalTime timeOfSurgery) {
        this.timeOfSurgery = timeOfSurgery;
    }

    public String getSurgeryNotes() {
        return surgeryNotes;
    }

    public void setSurgeryNotes(String surgeryNotes) {
        this.surgeryNotes = surgeryNotes;
    }

    public ArrayList<SurgeryInfo> getSurgery() {
        return surgery;
    }

    public void setSurgery(ArrayList<SurgeryInfo> surgery) {
        this.surgery = surgery;
    }

    public ArrayList<DailyInfo> getPostDailyInfo() {
        return postDailyInfo;
    }

    public void setPostDailyInfo(ArrayList<DailyInfo> postDailyInfo) {
        this.postDailyInfo = postDailyInfo;
    }

    public ArrayList<DrugInfo> getPostDrugs() {
        return postDrugs;
    }

    public void setPostDrugs(ArrayList<DrugInfo> postDrugs) {
        this.postDrugs = postDrugs;
    }

    public String getPostNotes() {
        return postNotes;
    }

    public void setPostNotes(String postNotes) {
        this.postNotes = postNotes;
    }
    
    public float[] getWeightAsDouble(ArrayList<DailyInfo> a){
        float[] out = new float[a.size()];
        
        int j = 0;
        for(DailyInfo i : a){
            out[j] = i.getWeight();
            j++;
        }
        
        return out;
    }    
    
    public float[] getRamseyAsDouble(ArrayList<DailyInfo> a){
        float[] out = new float[a.size()];
        
        int j = 0;
        for(DailyInfo i : a){
            out[j] = i.getRamsey();
            j++;
        }
        
        return out;
    }

    public ArrayList<FoodInfo> getDiet() {
        return diet;
    }

    public void setDiet(ArrayList<FoodInfo> diet) {
        this.diet = diet;
    }

    public ArrayList<BBBInfo> getBbb() {
        return bbb;
    }

    public void setBbb(ArrayList<BBBInfo> bbb) {
        this.bbb = bbb;
    }
}
