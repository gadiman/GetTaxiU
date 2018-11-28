package com.android.project.gettexiu.model.datasource;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.android.project.gettexiu.model.backend.DB_manager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.android.project.gettexiu.model.backend.TravelConst.TravelC.CUSTOMER_PHONE;


public class FireBase_DBTravel  implements DB_manager {
    @Override
    public void addNewTravel(ContentValues travel) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference firebaseRef = database.getReference( "gettexiu-ea883" );
        firebaseRef.child( travel.getAsString( CUSTOMER_PHONE ) ).setValue( travel );
    }

    @Override
    public boolean checkIfTravelAdded(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference firebaseRef = database.getReference( "gettexiu-ea883" );
        return false;
    }


}
