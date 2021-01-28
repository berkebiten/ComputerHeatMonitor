package com.example.computerheatmonitor;

import android.bluetooth.BluetoothAdapter;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bt;
    Button btToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = BluetoothAdapter.getDefaultAdapter();
        btToggle = (Button) findViewById(R.id.btToggle);
        
        btToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBluetooth();
            }

        });
    }

    private void toggleBluetooth() {
        if(bt == null){
         Toast.makeText(getApplicationContext(), "Couldn't find Bluetooth device", Toast.LENGTH_SHORT).show();
        }
        if (!bt.isEnabled()){
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBT);
        }
    }

}