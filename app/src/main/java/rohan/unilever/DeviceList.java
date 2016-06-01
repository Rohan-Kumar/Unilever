package rohan.unilever;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class DeviceList extends AppCompatActivity {

    ListView list;
    static ArrayList<String> listArray = new ArrayList<>(), address = new ArrayList<>();
    static BluetoothAdapter bluetoothAdapter;
    static BluetoothDevice bluetoothDevice;
    static BluetoothSocket bluetoothSocket;
    Set<BluetoothDevice> pairedDevice;
    static UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static android.os.Handler bluetoothIn;
    static int HandlerState = 0;
    static ConnectedThread connectedThread;
    public static String PERMISSION_PREF = "pref.permission";
    public static String ALL_PERMISSIONS_DONE = "all.permission";
    int reqCode = 1234;
    int request = 4321;
    SharedPreferences preferences;
    BroadcastReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        preferences = PreferenceManager.getDefaultSharedPreferences(DeviceList.this);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        list = (ListView) findViewById(R.id.list);


    }

    @Override
    protected void onResume() {
        super.onResume();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (preferences.getBoolean(PERMISSION_PREF, true))
                checkNotificationPermission();
            else {
                if (!isBluetoothOn()) {
                    Intent on = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(on, request);
                } else {
                    scanForDevice();
                }
            }
        } else {
            if (!isBluetoothOn()) {
                Intent on = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(on, request);
            } else {
                scanForDevice();
            }
        }

    }

   /* @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }*/

    public void scanForDevice() {

        Log.d("test", "inside scan func");
        bluetoothAdapter.startDiscovery();
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    listArray.add(device.getName() + "\n" + device.getAddress());
                    address.add(device.getAddress());
                    Toast.makeText(DeviceList.this, device.getName(), Toast.LENGTH_SHORT).show();
                    showDevices();
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                unregisterReceiver(mReceiver);
                Intent intent = new Intent(DeviceList.this, MainActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
                finish();
            }
        });

        bluetoothIn = new Handler() {
            public void HandleMessage(Message msg) {
                if (msg.what == HandlerState) {
                }
            }

        };

    }


    void showDevices() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listArray);

        list.setAdapter(adapter);

    }


    boolean isBluetoothOn() {
        if (bluetoothAdapter.isEnabled())
            return true;
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkNotificationPermission() {
        new AlertDialog.Builder(DeviceList.this).setTitle("Permission required")
                .setMessage("This app requires you to grant location access")
                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        Intent callSettingIntent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        startActivityForResult(callSettingIntent, reqCode);
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, reqCode);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == reqCode) {
            preferences.edit().putBoolean(PERMISSION_PREF, true).apply();
            scanForDevice();
        } else
            preferences.edit().putBoolean(PERMISSION_PREF, false).apply();

    }


    public static BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    public static class ConnectedThread extends Thread {
        InputStream inputStream;
        OutputStream outputStream;


        public ConnectedThread(BluetoothSocket bluetoothSocket1) {
            try {
                inputStream = bluetoothSocket1.getInputStream();
                outputStream = bluetoothSocket1.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {

                    bytes = inputStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    int xyz = buffer[0];

                    Log.d("tag", readMessage + " xyz" + xyz);

                    bluetoothIn.obtainMessage(HandlerState, bytes, -1, readMessage).sendToTarget();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


        public void write(String input) {
            byte[] msg = input.getBytes();
            try {
                outputStream.write(msg);

            } catch (IOException e) {
                Log.d("ERROR", "FAILED  TO SEND");
                e.printStackTrace();
            }
        }


    }
}
