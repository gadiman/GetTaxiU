package com.cohen.gad.gettexiu.model.backend;

import android.content.ContentValues;

public interface DB_manager {

    //------------------------------- Functions -------------------------------//
    public void addNewTravel(ContentValues travel);
    public void removeTravel(ContentValues travel);
}
