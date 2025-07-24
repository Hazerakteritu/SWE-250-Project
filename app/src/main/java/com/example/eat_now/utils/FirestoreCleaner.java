package com.example.eat_now.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


// Clear existing data
public class FirestoreCleaner {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static CollectionReference getRestaurantsCollection() {
        return db.collection("restaurants");
    }
    public static void clearExistingData(Context context, Runnable onComplete) {
        FirestoreUtil.getRestaurantsCollection().get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        doc.getReference().delete();
                    }

                    FirestoreUtil.getFoodItemsCollection().get()
                            .addOnSuccessListener(foodSnapshot -> {
                                for (DocumentSnapshot doc : foodSnapshot.getDocuments()) {
                                    doc.getReference().delete();
                                }
                                if (onComplete != null) onComplete.run();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("FirestoreCleaner", "Error clearing food items: " + e.getMessage());
                                showToast(context, "Error clearing food items: " + e.getMessage());
                                if (onComplete != null) onComplete.run();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreCleaner", "Error clearing restaurants: " + e.getMessage());
                    showToast(context, "Error clearing restaurants: " + e.getMessage());
                    if (onComplete != null) onComplete.run();
                });
    }

    private static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
