package com.example.computerheatmonitor;

import com.google.gson.annotations.SerializedName;

public class Summary {

    @SerializedName("mean")
    double mean;
    @SerializedName("variance")
    double var;
    @SerializedName("min")
    double min;
    @SerializedName("max")
    double max;
    @SerializedName("count")
    double count;

    public double getCount() {
        return count;
    }

    public double getMean() {
        return mean;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getVar() {
        return var;
    }
}
