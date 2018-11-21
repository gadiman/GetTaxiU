package com.android.project.gettexiu.model.backend;

import android.content.ContentValues;

public interface DB_manager {

    //------------------------------- Functions -------------------------------//
    public void addNewTravel(ContentValues travel);

    public boolean checkIfTravelAdded(String id);

    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }
}
