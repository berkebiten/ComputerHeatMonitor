package com.example.computerheatmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class sensorDataActivity extends AppCompatActivity{

    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "BluetoothControl";
    private boolean isBluetoothConnected = false;
    Button btnClear;
    TextView tvReceivedData;
    TextView results;
    String address = null;
    private ProgressDialog progressDialog;
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothSocket mBluetoothSocket = null;
    private UbidotsApi ubidotsApi;
    private String xAuthToken;
    InputStream mInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data);
        Intent getAddressIntent = getIntent();
        address = getAddressIntent.getStringExtra(MainActivity.EXTRA_ADDRESS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://industrial.api.ubidots.com/api/v1.6/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.ubidotsApi = retrofit.create(UbidotsApi.class);
        getAuth();
        results = findViewById(R.id.results);
        tvReceivedData = findViewById(R.id.tvReceivedData);
        btnClear = findViewById(R.id.btnClear);

        new BTConnectAsync().execute();

        btnClear.setOnClickListener(view -> tvReceivedData.setText(" "));

    }

    void beginListenForData() {
        final Handler handler = new Handler(Looper.getMainLooper());
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(() -> {
            while(!Thread.currentThread().isInterrupted() && !stopWorker) {
                try {
                    int bytesAvailable = mInputStream.available();
                    if(bytesAvailable > 0) {
                        byte[] packetBytes = new byte[bytesAvailable];
                        mInputStream.read(packetBytes);
                        for(int i=0;i<bytesAvailable;i++) {
                            byte b = packetBytes[i];
                            if(b == delimiter) {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes, "US-ASCII");
                                readBufferPosition = 0;

                                handler.post(() -> {
                                    tvReceivedData.setText(data);
                                    Temperature temp = new Temperature(data);
                                    try {
                                        insertTemperature(temp);
                                    } catch (Exception e){
                                        System.out.println("Exception e");
                                    }

                                    getTemperature("60140ae91d847245c46f2886");
                                });
                            }
                            else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                }
                catch (IOException ex) {
                    stopWorker = true;
                }
            }
        });

        workerThread.start();
    }

    public void getTemperature(String id){
        Call<Result> call = ubidotsApi.getTemperature(xAuthToken, id);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Unsuccessfull" + response.message());
                }
                List<Temperature> temperatures = response.body().getResults();
                String output = "";
                for (Temperature temp: temperatures){
                    output += temp.getMeasurement().toString() + "\n";
                }
                results.setText(output);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(TAG, "Unsuccessful " + t);
            }
        });

    }

    public void getAuth(){
        Call<Token> call = ubidotsApi.getAuth("BBFF-6d0d857720f1cc053925242508684c9b6b0");

        call.enqueue(new Callback<Token>(){

            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Unsuccessfull" + response.message());
                }
                Token token = response.body();
                xAuthToken = token.getToken();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e(TAG, "Web Service Error." + t);
            }
        });
    }

    public void insertTemperature(Temperature temp) {
        Call<Temperature> call = ubidotsApi.insertTemperature(xAuthToken, temp);

        call.enqueue(new Callback<Temperature>() {
            @Override
            public void onResponse(Call<Temperature> call, Response<Temperature> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "Unsuccessfull" + response.message());
                }
            }

            @Override
            public void onFailure(Call<Temperature> call, Throwable t) {
                Log.e(TAG, "Web Service Error." + t);
            }
        });

    }

    private void Disconnect(){
        if (mBluetoothSocket != null){
            try {
                stopWorker = true;
                mBluetoothSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Disconnect();
    }

    @SuppressLint("StaticFieldLeak")
    private class BTConnectAsync extends AsyncTask<Void,Void,Void> {
        private boolean ConnectSuccess = true;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(sensorDataActivity.this, "Bağlanıyor..", "Lütfen bekleyin");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (mBluetoothSocket == null || !isBluetoothConnected) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBluetoothSocket.connect();
                    mInputStream = mBluetoothSocket.getInputStream();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!ConnectSuccess) {
                Toast.makeText(getApplicationContext(), "Bağlantı Hatası Tekrar Deneyin", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Bağlantı Başarılı", Toast.LENGTH_SHORT).show();
                isBluetoothConnected = true;
                beginListenForData();
            }
            progressDialog.dismiss();
        }

    }
}
