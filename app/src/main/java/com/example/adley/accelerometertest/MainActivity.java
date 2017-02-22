/*
Created by Adley Gin 2.11.16
ECE 473
HW2
MainActivity.java
 */

package com.example.adley.accelerometertest;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private float x,y,z;
    private float last_X=0, last_Y=0, last_Z=0;
    private TextView XTextView,YTextView,ZTextView, timerText;
    private final float SENSOR_SENSITIVITY=.01f;
    private float deltaX, deltaY, deltaZ;
    private long currentTime=0, lastTime=0;
    private ArrayList<AccelerometerDataPoint> accelerometerDataPoints;
    private Button recordButton, emailButton, settingsButton;
    private MyCustomTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        accelerometerDataPoints=new ArrayList<AccelerometerDataPoint>();

        recordButton=(Button)findViewById(R.id.button);
        emailButton=(Button)findViewById(R.id.button3);
        settingsButton=(Button)findViewById(R.id.settingsButton);
        initializeTextView();
        initializeSensor();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });
    }


    /*
    Initialize the sensor.
     */
    public void initializeSensor(){
        mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if(mAccelerometerSensor!=null) {
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        else{
            Log.d("SensorError","Error in initializeSensor()");
        }
    }

    /*
    Initialize the textviews of the XYZ sensors and timer
     */
    public void initializeTextView(){
        XTextView=(TextView)findViewById(R.id.X_SENSOR_DISPLAY);
        YTextView=(TextView)findViewById(R.id.Y_SENSOR_DISPLAY);
        ZTextView=(TextView)findViewById(R.id.Z_SENSOR_DISPLAY);
        timerText=(TextView)findViewById(R.id.timerText);

    }

    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }
    /*
    As documented in the SensorManager class, you must unregister the sensor when the screen turns off, otherwise the sensor will remain on and
    drain the battery.
     */
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
    @Override
    /*
    SensorEvent holds data on the sensor type, timestamp, accuracy, and the numerical data. In this case, acceleration.
    onSensorChanged is called every time the accelerometer value is changed.
     */
    public void onSensorChanged(SensorEvent event){
        currentTime=System.currentTimeMillis();
        /*
        Only do stuff every 100 milliseconds. This way we don't get a TON of data points. Because the accelerometer is being called very quickly.
         */
        if(currentTime-lastTime>10) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            //deltaX = Math.abs(x - last_X);
            //deltaY = Math.abs(y - last_Y);
            //deltaZ = Math.abs(z - last_Z);

            /*
            If the record button text is "Stop Recording", then we are currently recording. So put the xyz data points into the data point array.
             */
            if(recordButton.getText().equals("Stop Recording")) {
                AccelerometerDataPoint point = new AccelerometerDataPoint(x, y, z); //create a new AcclerometerDataPoint object, which holds the xyz values of the sensor
                accelerometerDataPoints.add(point); //add the data point to an array list of data points.
            }
            displaySensorValues();
            lastTime=currentTime;
        }

    }

    /*
    Set the textView for each xyz sensor. Only set the text if the value is bigger than some delta, so that the numbers aren't going crazy fast with
    every little movement.
     */
    public void displaySensorValues()
    {
        /*if(deltaX>SENSOR_SENSITIVITY) {
            XTextView.setText(Float.toString(x));
            last_X=x;
        }
        if(deltaY>SENSOR_SENSITIVITY) {
            YTextView.setText(Float.toString(y));
            last_Y=y;
        }
        if(deltaZ>SENSOR_SENSITIVITY) {
            ZTextView.setText(Float.toString(z));
            last_Z=z;
        }*/
        XTextView.setText(Float.toString(x));
        YTextView.setText(Float.toString(y));
        ZTextView.setText(Float.toString(z));
    }

    /*
    This function writes a set of accelerometer data points to a specified file.
    For every 10 seconds of recording, we get a little under 3KB of data.
    MAKE SURE YOU HAVE PERMISSION TO WRITE TO EXTERNAL STORAGE OTHERWISE IT WON'T WORK. ADD PERMISSION TO MANIFEST FILE.
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     */
    public void writeToFile(ArrayList<AccelerometerDataPoint> dataPoints){
        String space=" ";

        try{
            /*
            Create the file name and header before inserting the data.
            File name is the date and time, along with my name.
             */
            String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
            String time= new SimpleDateFormat("KK:mm:ss").format(new Date());
            String fileName= new String("Adley Gin " + date + " " + time + ".txt");
            File newDataFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);
            FileWriter writer=new FileWriter(newDataFile);
            BufferedWriter buffWriter=new BufferedWriter(writer);

            buffWriter.write("%This text file contains accelerometer data points. Each row is in x y z format.");
            buffWriter.write("\r\n");


            buffWriter.write("%Generated on " + date + " @ " + time);
            buffWriter.write("\r\n");
            buffWriter.write("%By Adley Gin");       //Hardcoded in my name. May change that later to allow different users.
            buffWriter.write("\r\n");

            /*
            For each data point, write the xyz values to the file.
             */
            for(AccelerometerDataPoint adp:dataPoints) {
                float x = adp.getX();
                float y = adp.getY();
                float z = adp.getZ();
                buffWriter.write(Float.toString(x));
                buffWriter.write(space);
                buffWriter.write(Float.toString(y));
                buffWriter.write(space);
                buffWriter.write(Float.toString(z));
                buffWriter.write("\r\n");
            }
            buffWriter.close(); //MAKE SURE YOU CLOSE THE WRITER OR ELSE IT WON'T WORK
        }
        catch(Exception e){
            Log.d("ErrorinWriteToFile","Error in writeToFile()");
        }
    }

    /*
    Test function to read from file. Not used.
     */
    public String readFromFile(String fileToBeRead)
    {
        String ret = "";

        try {
            File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File targetFile=new File(path, fileToBeRead);
            FileInputStream inputStream = new FileInputStream(targetFile);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString).append("\n");               //IF YOU WANT TO APPEND NEWLINE WHEN READING FROM TEXT FILE, MAKE SURE TO INCLUDE .append("\n")
                }

                inputStream.close();
                ret = stringBuilder.toString();


            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    /*
    This function is called when the Send Email button is clicked. This creates an Intent, which is used to create another activity window
    The new activity window will display a list of data files for the user to email.
     */
    public void sendIntentToMain2Activity(View view){
        Intent intent=new Intent(this, Main2Activity.class);    //Constructor for Intent: Intent(Context context, Class class)
        startActivity(intent);
    }

    /*
    This function is called when the Settings button is clicked. It creates an Intent to create another activity window.
    The new activity window will display settings for the user to specify recording times and file sizes.
     */
    public void sendIntentToSettingsActivity(View view){
        Intent intent=new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /*
    This function stops and starts recording on the click of the record button.
    When the button is pressed, the color and text changes to "Start/Green" or "Stop/Red".
     The email and settings buttons are made invisible so that the user cannot press them during recording.
     When recording is stopped, the data points are written to a .txt file.
     The email and settings button are made visible again once recording has stopped.
     */
    public void startStopRecording(View view){

        /*
        Created a SharedPreferences object to get the status of the Settings checkbox. If it is checked, we want to add a timer when we record.
         */
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(recordButton.getText().equals("Start Recording")) {
            recordButton.setBackgroundColor(0xFF9E0F11);
            recordButton.setText("Stop Recording");
            emailButton.setVisibility(View.INVISIBLE);
            settingsButton.setVisibility(View.INVISIBLE);

            //If the settings checkbox is checked, the user wants to add a countdown timer to the recording. So we add it here.
            if(preferences.getBoolean("settingsCheckBox", false)) {
                timerText.setVisibility(View.VISIBLE);
                long start=(long) preferences.getInt("settingsTimer", 100);
                timer = new MyCustomTimer(start*1000, 100);
                timer.start();
            }

        }
        else if(recordButton.getText().equals("Stop Recording")) {
            recordButton.setBackgroundColor(0xFF096509);
            recordButton.setText("Start Recording");
            emailButton.setVisibility(View.VISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
            if(timerText.getVisibility()==View.VISIBLE) {
                timer.cancel();
                timerText.setVisibility(View.GONE);
            }
            writeToFile(accelerometerDataPoints);  //Write to file
            accelerometerDataPoints.clear(); //Clear Accelerometer data points array
        }
    }

    /*
    This class is a custom timer, which is used for the countdown timer.
    When the timer finishes, it performs a click on the record button, which causes the recording to stop.
    The timer is then made invisible.
     */
    public class MyCustomTimer extends CountDownTimer {
        public MyCustomTimer(long startTime, long interval){
            super(startTime, interval);
        }

        @Override
        public void onFinish(){
            recordButton.performClick();
            timerText.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished){
            timerText.setText(Long.toString(millisUntilFinished/1000)); //divide by 1000 to get seconds instead of milliseconds.

        }
    }




}
