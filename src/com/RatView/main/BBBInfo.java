/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.RatView.main;

import java.io.Serializable;
import org.joda.time.LocalDate;

/**
 *
 * @author Simon
 */
public class BBBInfo implements Serializable {
    
    private LocalDate date;
    private int BBB;

    public BBBInfo(LocalDate date, int BBB) {
        this.date = date;
        this.BBB = BBB;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getBBB() {
        return BBB;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setBBB(int BBB) {
        this.BBB = BBB;
    }
    
}
