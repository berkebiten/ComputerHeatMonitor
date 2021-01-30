package com.example.computerheatmonitor;

import android.bluetooth.BluetoothAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bt;
    Button btToggle, pairButton;
    ListView pairedList;

    public static String EXTRA_ADDRESS = "device_address";
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = BluetoothAdapter.getDefaultAdapter();
        btToggle = findViewById(R.id.btToggle);
        pairButton = findViewById(R.id.pairButton);
        pairedList = findViewById(R.id.pairedList);
        startService(new Intent(this, backgroundService.class));
        pairButton.setOnClickListener(v -> listDevices());
        btToggle.setOnClickListener(v -> toggleBluetooth());
    }

    private void listDevices(){
        Set<BluetoothDevice> pairedDevices = bt.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0){
            for (BluetoothDevice bt: pairedDevices){
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No paired device found.", Toast.LENGTH_SHORT).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        pairedList.setAdapter(adapter);
        pairedList.setOnItemClickListener(selectDevice);
    }

    private void toggleBluetooth() {
        if(bt == null){
         Toast.makeText(getApplicationContext(), "Couldn't find Bluetooth device", Toast.LENGTH_SHORT).show();
        }
        if (!bt.isEnabled()){
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBT);
        }
        if (bt.isEnabled()){
            bt.disable();
        }
    }

    public AdapterView.OnItemClickListener selectDevice = (parent, view, position, id) -> {
        String info = ((TextView) view).getText().toString();
        String address = info.substring(info.length() - 17);

        Intent comIntent = new Intent (MainActivity.this, sensorDataActivity.class);
        comIntent.putExtra(EXTRA_ADDRESS, address);
        startActivity(comIntent);
    };

}