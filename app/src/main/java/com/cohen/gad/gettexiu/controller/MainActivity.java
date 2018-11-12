package com.cohen.gad.gettexiu.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cohen.gad.gettexiu.R;
import com.cohen.gad.gettexiu.model.entities.Travel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);

        mySpinner.setAdapter(new ArrayAdapter<Travel.TRAVEL_STATUS>(this, android.R.layout.simple_spinner_item, Travel.TRAVEL_STATUS.values()));

    }
}
