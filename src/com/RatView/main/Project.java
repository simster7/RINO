package com.RatView.main;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Simon
 */
public class Project implements Serializable {

    private String name;
    private ArrayList<Rat> rats = new ArrayList<>();
    private String strain;
    private String researcher;
    private String gender;
    private int room;
    private int box;
    private String coordinates;
    private int number;
    private boolean operative;

    public Project(String name, String strain, int room, int box, String coordinates, int number, String researcher, String gender, boolean operative) {
        this.name = name;
        this.strain = strain;
        this.room = room;
        this.box = box;
        this.coordinates = coordinates;
        this.number = number;
        this.researcher = researcher;
        this.gender = gender;
        this.operative = operative;

    }

    public String getName() {
        return name;
    }

    public ArrayList<Rat> getRats() {
        return rats;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRats(ArrayList<Rat> rats) {
        this.rats = rats;
    }

    public String getStrain() {
        return strain;
    }

    public int getRoom() {
        return room;
    }

    public int getBox() {
        return box;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String toString() {
        return "" + name + "," + strain + "," + room + "," + box + "," + coordinates + "," + number;
    }

    public String getResearcher() {
        return researcher;
    }

    public void setResearcher(String researcher) {
        this.researcher = researcher;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isOperative() {
        return operative;
    }

    public void setOperative(boolean operative) {
        this.operative = operative;
    }
}
