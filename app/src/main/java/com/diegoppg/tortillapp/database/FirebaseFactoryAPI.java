package com.diegoppg.tortillapp.database;

import com.diegoppg.tortillapp.firebase.FirebaseTortilla;
import com.diegoppg.tortillapp.firebase.FirebaseUserProfile;
import com.diegoppg.tortillapp.interfaces.ITortillaAPI;
import com.diegoppg.tortillapp.interfaces.IUserAPI;

public class FirebaseFactoryAPI implements AbstractFactoryAPI {
    @Override
    public ITortillaAPI getTortilla() {
        return new FirebaseTortilla();
    }

    @Override
    public IUserAPI getUser() {
        return new FirebaseUserProfile();
    }
}
