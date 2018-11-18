package com.cohen.gad.gettexiu.controller;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.cohen.gad.gettexiu.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
//f
    private EditText NameTextInput;
    private EditText EmailTextInput;
    private EditText InitialLocationTextInput;
    private EditText DestinationTextInput;
    private EditText LeavingTimeTextInput;
    private EditText ArrivalTimeTextInPut;
    private Button SubmitButton;
    private TimePickerDialog timePickerDialog;
    private final static int PLACE_PICKER_RESULT =1;
    private final static int PLACE_PICKER_RESULT_ =2;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void findViews() {
        NameTextInput = (EditText)findViewById( R.id.NameTextInput );
        EmailTextInput = (EditText)findViewById( R.id.EmailTextInput );
        InitialLocationTextInput = (EditText)findViewById( R.id.InitialLocationTextInput );
        DestinationTextInput = (EditText)findViewById( R.id.DestinationTextInput );
        LeavingTimeTextInput = (EditText)findViewById( R.id.LeavingTimeTextInput );
        SubmitButton= (Button)findViewById(R.id.SubmitButton);

        LeavingTimeTextInput.setShowSoftInputOnFocus(false);
        InitialLocationTextInput.setShowSoftInputOnFocus(false);
        DestinationTextInput.setShowSoftInputOnFocus(false);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        String localTime = date.format(currentLocalTime);
        LeavingTimeTextInput.setText(localTime);



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





        InitialLocationTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Intent intent;

                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_RESULT);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        DestinationTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Intent intent;

                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_RESULT_);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == PLACE_PICKER_RESULT|| requestCode == PLACE_PICKER_RESULT_){
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data,this);
                String address = String.format("%s",place.getAddress());
                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;

                if(requestCode == PLACE_PICKER_RESULT)
                    InitialLocationTextInput.setText(address);
                else if(requestCode == PLACE_PICKER_RESULT_)
                    DestinationTextInput.setText(address);
            }
        }
    }


    void checkInput() {

        if (!(EmailTextInput.getText().toString().contains("@"))) {

        }
    }



}






