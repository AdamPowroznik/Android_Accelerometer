package com.example.accelerometer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        SensorEventListener {
    TextView tv1, tv2, tv3, tv4;
    EditText et1;
    SensorManager sensorManager;
    Sensor accelerometer;
    float [] values;
    float x, y, z;
    int counter = 0;
    SmsManager smsManager = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smsManager = SmsManager.getDefault();
        int SEND_SMS = 123;
        int hasPermission =
                checkSelfPermission(Manifest.permission.SEND_SMS);
        String[] permissions = new String[] {Manifest.permission.SEND_SMS};
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, SEND_SMS);
        }
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);
        et1 = findViewById(R.id.editText);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            values = event.values;
            x = values[0];
            y = values[1];
            z = values[2];
            tv1.setText("X: " + String.valueOf(x)+ " m/s2");
            tv2.setText("Y: " + String.valueOf(y) + " m/s2");
            tv3.setText("Z: " + String.valueOf(z) + " m/s2");
            float accelationSquareRoot = (x * x + y * y + z * z) /(SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            if (accelationSquareRoot < 1.5) {
                tv4.setText("Low Acceleration");
                tv4.setBackgroundColor(Color.GREEN);
            } else if ((accelationSquareRoot >= 1.5) &&
                    (accelationSquareRoot < 3)) {
                tv4.setText("Medium Acceleration");
                tv4.setBackgroundColor(Color.YELLOW);
            } else {
                tv4.setText("High Acceleration");
                tv4.setBackgroundColor(Color.RED);
                counter++;
                if(counter>4){
                    counter = 0;
                    smsManager.sendTextMessage(et1.getText().toString(), null,
                            "Czego mnom poczonsa on?", null, null);
                    Toast.makeText(getApplicationContext(), "Message sent!",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    protected void onResume() {
        super.onResume();
        accelerometer =
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}