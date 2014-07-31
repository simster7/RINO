package com.RatView.main;


import java.io.Serializable;
import org.joda.time.LocalDate;

 public class EnvInfo implements Serializable {

        LocalDate date;
        float temp;
        float hum;

        public EnvInfo(LocalDate date, float temp, float hum) {
            this.date = date;
            this.temp = temp;
            this.hum = hum;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public float getTemp() {
            return temp;
        }

        public void setTemp(float temp) {
            this.temp = temp;
        }

        public float getHum() {
            return hum;
        }

        public void setHum(float hum) {
            this.hum = hum;
        }

    }