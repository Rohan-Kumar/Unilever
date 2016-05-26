package rohan.unilever;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView time1, time2, time3, time4, speed1, speed2, speed3, speed4;

    // speed-hexx[(s1%5)+5]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init() {
        time1 = (AutoCompleteTextView) findViewById(R.id.time1);
        time2 = (AutoCompleteTextView) findViewById(R.id.time2);
        time3 = (AutoCompleteTextView) findViewById(R.id.time3);
        time4 = (AutoCompleteTextView) findViewById(R.id.time4);
        speed1 = (AutoCompleteTextView) findViewById(R.id.speed1);
        speed2 = (AutoCompleteTextView) findViewById(R.id.speed2);
        speed3 = (AutoCompleteTextView) findViewById(R.id.speed3);
        speed4 = (AutoCompleteTextView) findViewById(R.id.speed4);
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
        if (s1 > 210 || (s1 % 5) != 0) {
            speed1.setError("Should be a multiple of 5 less than 210");
            speed1.requestFocus();
            return;
        }
        if (s2 > 210 || (s2 % 5) != 0) {
            speed2.setError("Should be a multiple of 5 less than 210");
            speed2.requestFocus();
            return;
        }
        if (s3 > 210 || (s3 % 5) != 0) {
            speed3.setError("Should be a multiple of 5 less than 210");
            speed3.requestFocus();
            return;
        }
        if (s4 > 210 || (s4 % 5) != 0) {
            speed4.setError("Should be a multiple of 5 less than 210");
            speed4.requestFocus();
            return;
        }
        String test[] = MainActivity.this.getResources().getStringArray(R.array.speed_hex);
        Toast.makeText(MainActivity.this, "Yet to come! "+(s1/5)+" "+ test[((s1/5))], Toast.LENGTH_SHORT).show();
    }

    public void stop(View view) {
        Toast.makeText(MainActivity.this, "Yet to come!", Toast.LENGTH_SHORT).show();
    }
}
