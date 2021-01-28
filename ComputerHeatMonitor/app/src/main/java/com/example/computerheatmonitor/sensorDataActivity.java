package com.example.computerheatmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class sensorDataActivity extends AppCompatActivity {

    private ProgressDialog progress;
    boolean stopThread;
    private InputStream inputStream;
    private OutputStream outputStream;
    byte buffer[];
    TextView textView;
    BluetoothSocket btSocket ;
    BluetoothDevice remoteDevice;
    BluetoothAdapter bt = null;
    BluetoothServerSocket mmServer;
    String address = null;

    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data);
        Intent newInt = getIntent();
        address = newInt.getStringExtra(MainActivity.EXTRA_ADDRESS);
        new btConnect().execute();
    }

    private void Disconnect(){
        if(btSocket!=null){
            try {
                btSocket.close();
            } catch (IOException e){
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Disconnect();
    }

    private class btConnect extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(sensorDataActivity.this, "Connecting...", "Please Wait.");
        }


        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    bt = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = bt.getRemoteDevice(address);
                    btSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                Toast.makeText(getApplicationContext(),"Connection error. Please try again.",Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"Connected Successfully.",Toast.LENGTH_SHORT).show();
                try {
                    outputStream = btSocket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    inputStream = btSocket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isBtConnected = true;
            }
            progress.dismiss();
        }

    }

}
