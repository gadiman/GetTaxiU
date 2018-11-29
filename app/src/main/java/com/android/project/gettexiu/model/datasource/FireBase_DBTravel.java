package com.android.project.gettexiu.model.datasource;

import android.content.ContentValues;

import com.android.project.gettexiu.model.backend.DB_manager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FireBase_DBTravel  implements DB_manager {
    @Override
    public String addNewTravel(ContentValues travel) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference firebaseRef = database.getReference( "Travels" );

        //create a new key for this travel
        DatabaseReference TravelRef = firebaseRef.push();
        //Insert the object to the FireBase
        TravelRef.setValue(travel);
        //return the key for checking if the insert succeeded
        return TravelRef.push().getKey();
    }

    @Override
    public boolean checkIfTravelAdded(String id) {
        return id == FirebaseDatabase.getInstance().getReference("Travels").child(id).getKey();
    }


}
