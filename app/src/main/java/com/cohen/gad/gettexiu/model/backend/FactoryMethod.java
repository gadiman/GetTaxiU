package com.cohen.gad.gettexiu.model.backend;

import com.cohen.gad.gettexiu.model.datasource.List_DBTravel;

public class FactoryMethod {
    static DB_manager manager = null ;

    public static DB_manager getManager() {
        if (manager == null)
            manager = new List_DBTravel();

        return manager;
    }
}
