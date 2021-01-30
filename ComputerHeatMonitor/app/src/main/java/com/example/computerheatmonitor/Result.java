package com.example.computerheatmonitor;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Result {

    @SerializedName("results")
    List<Temperature> results;
    @SerializedName("id")
    String id;

    public String getId(){return id;}

    public List<Temperature> getResults() {
        return results;
    }
}
