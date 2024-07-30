package com.diegoppg.tortillapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

        readDataFirebase();
    }







      //  FragmentContainerView navHost = findViewById(R.id.nav_host);

      //  NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);

        //NavController navController = navHostFragment.getNavController();
       //NavController navController = Navigation.findNavController(this, R.id.nav_host);








    private void addDataFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String TAG  = "addData";

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


/*

        Firestore db = FirestoreOptions.getDefaultInstance().getService();

        // Example user
        Usuario user = new Usuario(null, "example@example.com", "Example Name", "123 Example St", "path/to/photo.jpg");

        // Convert to Firestore format and add to Firestore
        Map<String, Object> userMap = UsuarioConverter.toFirestore(user);
        ApiFuture<DocumentReference> addedDocRef = db.collection("usuarios").add(userMap);
        System.out.println("Added document with ID: " + addedDocRef.get().getId());

        // Fetch a document and convert back to Usuario
        DocumentReference docRef = db.collection("usuarios").document("some-document-id");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Usuario fetchedUser = UsuarioConverter.fromFirestore(document);

        if (fetchedUser != null) {
            System.out.println("Fetched user: " + fetchedUser.getNombre());
        }
        */
    }

    private void readDataFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String TAG  = "readData";


        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                Log.d(TAG, user.first);
                                Log.d(TAG, user.last);
                                Log.d(TAG, String.valueOf(user.born));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public class User {
        public String first;
        public String last;
        public int born;
    }

    public class UsuarioConverter {

        public Map<String, Object> toFirestore(Usuario usuario) {
            Map<String, Object> data = new HashMap<>();
            data.put("correo", usuario.correo);
            data.put("nombre", usuario.nombre);
            data.put("foto", usuario.foto);
            return data;
        }

        public class Usuario {
            private String id;
            private String correo;
            private String nombre;
            private String foto;

            public Usuario(String id, String correo, String nombre, String foto) {
                this.id = id;
                this.correo = correo;
                this.nombre = nombre;
                this.foto = foto;
            }

            // Add getters and setters if needed
        }

        public Usuario fromFirestore(DocumentSnapshot snapshot) {
            if (snapshot.exists()) {
                Map<String, Object> data = snapshot.getData();
                String id = snapshot.getId();
                String correo = (String) data.get("correo");
                String nombre = (String) data.get("nombre");
                String foto = (String) data.get("foto");
                return new Usuario(id, correo, nombre, foto);
            }
            return null;
        }
    }


}