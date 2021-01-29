package com.example.computerheatmonitor;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import org.json.JSONObject;

public class Temperature {

    @SerializedName("value")
    double measurement;

    Temperature(String measurement) {
        this.measurement = Double.parseDouble(measurement);
    }

    public Double getMeasurement(){
        return measurement;
    }


}
