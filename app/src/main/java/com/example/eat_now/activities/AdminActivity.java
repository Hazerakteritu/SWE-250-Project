package com.example.eat_now.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.eat_now.R;
import com.example.eat_now.utils.FirebaseStorageUtil;
import com.example.eat_now.utils.RestaurantFirestoreUtil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Spinner restaurantSpinner;
    private ImageView restaurantImageView;
    private Button selectImageButton, uploadImageButton, addSampleDataButton;
    private ProgressBar progressBar;
    private EditText restaurantNameEdit, restaurantAddressEdit, restaurantDescEdit;

    private Uri selectedImageUri;
    private FirebaseFirestore db;
    private List<String> restaurantIds;
    private List<String> restaurantNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = FirebaseFirestore.getInstance();
        restaurantIds = new ArrayList<>();
        restaurantNames = new ArrayList<>();

        initializeViews();
        loadRestaurants();
        setClickListeners();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Restaurant Admin");
        }

        restaurantSpinner = findViewById(R.id.restaurant_spinner);
        restaurantImageView = findViewById(R.id.restaurant_image_view);
        selectImageButton = findViewById(R.id.select_image_button);
        uploadImageButton = findViewById(R.id.upload_image_button);
        addSampleDataButton = findViewById(R.id.add_sample_data_button);
        progressBar = findViewById(R.id.progress_bar);
        restaurantNameEdit = findViewById(R.id.restaurant_name_edit);
        restaurantAddressEdit = findViewById(R.id.restaurant_address_edit);
        restaurantDescEdit = findViewById(R.id.restaurant_desc_edit);
    }

    private void loadRestaurants() {
        progressBar.setVisibility(View.VISIBLE);
        
        db.collection("restaurants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    restaurantIds.clear();
                    restaurantNames.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        restaurantIds.add(document.getId());
                        restaurantNames.add(document.getString("name"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, restaurantNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    restaurantSpinner.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to load restaurants: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setClickListeners() {
        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        uploadImageButton.setOnClickListener(v -> {
            if (selectedImageUri != null && restaurantSpinner.getSelectedItemPosition() >= 0) {
                uploadRestaurantImage();
            } else {
                Toast.makeText(this, "Please select a restaurant and image", Toast.LENGTH_SHORT).show();
            }
        });

        addSampleDataButton.setOnClickListener(v -> {
            RestaurantFirestoreUtil.addSampleData(this);
            // Reload restaurants after adding sample data
            new android.os.Handler().postDelayed(this::loadRestaurants, 3000);
        });
    }

    private void uploadRestaurantImage() {
        progressBar.setVisibility(View.VISIBLE);
        uploadImageButton.setEnabled(false);

        int selectedPosition = restaurantSpinner.getSelectedItemPosition();
        String restaurantId = restaurantIds.get(selectedPosition);

        FirebaseStorageUtil.uploadImage(this, selectedImageUri, "restaurant_images", 
                new FirebaseStorageUtil.OnImageUploadListener() {
                    @Override
                    public void onSuccess(String downloadUrl) {
                        // Update restaurant document with image URL
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("imageUrl", downloadUrl);

                        db.collection("restaurants").document(restaurantId)
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    progressBar.setVisibility(View.GONE);
                                    uploadImageButton.setEnabled(true);
                                    Toast.makeText(AdminActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                                    
                                    // Display the uploaded image
                                    Glide.with(AdminActivity.this)
                                            .load(downloadUrl)
                                            .into(restaurantImageView);
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    uploadImageButton.setEnabled(true);
                                    Toast.makeText(AdminActivity.this, "Failed to update restaurant: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        progressBar.setVisibility(View.GONE);
                        uploadImageButton.setEnabled(true);
                        Toast.makeText(AdminActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            restaurantImageView.setImageURI(selectedImageUri);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}