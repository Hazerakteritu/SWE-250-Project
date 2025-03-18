package com.example.eat_now.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.eat_now.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ViewProfileFragment extends Fragment {

    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, addressEditText;
    private Button saveChangesButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.view_profile_fragment, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        firstNameEditText = root.findViewById(R.id.first_name_edit_text);
        lastNameEditText = root.findViewById(R.id.last_name_edit_text);
        emailEditText = root.findViewById(R.id.email_edit_text);
        phoneEditText = root.findViewById(R.id.phone_edit_text);
        addressEditText = root.findViewById(R.id.address_edit_text);
        saveChangesButton = root.findViewById(R.id.save_changes_button);

        // Load user data
        loadUserData();

        // Set up save changes button
        saveChangesButton.setOnClickListener(v -> saveUserData());

        return root;
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Set email from Firebase Auth
            emailEditText.setText(user.getEmail());
            emailEditText.setEnabled(false); // Email cannot be changed

            // Get additional user data from Firestore
            DocumentReference userRef = db.collection("users").document(user.getUid());
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String firstName = documentSnapshot.getString("firstName");
                    String lastName = documentSnapshot.getString("lastName");
                    String phone = documentSnapshot.getString("phone");
                    String address = documentSnapshot.getString("address");

                    if (firstName != null) firstNameEditText.setText(firstName);
                    if (lastName != null) lastNameEditText.setText(lastName);
                    if (phone != null) phoneEditText.setText(phone);
                    if (address != null) addressEditText.setText(address);
                }
            });
        }
    }

    private void saveUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();

            // Validate inputs
            if (firstName.isEmpty()) {
                firstNameEditText.setError("First name is required");
                firstNameEditText.requestFocus();
                return;
            }

            if (lastName.isEmpty()) {
                lastNameEditText.setError("Last name is required");
                lastNameEditText.requestFocus();
                return;
            }

            // Create user data map
            Map<String, Object> userData = new HashMap<>();
            userData.put("firstName", firstName);
            userData.put("lastName", lastName);
            userData.put("phone", phone);
            userData.put("address", address);

            // Update user data in Firestore
            DocumentReference userRef = db.collection("users").document(user.getUid());
            userRef.set(userData)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(getActivity(), "Failed to update profile: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show());
        }
    }
}