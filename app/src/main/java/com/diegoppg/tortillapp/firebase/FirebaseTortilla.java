package com.diegoppg.tortillapp.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.diegoppg.tortillapp.interfaces.ITortillaAPI;
import com.diegoppg.tortillapp.modelo.Tortilla;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FirebaseTortilla implements ITortillaAPI {
    @Override
    public void addTortilla(String name, double latitude, double longitude) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new tortilla
        Map<String, Object> tortilla = new HashMap<>();
        tortilla.put("name", name);
        tortilla.put("latitude", latitude);
        tortilla.put("longitude", longitude);

        db.collection("tortillas")
                .add(tortilla)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    public CompletableFuture<List<Tortilla>> listTortillas() {
        CompletableFuture<List<Tortilla>> future = new CompletableFuture<>();

        // Access Firestore with a custom converter
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tortillaRef = db.collection("tortillas");


        tortillaRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                List<Tortilla> tortillas = new ArrayList<>();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        Tortilla tortilla = document.toObject(Tortilla.class);
                        tortillas.add(tortilla);
                    }
                }
                future.complete(tortillas);
            } else {
                future.completeExceptionally(new Exception("Error listLands", task.getException()));
            }
        });

        return future;
    }
}
