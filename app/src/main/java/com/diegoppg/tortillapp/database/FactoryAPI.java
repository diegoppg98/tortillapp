package com.diegoppg.tortillapp.database;

import static android.provider.Settings.System.getString;


import android.util.Log;

import com.diegoppg.tortillapp.R;

public class FactoryAPI {
    public static AbstractFactoryAPI getFactoryAPI(String type) {

        if (type.equalsIgnoreCase("firebase")){
            return new FirebaseFactoryAPI();
        }

        else{
            //return by default
            return new FirebaseFactoryAPI();
        }
    }
}
