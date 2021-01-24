package com.example.ashwith.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ConnectivityManager conn;
    BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        bluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
        ActivityCompat.requestPermissions(this,
                new String[]
                        {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                        },3);

        IntentFilter filter1=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mRceiver1,filter1);
       /* IntentFilter filter2=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mRceiver2,filter2);*/

    }
    private final BroadcastReceiver mRceiver1=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           String action=intent.getAction();
            Toast.makeText(context, "Inside on Receive of receiver1", Toast.LENGTH_SHORT).show();
            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                int state =intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);

                switch (state){
                    case  BluetoothAdapter.STATE_OFF:
                        Toast.makeText(MainActivity.this, "Bluetooth off", Toast.LENGTH_SHORT).show();
                        break;
                    case  BluetoothAdapter.STATE_ON:
                        Toast.makeText(MainActivity.this, "Bluetooth on", Toast.LENGTH_SHORT).show();
                        break;
                    case  BluetoothAdapter.STATE_TURNING_OFF:
                        Toast.makeText(MainActivity.this, "Bluetooth turning off", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        }

    };
    public  void Status(View view)
    {
        NetworkInfo networkInfo=conn.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
        {
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                Toast.makeText(this, "WI_FI", Toast.LENGTH_SHORT).show();
            }
            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                Toast.makeText(this, "Mobile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRceiver1);
        bluetoothAdapter.cancelDiscovery();


    }
    public void discoverBluetoothDevices(View view)
    {
        if(bluetoothAdapter!=null)
            bluetoothAdapter.startDiscovery();
        Toast.makeText(this, "Start Discovery"+bluetoothAdapter, Toast.LENGTH_SHORT).show();
    }
    public void turnon(View view)
    {
        if(bluetoothAdapter==null)
        {
            Toast.makeText(this, "Devices not supported bluetooth", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(!bluetoothAdapter.isEnabled())
            {
                Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i,1);
            }
            if(bluetoothAdapter.isEnabled())
            {
                Toast.makeText(this, "Bluetooth is Enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

       if(requestCode==1)
       {
           if(requestCode==RESULT_OK)
           {
               Toast.makeText(this, "Bluetooth Turned on", Toast.LENGTH_SHORT).show();
           }
           if(requestCode==RESULT_CANCELED)
           {
               Toast.makeText(this, "Bluetooth Turned on Failed", Toast.LENGTH_SHORT).show();
           }
       }else if(requestCode==2)
       {
           if(requestCode!=RESULT_CANCELED)
           {
               Toast.makeText(this, "Devices Discoverability Start", Toast.LENGTH_SHORT).show();
           }else
           {

                   Toast.makeText(this, "Devices Discoverability Cancelled", Toast.LENGTH_SHORT).show();

           }
       }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
