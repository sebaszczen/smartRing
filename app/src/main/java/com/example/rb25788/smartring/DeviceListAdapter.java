package com.example.rb25788.smartring;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private LayoutInflater layoutInflater;
    private List<BluetoothDevice> bluetoothDeviceList;
    private int mViewResourceId;

    public DeviceListAdapter(Context context, int tvResourceId, ArrayList<BluetoothDevice> bluetoothDevices) {
        super(context, tvResourceId,bluetoothDevices);
        this.bluetoothDeviceList = bluetoothDevices;
        this.mViewResourceId = tvResourceId;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(mViewResourceId, null);
        BluetoothDevice device = bluetoothDeviceList.get(position);
        if (device != null) {
            TextView deviceName = (TextView) convertView.findViewById(R.id.deviceName);
            TextView deviceAdress = (TextView) convertView.findViewById(R.id.deviceAdress);
            if (device.getName() != null) {
                deviceName.setText(device.getName());
            }
            if (device.getAddress() != null) {
                deviceAdress.setText(device.getAddress());
            }
        }
        return convertView;
    }
}
