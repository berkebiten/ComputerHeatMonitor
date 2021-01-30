package com.example.computerheatmonitor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Timestamp;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class summaryPage extends AppCompatActivity {
    private UbidotsApi ubidotsApi;
    private String xAuthToken;
    private String id;
    double mean;
    double variance;
    double min;
    double max;
    double count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_page);
        Intent getIntent = getIntent();
        xAuthToken = getIntent.getStringExtra(sensorDataActivity.EXTRA_AUTH);
        id = getIntent.getStringExtra(sensorDataActivity.EXTRA_ID);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://industrial.api.ubidots.com/api/v1.6/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Timestamp start = new Timestamp(System.currentTimeMillis());
        this.ubidotsApi = retrofit.create(UbidotsApi.class);
        Call<Summary> summary = ubidotsApi.getSummary();
        this.mean =
    }


}
