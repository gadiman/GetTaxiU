package com.cohen.gad.gettexiu.model.datasource;

import android.content.ContentValues;

import com.cohen.gad.gettexiu.model.backend.DB_manager;
import com.cohen.gad.gettexiu.model.entities.Travel;

import java.util.ArrayList;
import java.util.List;

import static com.cohen.gad.gettexiu.model.backend.TravelConst.*;

public class List_DBTravel implements DB_manager {

    //---------------------------------- Fields ------------------------------------------//
    static List<Travel> travels;

    //---------------------------------- Constructors ------------------------------------//

    static {
        travels = new ArrayList<>();
    }

    //---------------------------------- Functions ------------------------------------//

    @Override
    public void addNewTravel(ContentValues travel) {

        Travel travel_ = ContentValuesToTravel(travel);
        travels.add(travel_);
    }
}
