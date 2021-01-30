package com.example.computerheatmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import java.sql.Timestamp;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class summaryPage extends AppCompatActivity {
    Button daily, monthly, weekly;
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
        daily = findViewById(R.id.daily);
        monthly = findViewById(R.id.monthly);
        weekly = findViewById(R.id.weekly);
        Intent getIntent = getIntent();
        xAuthToken = getIntent.getStringExtra(sensorDataActivity.EXTRA_AUTH);
        id = getIntent.getStringExtra(sensorDataActivity.EXTRA_ID);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://industrial.api.ubidots.com/api/v1.6/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        daily.setOnClickListener(v -> {
            Timestamp start = new Timestamp(System.currentTimeMillis());
            Timestamp end = start.getMonth() - 1;

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -5);
            long start = cal.getTimeInMillis();
            this.ubidotsApi = retrofit.create(UbidotsApi.class);
            Call<Summary> summary = ubidotsApi.getSummary(xAuthToken, id, "mean", start.getTime(), end);
            this.mean =
        });



    }


}