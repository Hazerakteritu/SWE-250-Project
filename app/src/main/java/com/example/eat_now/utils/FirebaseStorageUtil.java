package com.example.eat_now.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FirebaseStorageUtil {
    private static final String TAG = "FirebaseStorageUtil";
    
    public interface OnImageUploadListener {
        void onSuccess(String downloadUrl);
        void onFailure(Exception e);
    }
    
    /**
     * Upload an image to Firebase Storage and get the download URL
     * 
     * @param context The context
     * @param imageUri The URI of the image to upload
     * @param folderName The folder name in Firebase Storage (e.g., "restaurants", "food_items")
     * @param listener Callback for success or failure
     */
    public static void uploadImage(Context context, Uri imageUri, String folderName, OnImageUploadListener listener) {
        if (imageUri == null) {
            listener.onFailure(new IllegalArgumentException("Image URI cannot be null"));
            return;
        }
        
        // Create a unique filename
        String filename = UUID.randomUUID().toString();
        
        // Get reference to Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        
        // Create a reference to the image file
        StorageReference imageRef = storageRef.child(folderName + "/" + filename);
        
        // Upload the file
        UploadTask uploadTask = imageRef.putFile(imageUri);
        
        // Show a toast message
        Toast.makeText(context, "Uploading image...", Toast.LENGTH_SHORT).show();
        
        // Get the download URL after upload completes
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            
            // Continue with the task to get the download URL
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                String downloadUrl = downloadUri.toString();
                Log.d(TAG, "✅ Image upload successful. URL: " + downloadUrl);
                listener.onSuccess(downloadUrl);
            } else {
                Exception e = task.getException();
                if (e != null && e.getMessage() != null && e.getMessage().contains("Object does not exist")) {
                    Log.e(TAG, "❌ Object does not exist at location. Possible reasons: bad path or upload failed.");
                    Toast.makeText(context, "❌ Error: Image not found in storage.", Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "❌ Upload failed: " + e.getMessage());
                    Toast.makeText(context, "❌ Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                listener.onFailure(e);
            }
        });

    }
}