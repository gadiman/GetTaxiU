package com.android.project.gettexiu.model.backend;

import android.content.ContentValues;

public interface DB_manager {

    //------------------------------- Functions -------------------------------//
    public void addNewTravel(ContentValues travel);
    public boolean checkIfTravelAdded(String id);
}
