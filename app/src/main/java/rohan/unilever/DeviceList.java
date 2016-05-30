package rohan.unilever;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class DeviceList extends AppCompatActivity {

    ListView list;
    ArrayList<String> listArray = new ArrayList<>(),address = new ArrayList<>();
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket;
    Set<BluetoothDevice> pairedDevice;
    UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static android.os.Handler bluetoothIn;
    static int HandlerState = 0;
    static ConnectedThread connectedThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        list = (ListView) findViewById(R.id.list);


        if (!isBluetoothOn()){
             Intent on = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
             startActivityForResult(on, 5);
        }
        else{
            pairedDevice = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevice) {
                address.add(device.getAddress());
                listArray.add(device.getAddress() + "\n" + device.getName());
            }

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, listArray);

            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(DeviceList.this, "" + position, Toast.LENGTH_SHORT).show();
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(address.get(position));
                    try {
                        bluetoothSocket = createBluetoothSocket(bluetoothDevice);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        bluetoothSocket.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    connectedThread = new ConnectedThread(bluetoothSocket);
                    connectedThread.start();
                }
            });

            bluetoothIn = new Handler() {
                public void HandleMessage(Message msg) {
                    if (msg.what == HandlerState) {
                    }
                }

            };

        }
    }

    boolean isBluetoothOn()
    {
        if(bluetoothAdapter.isEnabled())
            return true;
        return false;

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

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
                Log.d("ERROR","FAILED  TO SEND");
                e.printStackTrace();
            }
        }


    }
}
