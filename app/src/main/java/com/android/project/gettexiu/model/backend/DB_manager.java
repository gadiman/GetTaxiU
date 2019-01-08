package com.android.project.gettexiu.model.backend;

import android.content.ContentValues;

import com.android.project.gettexiu.model.entities.Travel;

public interface DB_manager {

    //------------------------------- Functions -------------------------------//
    public String addNewTravel(Travel travel);

    public boolean checkIfTravelAdded(String id);


}
