package com.example.eat_now.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * FIXED Helper class for uploading restaurant images
 */
public class ImageUploadHelper {
    private static final String TAG = "ImageUploadHelper";
    public static final int PICK_IMAGE_REQUEST = 1001;

    /**
     * Open image picker to select restaurant image
     */
    public static void pickImageFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Upload selected image for a restaurant - FIXED VERSION
     */
    public static void uploadRestaurantImage(Activity activity, Uri imageUri, String restaurantId) {
        if (imageUri == null) {
            Toast.makeText(activity, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(activity, "Uploading restaurant image...", Toast.LENGTH_SHORT).show();

        // Create simple filename
        String filename = restaurantId + "_" + System.currentTimeMillis() + ".jpg";

        // Get Firebase Storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("restaurants/" + filename);

        // Upload file
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "Image uploaded successfully");

                    // Get download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                String downloadUrl = downloadUri.toString();
                                Log.d(TAG, "Download URL: " + downloadUrl);

                                // Update restaurant document
                                FirebaseFirestore.getInstance()
                                        .collection("restaurants")
                                        .document(restaurantId)
                                        .update("imageUrl", downloadUrl)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(activity, "✅ Restaurant image uploaded successfully!", Toast.LENGTH_LONG).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Failed to update restaurant document", e);
                                            Toast.makeText(activity, "❌ Failed to update restaurant: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to get download URL", e);
                                Toast.makeText(activity, "❌ Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Image upload failed", e);
                    Toast.makeText(activity, "❌ Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
