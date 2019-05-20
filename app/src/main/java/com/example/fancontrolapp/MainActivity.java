package com.example.fancontrolapp;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button enableBluetooth;
    static BluetoothAdapter bluetoothAdapter;

    private static String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    public static void verifyPermissions(Activity activity) {

        // Check if we have location permission
        int locationPermission = ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int bluetoothPermission = ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.BLUETOOTH);

        if (locationPermission != PackageManager.PERMISSION_GRANTED ||
                bluetoothPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission, so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_REQUIRED,1);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enableBluetooth = findViewById(R.id.BtnBluetoothON);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();


        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Device does not support BLE", Toast.LENGTH_SHORT).show();
            enableBluetooth.setVisibility(View.INVISIBLE);
            finish();
        }

        if (bluetoothAdapter.isEnabled()) {
            enableBluetooth.setVisibility(View.INVISIBLE);
        }


        //BtnHumidity.setOnClickListener(this);
        //BtnTemperature.setOnClickListener(this);
    }

    public void onClick(View v) {

        verifyPermissions(this);

        switch (v.getId()) {

            case R.id.startScan:
                Intent scanIntent = new Intent(MainActivity.this, ConnectActivity.class);
                MainActivity.this.startActivity(scanIntent);
                break;

            case R.id.BtnBluetoothON:
                // If Bluetooth is OFF, turn it ON
                if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(enableBtIntent);
                    Toast.makeText(this, "Bluetooth is Turned ON", Toast.LENGTH_LONG).show();
                    enableBluetooth.setClickable(false);
                }

        }
    }
}


