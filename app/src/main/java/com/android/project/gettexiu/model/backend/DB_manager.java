package com.android.project.gettexiu.model.backend;

import android.content.ContentValues;

public interface DB_manager {

    //------------------------------- Functions -------------------------------//
    public String addNewTravel(ContentValues travel);

    public boolean checkIfTravelAdded(String id);


}
