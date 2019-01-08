package com.android.project.gettexiu.model.datasource;

import android.content.ContentValues;

import com.android.project.gettexiu.model.backend.DB_manager;
import com.android.project.gettexiu.model.entities.Travel;

import java.util.ArrayList;
import java.util.List;

import static com.android.project.gettexiu.model.backend.TravelConst.ContentValuesToTravel;

public class List_DBTravel implements DB_manager {

    //---------------------------------- Fields ------------------------------------------//
    static List<Travel> travels;

    //---------------------------------- Constructors ------------------------------------//

    static {
        travels = new ArrayList<>();
    }

    //---------------------------------- Functions ------------------------------------//

    @Override
    public String addNewTravel(Travel travel) {

        travels.add(travel);
        return travel.getCustomerPhoneNumber();
    }

    @Override
    public boolean checkIfTravelAdded(String Phone) {
        for (Travel it:travels) {
            if (it.getCustomerPhoneNumber() == Phone)
                return true;
        }
        return false;
    }
}
