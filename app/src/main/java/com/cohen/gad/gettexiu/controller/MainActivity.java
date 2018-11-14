package com.cohen.gad.gettexiu.controller;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cohen.gad.gettexiu.R;
import com.cohen.gad.gettexiu.model.entities.Travel;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView StatusOfTravelLabel;
    private Spinner spinner;
    private EditText NameTextInput;
    private EditText EmailTextInput;
    private EditText InitialLocationTextInput;
    private EditText DestinationTextInput;
    private EditText LeavingTimeTextInput;
    private EditText ArrivalTimeTextInPut;
    private Button SubmitButton;
    private TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        mySpinner.setAdapter(new ArrayAdapter<Travel.TRAVEL_STATUS>(this, android.R.layout.simple_spinner_item, Travel.TRAVEL_STATUS.values()));
        findViews();

    }

    private void findViews() {
        spinner = (Spinner)findViewById( R.id.spinner );
        NameTextInput = (EditText)findViewById( R.id.NameTextInput );
        EmailTextInput = (EditText)findViewById( R.id.EmailTextInput );
        InitialLocationTextInput = (EditText)findViewById( R.id.InitialLocationTextInput );
        DestinationTextInput = (EditText)findViewById( R.id.DestinationTextInput );
        LeavingTimeTextInput = (EditText)findViewById( R.id.LeavingTimeTextInput );
        ArrivalTimeTextInPut = (EditText)findViewById( R.id.ArrivalTimeTextInPut );
        SubmitButton= (Button)findViewById(R.id.SubmitButton);

        //SubmitButton.setOnClickListener((View.OnClickListener) this);
        LeavingTimeTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        LeavingTimeTextInput.setText(String.format("%02d:%02d ", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });


        ArrivalTimeTextInPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        ArrivalTimeTextInPut.setText(String.format("%02d:%02d ", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

    }


    void checkInput() {

        if (!(EmailTextInput.getText().toString().contains("@"))) {

        }
    }



}






