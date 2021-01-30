package com.example.computerheatmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import retrofit2.converter.gson.GsonConverterFactory;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class summaryPage extends AppCompatActivity {
    private static final String TAG = "BluetoothControl";
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private LineGraphSeries<DataPoint> mSeries1;
    GraphView graph;
    Button daily, monthly, weekly;
    private UbidotsApi ubidotsApi;
    private String xAuthToken;
    private String id;
    DataPoint[] data;
    List<Temperature> output;
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
        graph = findViewById(R.id.graph);
        minText = findViewById(R.id.min);
        maxText = findViewById(R.id.max);
        countText = findViewById(R.id.count);
        Intent getIntent = getIntent();

        xAuthToken = getIntent.getStringExtra(sensorDataActivity.EXTRA_AUTH);
        id = getIntent.getStringExtra(sensorDataActivity.EXTRA_ID);
        Log.e(TAG,"variable_ID: " + id);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://industrial.api.ubidots.com/api/v1.6/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.ubidotsApi = retrofit.create(UbidotsApi.class);

        getTemperature(id);

        daily.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            Timestamp a = new Timestamp(c.getTimeInMillis());
            long end = a.getTime();
            c.set(Calendar.DATE, c.get(Calendar.DATE)-1);
            Timestamp b = new Timestamp(c.getTimeInMillis());
            long start = b.getTime();
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
            c.set(Calendar.DATE, c.get(Calendar.DATE)-30);
            Timestamp b = new Timestamp(c.getTimeInMillis());
            long start = b.getTime();
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
            c.set(Calendar.DATE, c.get(Calendar.DATE)-7);
            Timestamp b = new Timestamp(c.getTimeInMillis());
            long start = b.getTime();
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
                String output = "" + mean.getMean();
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
                String output = "" + var.getVar();
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
                String output = "" + min.getMin();
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
                String output = "" + max.getMax();
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
                String output = "" + count.getCount();
                countText.setText(output);
            }

            @Override
            public void onFailure(Call<Summary> call, Throwable t) {
                Log.e(TAG, "Web Service Error." + t);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                mSeries1.resetData(getData());
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer1, 300);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        super.onPause();
    }

    public void getTemperature(String id){
        Call<Result> call = ubidotsApi.getTemperature(xAuthToken, id);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, response.message());
                }
                output = response.body().getResults();
                data = getData();
                mSeries1 = new LineGraphSeries<>(data);
                graph.addSeries(mSeries1);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(TAG, "Unsuccessful " + t);
            }
        });

    }

    private DataPoint[] getData() {
        int count = output.size();
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double y = output.get(i).getMeasurement();;
            DataPoint v = new DataPoint(i, y);
            values[i] = v;
        }
        return values;
    }
}