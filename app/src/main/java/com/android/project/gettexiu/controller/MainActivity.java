package com.android.project.gettexiu.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.project.gettexiu.R;
import com.android.project.gettexiu.model.backend.FactoryMethod;
import com.android.project.gettexiu.model.entities.Travel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.shredzone.commons.suncalc.SunTimes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.android.project.gettexiu.model.backend.TravelConst.TravelToContentValues;

public class MainActivity extends AppCompatActivity {

    private EditText NameTextInput;
    private EditText EmailTextInput;
    private EditText InitialLocationTextInput;
    private EditText DestinationTextInput;
    private EditText LeavingTimeTextInput;
    private EditText ArrivalTimeTextInPut;
    private Button submitButton;
    private EditText PhoneNumber_;
    private TimePickerDialog timePickerDialog;
    private final static int PLACE_PICKER_RESULT = 1;
    private final static int PLACE_PICKER_RESULT_ = 2;
    Calendar calendar;
    int currentHour;
    int currentMinute;

    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    // Define a listener that responds to location updates
    LocationListener locationListener;

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
        NameTextInput = (EditText) findViewById(R.id.NameTextInput);
        EmailTextInput = (EditText) findViewById(R.id.EmailTextInput);
        InitialLocationTextInput = (EditText) findViewById(R.id.InitialLocationTextInput);
        DestinationTextInput = (EditText) findViewById(R.id.DestinationTextInput);
        LeavingTimeTextInput = (EditText) findViewById(R.id.LeavingTimeTextInput);
        submitButton = (Button) findViewById(R.id.SubmitButton);
        PhoneNumber_ = (EditText) findViewById(R.id.PhoneTextInput);


        setInitialValues();
        createListeners();
    }

    private void createListeners() {

        LeavingTimeTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        LeavingTimeTextInput.setText(String.format("%02d:%02d ", hourOfDay, minutes));
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

        submitButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                Travel travel = new Travel(
                        InitialLocationTextInput.getText().toString(),
                        DestinationTextInput.getText().toString(),
                        LeavingTimeTextInput.getText().toString(),
                        NameTextInput.getText().toString(),
                        getIntent().getStringExtra("Phone"),
                        EmailTextInput.getText().toString());

                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        FactoryMethod.getManager().addNewTravel(TravelToContentValues(travel));
                        return FactoryMethod.getManager().checkIfTravelAdded(getIntent().getStringExtra("Phone"));
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if (aBoolean)
                            Toast.makeText(MainActivity.this, "Add to database successful", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "Add to database not successful", Toast.LENGTH_SHORT).show();


                    }
                }.execute();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setInitialValues() {
        LeavingTimeTextInput.setShowSoftInputOnFocus(false);
        InitialLocationTextInput.setShowSoftInputOnFocus(false);
        DestinationTextInput.setShowSoftInputOnFocus(false);


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        String localTime = date.format(currentLocalTime);
        LeavingTimeTextInput.setText(localTime);
        PhoneNumber_.setText(getIntent().getStringExtra("Phone"));
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                showSunTimes(location.getLatitude(), location.getLongitude()); /// ...

                // Called when a new location is found by the network location provider.
                //    Toast.makeText(getBaseContext(), location.toString(), Toast.LENGTH_LONG).show();
                InitialLocationTextInput.setText(getPlace(location));////location.toString());

                // Remove the listener you previously added
                //  locationManager.removeUpdates(locationListener);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_RESULT || requestCode == PLACE_PICKER_RESULT_) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String address = String.format("%s", place.getAddress());
                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;

                if (requestCode == PLACE_PICKER_RESULT)
                    InitialLocationTextInput.setText(address);
                else if (requestCode == PLACE_PICKER_RESULT_)
                    DestinationTextInput.setText(address);
            }
        }
    }

    void showSunTimes(double lat, double lng) {
        Date date = new Date();// date of calculation

        SunTimes times = SunTimes.compute()
                .on(date)       // set a date
                .at(lat, lng)   // set a location
                .execute();     // get the results
        System.out.println("Sunrise: " + times.getRise());
        System.out.println("Sunset: " + times.getSet());
    }

    public String getPlace(Location location) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                return stateName + "\n" + cityName + "\n" + countryName;
            }

            return "no place: \n (" + location.getLongitude() + " , " + location.getLatitude() + ")";
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }
        return "IOException ...";
    }


    private void getLocation() throws IOException {

        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the location", Toast.LENGTH_SHORT).show();
            }
        }


    }



    }







