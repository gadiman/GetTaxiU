package com.android.project.gettexiu.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //---------------------------------- Fields ------------------------------------------//
    //xml objects
    private EditText NameTextInput;
    private EditText EmailTextInput;
    private EditText LeavingTimeTextInput;
    private PlaceAutocompleteFragment placeAutocompleteFragment1;
    private PlaceAutocompleteFragment placeAutocompleteFragment2;
    private Button submitButton;
    private EditText PhoneNumber_;
    //Time picker for pick a leaving time
    private TimePickerDialog timePickerDialog;
    //Place picker if the user chose by a map
    private ImageButton DestinationPlacePicker;
    private ImageButton InitialLocationPlacePicker;
    //Those fields for Place Picker results
    private final static int PLACE_PICKER_RESULT = 1;
    private final static int PLACE_PICKER_RESULT_ = 2;
    //Those fields for set the current time by default
    Calendar calendar;
    int currentHour;
    int currentMinute;
    //Those fields to store the locations into the travel objects
    String InitialAddress ="";
    String Destination= "";
    private String destinetionCityName_;
    private double intialLocationLatitude_;
    private double initialLocationLongitude_;
    private double destinetionLatitude_;
    private double destinetionLongitude_;
    //Those fields to manager the location
    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    // Define a listener that responds to location updates
    LocationListener locationListener;
    Location locationA = new Location("A");//= new Location(from);
    Location locationB = new Location("B") ;//= new Location(to);
    public Criteria criteria;
    public String bestProvider;
    //This Travel object for check if the same travel was invented
    Travel travel_;

    //---------------------------------- Functions ------------------------------------//


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setInitialValues();
        createListeners();
        //This function do validate for the EditText's input before user submit
        createOnChangeFocuseListeners();

    }



    //This function load all the xml Objects
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void findViews() {

        NameTextInput = (EditText) findViewById(R.id.NameTextInput);
        EmailTextInput = (EditText) findViewById(R.id.EmailTextInput);
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
        createPlaceListeners();

    }

    //This function create listeners for place Fields
    private void createPlaceListeners() {

        placeAutocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                locationA.setLatitude(place.getLatLng().latitude);
                locationA.setLongitude(place.getLatLng().longitude);
                intialLocationLatitude_ = place.getLatLng().latitude;
                initialLocationLongitude_ =place.getLatLng().longitude;
                getCityName(initialLocationLongitude_,intialLocationLatitude_);
                // .getAddress().toString();//get place details here
                InitialAddress = place.getAddress().toString();
                placeAutocompleteFragment1.setText(place.getAddress().toString());

            }

            @Override
            public void onError(Status status) {

            }
        });

        placeAutocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //  to = place.getAddress().toString();//get place details here
                locationB.setLatitude(place.getLatLng().latitude);
                locationB.setLongitude(place.getLatLng().longitude);
                destinetionLatitude_ =place.getLatLng().latitude;
                destinetionLongitude_ = place.getLatLng().longitude;
                getCityName(destinetionLongitude_,destinetionLatitude_);
                Destination = place.getAddress().toString();
            }

            @Override
            public void onError(Status status) {

            }
        });


        //Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

               showSunTimes(location.getLatitude(), location.getLongitude());

                // Called when a new location is found by the network location provider.
                //    Toast.makeText(getBaseContext(), location.toString(), Toast.LENGTH_LONG).show();
               placeAutocompleteFragment1.setText(getPlace(location));////location.toString());
               InitialAddress=getPlace(location);


                // Remove the listener you previously added
                // locationManager.removeUpdates(locationListener);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

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

                if(hourOfDay < currentHour || (hourOfDay == currentHour && minutes < currentMinute)){
                    Toast.makeText(getApplicationContext(), "The selected time has passed", Toast.LENGTH_SHORT).show();
                    LeavingTimeTextInput.setText(String.format("%02d:%02d ", currentHour, currentMinute));
                }
                else
                   LeavingTimeTextInput.setText(String.format("%02d:%02d ", hourOfDay, minutes));

            }
        }, currentHour, currentMinute, false);
        timePickerDialog.show();
    }

    //This function add travel to Database by asynchronous task
    @SuppressLint("StaticFieldLeak")
    private void submitNewTravel() {
        if(!ValidateSubmitButton())
        {
            Toast.makeText(this, "Fill out all fields before submitting", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            final Travel travel = new Travel(
                    InitialAddress,
                    Destination,
                    LeavingTimeTextInput.getText().toString(),
                    NameTextInput.getText().toString(),
                    PhoneNumber_.getText().toString(),
                    EmailTextInput.getText().toString(),
                    destinetionCityName_,
                    intialLocationLatitude_,
                    initialLocationLongitude_,
                    destinetionLatitude_,
                    destinetionLongitude_);

            new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected Boolean doInBackground(Void... voids) {
                    String id = FactoryMethod.getManager().addNewTravel(travel);
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

    }


    //This function set initial values on the xml Objects
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setInitialValues() {
         //set the current location
        getLocation();
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

    //This function use for the Place Pickers results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_RESULT || requestCode == PLACE_PICKER_RESULT_) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String address = place.getAddress().toString();
                LatLng queriedLocation = place.getLatLng();
                intialLocationLatitude_ = queriedLocation.latitude;
                initialLocationLongitude_= queriedLocation.longitude;


                if (requestCode == PLACE_PICKER_RESULT) {
                    placeAutocompleteFragment1.setText(address);
                    InitialAddress = address;
                    intialLocationLatitude_ = queriedLocation.latitude;
                    initialLocationLongitude_= queriedLocation.longitude;
                }
                else if (requestCode == PLACE_PICKER_RESULT_) {
                    placeAutocompleteFragment2.setText(address);
                    Destination = address;
                    destinetionLatitude_ = queriedLocation.latitude;
                    destinetionLongitude_ = queriedLocation.longitude;
                }
            }
        }
    }


    //This function validate EditText's input before submit
    private void createOnChangeFocuseListeners() {
        EmailTextInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(EmailTextInput.getText().toString()).matches()) {
                    EmailTextInput.setError("not valid email");

                }
            }
        });

        NameTextInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!NameTextInput.getText().toString().matches("[a-zA-Z\u0590-\u05fe]+")){
                    NameTextInput.setError("not valid name");
                }
            }
        });

        PhoneNumber_.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(PhoneNumber_.getText().toString().length() != 10)
                    PhoneNumber_.setError("not valid phone number");
            }


        });

    }

    //This function check if all the fields are filled or not
    boolean ValidateSubmitButton()
    {
        if(InitialAddress.isEmpty() ||
                Destination.isEmpty()  ||
                NameTextInput.getText().toString().isEmpty()  ||
                PhoneNumber_.getText().toString().isEmpty()  ||
                EmailTextInput.getText().toString().isEmpty() ||
                LeavingTimeTextInput.getText().toString().isEmpty())
        {
            return false;
        }
        return true;

    }

    //This function get the location of the user by default (when the activity is run at first)
    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {

                if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    criteria = new Criteria();
                    bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

                    Location location = locationManager.getLastKnownLocation(bestProvider);
                    if (location != null) {
                        // placeAutocompleteFragment1.setText(getPlace(location));//location.toString());
                        //InitialAddress = getPlace(location);
                        return getPlace(location);
                    } else {
                        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
                        return "";
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
                    return "";
                }
            }

            @Override
            protected void onPostExecute(String location) {
                super.onPostExecute(location);
                placeAutocompleteFragment1.setText(location);
                InitialAddress = location;

            }
        }.execute();


    }

    //This function for location permission ask
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {
                Toast.makeText(this, "Until you grant the permission, we can not display the location", Toast.LENGTH_SHORT).show();
            }
        }

    }
    //This function update the current location for case that the user walking
    void showSunTimes( double lat, double lng)
    {
        Date date = new Date();// date of calculation

        SunTimes times = SunTimes.compute()
                .on(date)       // set a date
                .at(lat, lng)   // set a location
                .execute();     // get the results
        System.out.println("Sunrise: " + times.getRise());
        System.out.println("Sunset: " + times.getSet());
    }

    //This function return the string of location
    public String getPlace(Location location) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


            if (addresses.size() > 0) {
                initialLocationLongitude_ =location.getLatitude();
                initialLocationLongitude_ = location.getLongitude();
                destinetionCityName_ =  addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                InitialAddress = addresses.toString();
                String result = "";
                if(stateName != null)
                    result+=stateName;
                if(destinetionCityName_ != null)
                    result += " "+destinetionCityName_;
                if(countryName != null)
                    result+= " "+ countryName;
                return stateName + " " + destinetionCityName_ + " " + countryName;
            }

            return "";
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }


    private void getCityName(double longitude, double latitude){
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
               destinetionCityName_ = addresses.get(0).getLocality();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}







