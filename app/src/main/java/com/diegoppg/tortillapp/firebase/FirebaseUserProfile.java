package com.diegoppg.tortillapp.firebase;

import androidx.annotation.NonNull;
import com.diegoppg.tortillapp.interfaces.IUserAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.CompletableFuture;

public class FirebaseUserProfile implements IUserAPI {
    @Override
    public CompletableFuture<String> login(String email, String password) {
        return null;
    }

    public CompletableFuture<String> signInWithGoogle(String idToken) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CompletableFuture<String> future = new CompletableFuture<>();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            future.complete("user logged in");
                        } else {
                            // Sign in fails
                            future.completeExceptionally(task.getException());
                        }
                    }
                });
        return future;
    }

    @Override
    public void logout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();
    }

    @Override
    public void userInformation() {

    }

    @Override
    public boolean userAutenticated() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        return mAuth.getCurrentUser() != null;
    }
}
