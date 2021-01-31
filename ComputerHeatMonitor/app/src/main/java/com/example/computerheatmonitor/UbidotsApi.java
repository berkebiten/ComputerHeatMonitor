package com.example.computerheatmonitor;

import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UbidotsApi {

    @GET("variables/{id}/values?page=1&page_size=5")
    Call<Result> getTemperature(@Header("X-Auth-Token") String token, @Path("id") String id);

    @GET("variables/{id}/values?page=1")
    Call<Result> getTemperatures(@Header("X-Auth-Token") String token, @Path("id") String id, @Query("page_size") int size);

    @POST("devices/{device_label}/temperature/values/")
    Call<Temperature> insertTemperature(@Header("X-Auth-Token") String token,@Path("device_label") String device_label, @Body Temperature temperature);

    @POST("devices/{device_label}/")
    Call<Variable> addDevice(@Header("X-Auth-Token") String token, @Path("device_label") String device_label, @Body Variable variable);

    @GET("devices/{device_label}/temperature/")
    Call<Result> getVariableId(@Header("X-Auth-Token") String token, @Path("device_label") String device_label);

    @GET("variables/{id}/statistics/{aggregation}/{start}/{end}/")
    Call<Summary> getSummary(@Header("X-Auth-Token") String token, @Path("id") String id, @Path("aggregation") String aggregation, @Path("start") long start, @Path("end") long end);
}
