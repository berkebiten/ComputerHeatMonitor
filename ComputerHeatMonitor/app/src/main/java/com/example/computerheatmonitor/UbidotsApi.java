package com.example.computerheatmonitor;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UbidotsApi {

    @GET("variables/{id}/values?page=1&page_size=5")
    Call<Result> getTemperature(@Header("X-Auth-Token") String token, @Path("id") String id);

    @POST("auth/token")
    Call<Token> getAuth(@Header("x-ubidots-apikey") String apiKey);

    @POST("devices/android-phone/temperature/values/")
    Call<Temperature> insertTemperature(@Header("X-Auth-Token") String token, @Body Temperature temperature);

    @POST("devices/{device_label}/")
    Call<device> addDevice(@Header("X-Auth-Token") String token, @Path("device_label") String device_label, @Body String variable_label);

    
}
