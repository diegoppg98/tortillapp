package com.diegoppg.tortillapp.database.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.diegoppg.tortillapp.R;
import com.diegoppg.tortillapp.database.FactoryAPI;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserLogin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "UserLogin";


    public UserLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserLogin.
     */
    // TODO: Rename and change types and number of parameters
    public static UserLogin newInstance(String param1, String param2) {
        UserLogin fragment = new UserLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SignInButton btSignIn = requireView().findViewById(R.id.sign_in_button);

        // Button listeners
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        CredentialManager credentialManager = CredentialManager.create(requireContext());
        GetSignInWithGoogleOption googleSignInOption = new GetSignInWithGoogleOption.Builder(getString(R.string.default_web_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleSignInOption)
                .build();

        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(() -> {
            Log.d(TAG, "Preparing credentials with Google was cancelled.");
            Toast.makeText(requireContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
        });

        credentialManager.getCredentialAsync(
                requireActivity(),
                request,
                cancellationSignal,
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignIn(result);
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        Logger logger = Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
                        logger.log(Level.SEVERE, "Error getting (or preparing) credential: " + e.getMessage());
                    }
                });
    }

    public void handleSignIn(GetCredentialResponse result) {
        Credential credential = result.getCredential();

        if (credential instanceof CustomCredential && GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(((CustomCredential) credential).getData());

            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
        } else {
            Log.d(TAG, "handleSignIn: credential is not a CustomCredential");
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        FactoryAPI.getFactoryAPI(getString(R.string.database)).getUser().signInWithGoogle(idToken).thenAccept(user -> {
            // Handle successful sign in
            Log.d(TAG,"signInWithCredential:success, user: " + idToken);
            Toast.makeText(requireContext(), "Sesion iniciada correctamente.", Toast.LENGTH_SHORT).show();
            loadUserProfile();

        }).exceptionally(e -> {
            // Handle sign in failure
            Log.d(TAG,"signInWithCredential:failure, error: " + e.getMessage());
            return null;
        });
    }

    private void loadUserProfile() {
        Fragment fragment = new UserProfile();
        FragmentManager fragmentManager = requireActivity()
                .getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}