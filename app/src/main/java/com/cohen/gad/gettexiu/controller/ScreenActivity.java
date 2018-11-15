package com.cohen.gad.gettexiu.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cohen.gad.gettexiu.R;
import com.spark.submitbutton.SubmitButton;

public class ScreenActivity extends Activity {

    SubmitButton submitButton;
    EditText phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        findViews();






    }

    private void findViews() {
        final Intent nextIntent=new Intent( ScreenActivity.this, MainActivity.class);
        submitButton = (SubmitButton) findViewById(R.id.sendButton);
        phoneNumber= (EditText) findViewById(R.id.PhoneNumber) ;
        submitButton.setVisibility(View.GONE);

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(ScreenActivity.this,"GO",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity( nextIntent );
                    }
                }, 3000);
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()== 10)
                    submitButton.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()> 10)
                    submitButton.setVisibility(View.GONE);

            }
        });

    }

}
