package com.diegoppg.tortillapp.interfaces;

import java.util.concurrent.CompletableFuture;

public interface IUserAPI {

    CompletableFuture<String> login(String email, String password);

    CompletableFuture<String> signInWithGoogle(String idToken);

    void logout();

    void userInformation();

    boolean userAutenticated();
}
