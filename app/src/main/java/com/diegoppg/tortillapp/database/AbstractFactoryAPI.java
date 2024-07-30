package com.diegoppg.tortillapp.database;

import com.diegoppg.tortillapp.interfaces.ITortillaAPI;
import com.diegoppg.tortillapp.interfaces.IUserAPI;

public interface AbstractFactoryAPI {

    /**
     * Gets a API of tortillas
     */
    ITortillaAPI getTortilla();
    /**
     * Gets a API of users
     */
    IUserAPI getUser();

}
