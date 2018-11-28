package com.android.project.gettexiu.model.backend;

import com.android.project.gettexiu.model.datasource.FireBase_DBTravel;

public class FactoryMethod {
    static DB_manager manager = null ;

    public static DB_manager getManager() {
        if (manager == null)
            manager = new FireBase_DBTravel();

        return manager;
    }
}
