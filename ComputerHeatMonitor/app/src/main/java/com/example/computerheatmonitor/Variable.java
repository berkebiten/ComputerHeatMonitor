package com.example.computerheatmonitor;

import com.google.gson.annotations.SerializedName;

public class Variable {

    @SerializedName("temperature")
    int deviceLabel;


    public Variable(int label){
        this.deviceLabel = label;
    }

    public int getDeviceLabel(){
        return this.deviceLabel;
    }
}
