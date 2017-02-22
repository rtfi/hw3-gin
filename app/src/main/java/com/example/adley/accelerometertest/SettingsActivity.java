/*
Adley Gin 2.11.16
ECE473 HW2
SettingsActivity.java is the third activity for the user to change the settings, like timer and file size.
 */
package com.example.adley.accelerometertest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox checkBox;  //checkbox to determine if the user wants to implement countdown timer.
    private EditText timerSize;    //edit text field for specifying timer.
    private EditText fileSize;      //edit text field for specifying max file size.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkBox=(CheckBox)findViewById(R.id.settingsCheckBox);
        timerSize=(EditText)findViewById(R.id.timerEditText);
        fileSize=(EditText)findViewById(R.id.fileSizeEditText);

        /*
        These put the checkbox and the textfield values to what they were before.
        If the user closes this activity, and comes back, the values will be set as before.
         */
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        checkBox.setChecked(preferences.getBoolean("settingsCheckBox", false));
        timerSize.setText(preferences.getString("settingsTimerString", ""));
        fileSize.setText(preferences.getString("settingsFileSizeString", ""));



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        timerSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            /*
            This function allows the user to see the file size text field change in real time as they change the timer value.
            Make sure to check if the timer text field is empty, otherwise it crashes the app when the user clears the field.
            value and value2 are used to convert the specified time to the max file size.
            10 seconds of recording time resulted in 3kb of data.
             */
            @Override
            public void afterTextChanged(Editable s) {
                if (getCurrentFocus() == timerSize) {
                    if(!timerSize.getText().toString().isEmpty()) {
                        float value = Float.parseFloat(timerSize.getText().toString());
                        int value2=Math.round(value*3/10);
                        fileSize.setText(String.valueOf(value2));
                    }
                    else{
                        fileSize.setText(String.valueOf(0));
                    }
                }
            }
        });

        fileSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            /*
            This function allows the user to see the timer value change in real time as they specify a max file size.
            Somewhat tricky to implement. Make sure to check if the textfield is empty, otherwise it crashes when
            the user deletes everything in the textfield.
            I found that 10 seconds of recording resulted in 3kb of data, so the value and value2 are used to convert time to file size.
             */
            @Override
            public void afterTextChanged(Editable s) {
                if(getCurrentFocus()==fileSize){
                    if(!fileSize.getText().toString().isEmpty()) {
                        float value = Float.parseFloat(fileSize.getText().toString());
                        int value2=Math.round(value/3*10);
                        timerSize.setText(String.valueOf(value2));
                    }
                    else{
                        timerSize.setText(String.valueOf(0));
                    }
                }
            }
        });
    }

    /*
    SaveInstance allows me to save the check box and text field values if the user presses the Home button. But not the "back" button.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("checkBox", checkBox.isChecked());
        savedInstanceState.putString("timerSize", timerSize.getText().toString());
        savedInstanceState.putString("fileSize", fileSize.getText().toString());
    }

    /*
    This restores the state of the checkbox and the textfields when the user comes back.
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        boolean b= savedInstanceState.getBoolean("checkBox");

        checkBox.setChecked(b);
        timerSize.setText(savedInstanceState.getString("timerSize"));
        fileSize.setText(savedInstanceState.getString("fileSize"));
    }

    /*
    This saves the state of the checkbox, and the values of the textfields in a SharedPreferences object.
    I use an Int and a String for each value, because the textfield value is a string, but the countdown timer
    in the MainActivity wants an int value to use for the countdown.
     */
    public void saveChanges(View view){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        if(fileSize.getText().toString().isEmpty()){
            fileSize.setText(String.valueOf(0));
        }
        if(timerSize.getText().toString().isEmpty()){
            timerSize.setText(String.valueOf(0));
        }
        editor.putBoolean("settingsCheckBox", checkBox.isChecked());
        editor.putInt("settingsTimer", Integer.parseInt(timerSize.getText().toString()));
        editor.putString("settingsTimerString", timerSize.getText().toString());
        editor.putInt("settingsFileSize", Integer.parseInt(fileSize.getText().toString()));
        editor.putString("settingsFileSizeString", fileSize.getText().toString());
        editor.commit();
        Toast toast=Toast.makeText(getApplicationContext(),"Changes Successfully Saved", Toast.LENGTH_SHORT);
        toast.show();
    }

}
