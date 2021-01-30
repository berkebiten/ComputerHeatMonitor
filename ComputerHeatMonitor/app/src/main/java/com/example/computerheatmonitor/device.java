package com.example.computerheatmonitor;

import com.google.gson.annotations.SerializedName;

public class device {

    @SerializedName("device_label")
    String deviceLabel;


    public device(String label){
        this.deviceLabel = label;
    }

    public String getDeviceLabel(){
        return this.deviceLabel;
    }
}
