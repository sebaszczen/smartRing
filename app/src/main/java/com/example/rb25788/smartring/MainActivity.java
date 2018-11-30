package com.example.rb25788.smartring;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final int ERROR_DIALOG_REQUEST = 9001;
    private BluetoothAdapter bluetoothAdapter;
    private Button btnDisableEnable_Discoverable;
    private List<BluetoothDevice> bluetoothDevicesList = new ArrayList<>();
    private DeviceListAdapter deviceListAdapter;
    private ListView lvNewDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isServiceOk()) {
            init();
        }
    }

    private void init() {
        Button btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        Button btnOnOff = (Button) findViewById(R.id.btnONOFF);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: enabling/disablin bluetooth");
                enableDisableBluetooth();
            }
        });

        btnDisableEnable_Discoverable = (Button) findViewById(R.id.btnDiscoverable_ONOFF);
        lvNewDevices=(ListView) findViewById(R.id.lvNewDevices);
        bluetoothDevicesList = new ArrayList<>();
    }

    private void enableDisableBluetooth() {
        if (bluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBluetooth: doesnt have bluetooth capabilities");
        }
        if (!bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBletooth: enabling bletooth");
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetoothIntent);

            //mBroadcastReveiver will catch state change and will log it
            IntentFilter bTintentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReveiver, bTintentFilter);
        }
        if (bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBletooth: disabling bletooth");
            bluetoothAdapter.disable();
            IntentFilter bTintentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReveiver, bTintentFilter);
        }
    }

    //create a broadcatReceiver for Action_Found
    private final BroadcastReceiver mBroadcastReveiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //when dicvovery finds a device
            if (action.equals(bluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, bluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: stateOFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver: state turning off");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver: STATE TURNING ON");
                        break;
                }
            }
        }
    };
    private final BroadcastReceiver mBroadcastReveiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //when dicvovery finds a device
            if (action.equals(bluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, bluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverablity enabled");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverablity disabled. Able to receive connections");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverablity disabled. Not able do receive conenctions");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: CONNECTING...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: CONNECTED.");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReveiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: Action found");
            //when dicvovery finds a device
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDevicesList.add(bluetoothDevice);
                Log.d(TAG, "onReceive: device name= " + bluetoothDevice.getName() + ": device adress= " + bluetoothDevice.getAddress());
                deviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, (ArrayList<BluetoothDevice>) bluetoothDevicesList);


                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, bluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverablity enabled");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverablity disabled. Able to receive connections");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverablity disabled. Not able do receive conenctions");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: CONNECTING...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: CONNECTED.");
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "on destroy called");
        super.onDestroy();
        unregisterReceiver(mBroadcastReveiver);
    }

    public boolean isServiceOk() {
        Log.d(TAG, "isServiceOk : checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and user can make map request
            Log.d(TAG, "isServicesOk: Google PLay Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occured but it can be fixed
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
        } else {
            Toast.makeText(this, "Ther is nothing you can do", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void btnEnableDisable_Discoverable(View view) {
        Log.d(TAG, "making device discoverable");
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        IntentFilter intentFilter = new IntentFilter(bluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReveiver2, intentFilter);
    }

    public void discoverDevices(View view) {
        Log.d(TAG, "discoverDevices: Looking for unpaired devices");
        if (bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDicoverDevices: Cancelling discovery");
            getPermissions();
            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReveiver3, discoverDevicesIntent);
        }
        if (!bluetoothAdapter.isDiscovering()) {
            getPermissions();
            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReveiver3, discoverDevicesIntent);
        }
    }

//    private void checkPermissions() {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACESS.FINE.LOACATION");
//            permissionCheck += this.checkSelfPermission("Manifest.permission.ACESS.COARSE.LOACATION");
//            if (permissionCheck != 0) {
//                this.requestPermissions(new String[])
//            }
//        }
//    }

    public void getPermissions() {
        Log.d(TAG, "getPermissions: getting permissions");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), MapActivity.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this.getApplicationContext(), MapActivity.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                MapActivity.mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{MapActivity.ACCESS_COARSE_LOCATION, MapActivity.ACCESS_FINE_LOCATION}, MapActivity.LOCATION_PERMISSSION_REQUEST_CODE);
            }
        }
        else {
            Log.d(TAG, "getPermissions: No need to check permissions, SDK version< LOLIPOP");
        }

    }
}
