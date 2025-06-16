package com.example.eat_now.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.eat_now.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * FIXED Forgot Password Activity - No crashes!
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_forgot_password);

            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();

            // Set up toolbar - WITH ERROR HANDLING
            try {
                Toolbar toolbar = findViewById(R.id.toolbar);
                if (toolbar != null) {
                    setSupportActionBar(toolbar);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setTitle("Reset Password");
                    }
                }
            } catch (Exception e) {
                // Ignore toolbar errors
            }

            // Initialize views - WITH NULL CHECKS
            emailEditText = findViewById(R.id.email_edit_text);
            resetPasswordButton = findViewById(R.id.reset_password_button);
            progressBar = findViewById(R.id.progress_bar);

            // Set click listener - WITH NULL CHECK
            if (resetPasswordButton != null) {
                resetPasswordButton.setOnClickListener(v -> resetPassword());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error loading screen", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Send password reset email - SAFE VERSION
     */
    private void resetPassword() {
        try {
            if (emailEditText == null) return;

            String email = emailEditText.getText().toString().trim();

            // Validate email
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Please enter a valid email");
                emailEditText.requestFocus();
                return;
            }

            // Show loading - SAFE
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            if (resetPasswordButton != null) {
                resetPasswordButton.setEnabled(false);
                resetPasswordButton.setText("Sending...");
            }

            // Send email - WITH ERROR HANDLING
            if (mAuth != null) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            try {
                                // Hide loading
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                if (resetPasswordButton != null) {
                                    resetPasswordButton.setEnabled(true);
                                    resetPasswordButton.setText("Send Reset Email");
                                }

                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            "✅ Reset email sent to " + email,
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            "❌ Failed to send reset email",
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        "Error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
