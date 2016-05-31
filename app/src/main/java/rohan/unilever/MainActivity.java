package rohan.unilever;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText time1, time2, time3, time4, speed1, speed2, speed3, speed4;
    String sendString = "";

    // speed-hexx[(s1%5)+5]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    void init() {
        time1 = (EditText) findViewById(R.id.time1);
        time2 = (EditText) findViewById(R.id.time2);
        time3 = (EditText) findViewById(R.id.time3);
        time4 = (EditText) findViewById(R.id.time4);
        speed1 = (EditText) findViewById(R.id.speed1);
        speed2 = (EditText) findViewById(R.id.speed2);
        speed3 = (EditText) findViewById(R.id.speed3);
        speed4 = (EditText) findViewById(R.id.speed4);
    }


    public void start(View view) {
        if (time1.getText().toString().isEmpty()) {
            time1.setError("Cannot be empty");
            time1.requestFocus();
            return;
        }
        if (speed1.getText().toString().isEmpty()) {
            speed1.setError("Cannot be empty");
            speed1.requestFocus();
            return;
        }
        int t1 = Integer.parseInt(time1.getText().toString());
        int s1 = Integer.parseInt(speed1.getText().toString());
        int t2 = 0, t3 = 0, t4 = 0, s2 = 0, s3 = 0, s4 = 0;
        try {
            t2 = Integer.parseInt(time2.getText().toString());
        } catch (NumberFormatException e) {
        }
        try {
            t3 = Integer.parseInt(time3.getText().toString());
        } catch (NumberFormatException e) {
        }
        try {
            t4 = Integer.parseInt(time4.getText().toString());
        } catch (NumberFormatException e) {
        }
        try {
            s2 = Integer.parseInt(speed2.getText().toString());
        } catch (NumberFormatException e) {
        }
        try {
            s3 = Integer.parseInt(speed3.getText().toString());
        } catch (NumberFormatException e) {
        }
        try {
            s4 = Integer.parseInt(speed4.getText().toString());
        } catch (NumberFormatException e) {
        }
        if (t1 > 90) {
            time1.setError("Cannot be greater than 90");
            time1.requestFocus();
            return;
        }
        if (t2 > 90) {
            time2.setError("Cannot be greater than 90");
            time2.requestFocus();
            return;
        }
        if (t3 > 90) {
            time3.setError("Cannot be greater than 90");
            time3.requestFocus();
            return;
        }
        if (t4 > 90) {
            time4.setError("Cannot be greater than 90");
            time4.requestFocus();
            return;
        }
        if (s1 > 210 ) {
            speed1.setError("Should be less than 210");
            speed1.requestFocus();
            return;
        }
        if((s1 % 5) != 0){
            speed1.setError("Should be a multiple of 5");
            speed1.requestFocus();
            return;
        }
        if (s2 > 210) {
            speed2.setError("Should be less than 210");
            speed2.requestFocus();
            return;
        }
        if((s2 % 5) != 0){
            speed2.setError("Should be a multiple of 5 ");
            speed2.requestFocus();
            return;
        }
        if (s3 > 210) {
            speed3.setError("Should be less than 210");
            speed3.requestFocus();
            return;
        }
        if((s3 % 5) != 0){
            speed3.setError("Should be a multiple of 5");
            speed3.requestFocus();
            return;
        }
        if (s4 > 210 || (s4 % 5) != 0) {
            speed4.setError("Should be less than 210");
            speed4.requestFocus();
            return;
        }
        if((s4 % 5) != 0){
            speed4.setError("Should be a multiple of 5");
            speed4.requestFocus();
            return;
        }
        String speedArray[] = MainActivity.this.getResources().getStringArray(R.array.speed_hex);
        sendString = "23,01," + Integer.toHexString(t1).toUpperCase() + "," + speedArray[(s1 / 5)] + "," + Integer.toHexString(t2).toUpperCase() + "," + speedArray[(s2 / 5)] + "," + Integer.toHexString(t3).toUpperCase() + "," + speedArray[(s3 / 5)] + "," + Integer.toHexString(t4).toUpperCase() + "," + speedArray[(s4 / 5)] + "," + "2A";

        DeviceList.connectedThread = new DeviceList.ConnectedThread(DeviceList.bluetoothSocket);
        DeviceList.connectedThread.start();

        DeviceList.connectedThread.write(sendString);
    }

    public void stop(View view) {
        sendString = "23,01,0,0,0,0,0,0,0,0,2A";

        DeviceList.connectedThread = new DeviceList.ConnectedThread(DeviceList.bluetoothSocket);
        DeviceList.connectedThread.start();

        DeviceList.connectedThread.write(sendString);
    }
}
