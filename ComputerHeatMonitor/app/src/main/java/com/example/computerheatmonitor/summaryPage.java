package com.example.computerheatmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Timestamp;
import java.util.Calendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class summaryPage extends AppCompatActivity {
    private static final String TAG = "BluetoothControl";
    Button daily, monthly, weekly;
    private UbidotsApi ubidotsApi;
    private String xAuthToken;
    private String id;
    TextView meanText, varianceText, minText, maxText, countText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_page);

        daily = findViewById(R.id.daily);
        monthly = findViewById(R.id.monthly);
        weekly = findViewById(R.id.weekly);
        meanText = findViewById(R.id.mean);
        varianceText = findViewById(R.id.variance);
        minText = findViewById(R.id.min);
        maxText = findViewById(R.id.max);
        countText = findViewById(R.id.count);
        Intent getIntent = getIntent();

        xAuthToken = getIntent.getStringExtra(sensorDataActivity.EXTRA_AUTH);
        id = getIntent.getStringExtra(sensorDataActivity.EXTRA_ID);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://industrial.api.ubidots.com/api/v1.6/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.ubidotsApi = retrofit.create(UbidotsApi.class);

        daily.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            Timestamp a = new Timestamp(c.getTimeInMillis());
            long end = a.getTime();
            Log.e(TAG, "Web Service Error. " + end);
            c.set(Calendar.DATE, c.get(Calendar.DATE)-1);
            Timestamp b = new Timestamp(c.getTimeInMillis());
            long start = b.getTime();
            Log.e(TAG, "Web Service Error. " + start);
            getMean(start, end);
            getVariance(start, end);
            getMin(start, end);
            getMax(start, end);
            getCount(start, end);
        });

        monthly.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            Timestamp a = new Timestamp(c.getTimeInMillis());
            long end = a.getTime();
            Log.e(TAG, "Web Service Error. " + end);
            c.set(Calendar.DATE, c.get(Calendar.DATE)-30);
            Timestamp b = new Timestamp(c.getTimeInMillis());
            long start = b.getTime();
            Log.e(TAG, "Web Service Error. " + start);
            getMean(start, end);
            getVariance(start, end);
            getMin(start, end);
            getMax(start, end);
            getCount(start, end);
        });

        weekly.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            Timestamp a = new Timestamp(c.getTimeInMillis());
            long end = a.getTime();
            Log.e(TAG, "Web Service Error. " + end);
            c.set(Calendar.DATE, c.get(Calendar.DATE)-7);
            Timestamp b = new Timestamp(c.getTimeInMillis());
            long start = b.getTime();
            Log.e(TAG, "Web Service Error. " + start);
            getMean(start, end);
            getVariance(start, end);
            getMin(start, end);
            getMax(start, end);
            getCount(start, end);
        });
    }

    public void getMean(long start, long end) {
        Call<Summary> call = ubidotsApi.getSummary(xAuthToken, id, "mean", start, end);

        call.enqueue(new Callback<Summary>() {
            @Override
            public void onResponse(Call<Summary> call, Response<Summary> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Unsuccessfull" + response.message());
                }
                Summary mean = response.body();
                String output = "Mean:  " + mean.getMean() + "";
                meanText.setText(output);
            }

            @Override
            public void onFailure(Call<Summary> call, Throwable t) {
                Log.e(TAG, "Web Service Error." + t);
            }
        });

    }

    public void getVariance(long start, long end) {
        Call<Summary> call = ubidotsApi.getSummary(xAuthToken, id, "variance", start, end);

        call.enqueue(new Callback<Summary>() {
            @Override
            public void onResponse(Call<Summary> call, Response<Summary> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Unsuccessfull" + response.message());
                }
                Summary var = response.body();
                String output = "Variance: " + var.getVar();
                varianceText.setText(output);
            }

            @Override
            public void onFailure(Call<Summary> call, Throwable t) {
                Log.e(TAG, "Web Service Error." + t);
            }
        });

    }

    public void getMin(long start, long end) {
        Call<Summary> call = ubidotsApi.getSummary(xAuthToken, id, "min", start, end);

        call.enqueue(new Callback<Summary>() {
            @Override
            public void onResponse(Call<Summary> call, Response<Summary> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Unsuccessfull" + response.message());
                }
                Summary min = response.body();
                String output = "Min Temperature: " + min.getMin();
                minText.setText(output);
            }

            @Override
            public void onFailure(Call<Summary> call, Throwable t) {
                Log.e(TAG, "Web Service Error." + t);
            }
        });

    }
    public void getMax(long start, long end) {
        Call<Summary> call = ubidotsApi.getSummary(xAuthToken, id, "max", start, end);

        call.enqueue(new Callback<Summary>() {
            @Override
            public void onResponse(Call<Summary> call, Response<Summary> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Unsuccessfull" + response.message());
                }
                Summary max = response.body();
                String output = "Max Temperature: " + max.getMax();
                maxText.setText(output);
            }

            @Override
            public void onFailure(Call<Summary> call, Throwable t) {
                Log.e(TAG, "Web Service Error." + t);
            }
        });

    }
    public void getCount(long start, long end) {
        Call<Summary> call = ubidotsApi.getSummary(xAuthToken, id, "count", start, end);

        call.enqueue(new Callback<Summary>() {
            @Override
            public void onResponse(Call<Summary> call, Response<Summary> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Unsuccessfull" + response.message());
                }
                Summary count = response.body();
                String output = "Count: " + count.getCount();
                countText.setText(output);
            }

            @Override
            public void onFailure(Call<Summary> call, Throwable t) {
                Log.e(TAG, "Web Service Error." + t);
            }
        });

    }


}