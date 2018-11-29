package com.android.project.gettexiu.controller;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.project.gettexiu.R;
import com.android.project.gettexiu.model.backend.FactoryMethod;
import com.android.project.gettexiu.model.entities.Travel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.android.project.gettexiu.model.backend.TravelConst.TravelToContentValues;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //---------------------------------- Fields ------------------------------------------//

    private EditText NameTextInput;
    private EditText EmailTextInput;
    private EditText InitialLocationTextInput;
    private EditText DestinationTextInput;
    private EditText LeavingTimeTextInput;
    private PlaceAutocompleteFragment placeAutocompleteFragment1;
    private PlaceAutocompleteFragment placeAutocompleteFragment2;
    private Button submitButton;
    private EditText PhoneNumber_;
    private TimePickerDialog timePickerDialog;
    private ImageButton DestinationPlacePicker;
    private ImageButton InitialLocationPlacePicker;
    private final static int PLACE_PICKER_RESULT = 1;
    private final static int PLACE_PICKER_RESULT_ = 2;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    // Define a listener that responds to location updates
    LocationListener locationListener;

    //---------------------------------- Functions ------------------------------------//


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setInitialValues();
        createListeners();

    }


    //This function load all the xml Objects
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void findViews() {

        NameTextInput = (EditText) findViewById(R.id.NameTextInput);
        EmailTextInput = (EditText) findViewById(R.id.EmailTextInput);
        //InitialLocationTextInput = (EditText) findViewById(R.id.InitialLocationTextInput);
        //DestinationTextInput = (EditText) findViewById(R.id.DestinationTextInput);
        LeavingTimeTextInput = (EditText) findViewById(R.id.LeavingTimeTextInput);
        submitButton = (Button) findViewById(R.id.SubmitButton);
        PhoneNumber_ = (EditText) findViewById(R.id.PhoneTextInput);
        DestinationPlacePicker =(ImageButton)findViewById(R.id.destinationPickPlase);
        InitialLocationPlacePicker= (ImageButton)findViewById(R.id.initialLocationPickPlace);
        placeAutocompleteFragment1 = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById( R.id.place_autocomplete_fragment1 );
        placeAutocompleteFragment2 = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById( R.id.place_autocomplete_fragment2 );
    }

    //This function set all the Listeners
    private void createListeners() {

        LeavingTimeTextInput.setOnClickListener(this);
        InitialLocationPlacePicker.setOnClickListener(this);
        DestinationPlacePicker.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    //This function activates functions according to the pressed xml object
    @Override
    public void onClick(View v) {
        if(v == LeavingTimeTextInput)
            setTimeByTimePicker();
        if(v == InitialLocationPlacePicker)
            setInitialPlaceByPlacePicker();
        if(v == DestinationPlacePicker)
            setDestinationByPlacePicker();
        if (v == submitButton)
            submitNewTravel();
    }

    //This function activate the Place Picker for initial place
    private void setInitialPlaceByPlacePicker() {
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

    //This function activate the Place Picker for destination place
    private void setDestinationByPlacePicker() {
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


    //This function activate the Time Picker for initial time of ride
    private void setTimeByTimePicker() {

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

    //This function add travel to Database by asynchronous task
    @SuppressLint("StaticFieldLeak")
    private void submitNewTravel() {
        final Travel travel = new Travel(
                placeAutocompleteFragment1.toString(),
                placeAutocompleteFragment2.toString(),
                LeavingTimeTextInput.getText().toString(),
                NameTextInput.getText().toString(),
                getIntent().getStringExtra("Phone"),
                EmailTextInput.getText().toString());

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                String id= FactoryMethod.getManager().addNewTravel(TravelToContentValues(travel));
                return FactoryMethod.getManager().checkIfTravelAdded(id);
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


    //This function set initial values on the xml Objects
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setInitialValues() {

        //set the current time by default
        LeavingTimeTextInput.setShowSoftInputOnFocus(false);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        String localTime = date.format(currentLocalTime);
        LeavingTimeTextInput.setText(localTime);
        //set the phone number that was send by previous Activity
        PhoneNumber_.setText(getIntent().getStringExtra("Phone"));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        placeAutocompleteFragment1.setHint("Initial location");
        placeAutocompleteFragment2.setHint("Destination");

    }

    //This function use for  the PlacePickers results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_RESULT || requestCode == PLACE_PICKER_RESULT_) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String address = String.format("%s", place.getAddress());
                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;

                if (requestCode == PLACE_PICKER_RESULT)
                    placeAutocompleteFragment1.setText(address);
                else if (requestCode == PLACE_PICKER_RESULT_)
                    placeAutocompleteFragment2.setText(address);
            }
        }
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



}







