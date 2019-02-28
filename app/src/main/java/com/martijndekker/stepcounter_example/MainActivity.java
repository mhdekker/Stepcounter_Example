package com.martijndekker.stepcounter_example;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

//This is an example of how to measure steps using the Step_Counter_Sensor
// https://developer.android.com/reference/android/hardware/Sensor.html#TYPE_STEP_COUNTER
//This sensor can be missing on some older devices and DOES NOT run in the emulator

//Made by Martijn Dekker for the course Creative Apps,
//Based on: "Develop simple step counter" Retrieved from: https://www.youtube.com/watch?v=CNGMWnmldaU

/*You might need some tweaking for your app, since this app displays the value of the sensor
 since last reboot. To fix this you could save and subtract the value when booting up the app,
 so the stepcount will be zero*/

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //initiate the sensor
    SensorManager sensorManager;

    TextView tv_steps;

    //Boolean to see if the app is running or not.
    //You will see this being set to different values in the onPause and onResume method.
    boolean isRunning = false;


    //This function starts when the first activity has started and is used to setup the basics
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find and link the TextView
        tv_steps = (TextView) findViewById(R.id.tv_steps);

        //Find and link the sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    /* Called when the activity will start interacting with the user.
    At this point your activity is at the top of the activity stack, with user input going to it.
    In this case we use it to find the sensor and check if it is available, otherwise it will send an error*/
    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    /* Called when the system is about to start resuming a previous activity.
    This is typically used to commit unsaved changes to persistent data,
    stop animations and other things that may be consuming CPU, etc.
    Implementations of this method must be very quick because the next
    activity will not be resumed until this method returns.
    In this case we use it to unregister the listener.*/
    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        sensorManager.unregisterListener(this);
    }

    //When the sensor value changes and the app is running, display the steps in the TextView
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(isRunning) {
            tv_steps.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
