package com.RatView.main;


public class Statistics {
    
    static float getMean(float[] data) {
        
        float size = data.length;
        float sum = 0.0f;
        for (float a : data) {
            sum += a;
        }
        return sum / size;
    }

    static float getVariance(float[] data) {
        
        float size = data.length;
        float mean = getMean(data);
        float temp = 0;
        for (double a : data) {
            temp += (mean - a) * (mean - a);
        }
        return temp / size;
    }

    static float getStdDev(float[] data) {
        return (float) Math.sqrt(getVariance(data));
    }
}
