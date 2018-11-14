package com.cohen.gad.gettexiu.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.cohen.gad.gettexiu.R;

public class ScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        final Intent nextIntent=new Intent( ScreenActivity.this, MainActivity.class);
        final ImageButton sendbutton = findViewById(R.id.sendButton);
        sendbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity( nextIntent );
            }
        });
    }

}
