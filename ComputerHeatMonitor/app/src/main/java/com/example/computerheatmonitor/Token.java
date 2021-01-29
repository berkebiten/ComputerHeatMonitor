package com.example.computerheatmonitor;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("token")
    String token;

    public String getToken() {
        return token;
    }
}
