/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.RatView.main;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Simon
 */
public class SaveFile implements Serializable{
    
    private ArrayList<Project> projects = new ArrayList<>();
    private ArrayList<EnvInfo> data = new ArrayList<>();

    public SaveFile(ArrayList<Project> projects, ArrayList<EnvInfo> data) {
        this.projects = projects;
        this.data = data;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public ArrayList<EnvInfo> getData() {
        return data;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public void setData(ArrayList<EnvInfo> data) {
        this.data = data;
    }
    
    
    
    
}
