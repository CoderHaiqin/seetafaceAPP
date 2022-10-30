package com.example.btopen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {

    private BluetoothAdapter mBluetooth;
    private BluetoothSocket socket;
    final private int mOpenCode = 0x01;
    static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ListView listView;
    private List<BluetoothDevice> listBluetooth;
    private List<String> deviceAddress = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    protected BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                deviceAddress.clear();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listBluetooth.add(device);
                deviceAddress.add(device.getAddress());
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if(ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_SCAN")
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{"android.permission.BLUETOOTH_SCAN"}, 100);
        }

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                deviceAddress
        );

        listView = findViewById(R.id.device_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothDevice target = mBluetooth.getRemoteDevice(deviceAddress.get(i));
                try{
                    socket = target.createRfcommSocketToServiceRecord(SPP_UUID);
                    if(socket != null){
                        socket.connect();
                    }else{
                        Toast.makeText(MainActivity2.this, "failed", Toast.LENGTH_SHORT);
                    }
                }catch (IOException e){
                    finish();
                }catch (SecurityException e){
                    finish();
                }

            }
        });

        blueToothInit();

    }

    private void blueToothInit() {
        mBluetooth = BluetoothAdapter.getDefaultAdapter();
    }

    public void openBt(View view) {
        if(!mBluetooth.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            try {
                startActivityForResult(intent, mOpenCode);
            }catch (SecurityException e){
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == mOpenCode){
            if(resultCode == 120){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if(!mBluetooth.isDiscovering()){
                                mBluetooth.startDiscovery();
                            }
                        }catch(SecurityException e){
                            finish();
                        }
                    }
                }, 50);
            }else{
                finish();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter discoveryFilter = new IntentFilter();
        discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryReceiver, discoveryFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(discoveryReceiver);
    }

    public void clickPairedDevices(View view) {
        try{
            deviceAddress.clear();
            Toast.makeText(MainActivity2.this, "start",Toast.LENGTH_SHORT).show();
            Set<BluetoothDevice> deviceSet = mBluetooth.getBondedDevices();
            for(BluetoothDevice device : deviceSet){
                deviceAddress.add(device.getAddress());
            }
            adapter.notifyDataSetChanged();
        }catch(SecurityException e){
            finish();
        }
    }

    public void clickOpen(View view) {
        if(socket != null){
            try{
                if(!socket.isConnected()){
                    socket.connect();
                }
                new DataOutputStream(socket.getOutputStream()).writeUTF("180");
            }catch(IOException e){
                finish();
            }catch(SecurityException e){
                finish();
            }
        }
    }

    public void clickClose(View view) {
        if(socket != null){
            try{
                if(!socket.isConnected()){
                    socket.connect();
                }
                new DataOutputStream(socket.getOutputStream()).writeUTF("0");
            }catch(IOException e){
                finish();
            }catch(SecurityException e){
                finish();
            }
        }
    }
}