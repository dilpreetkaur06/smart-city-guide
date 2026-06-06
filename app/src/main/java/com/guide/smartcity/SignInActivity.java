package com.guide.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.UserProfileChangeRequest;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword;
    private CheckBox cbAgree;
    private Button btnCreateAccount;
    private TextView tvSignInLink;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ IMPORTANT: This is JOIN US screen
        setContentView(R.layout.activity_sign_in);
        View header = findViewById(R.id.headerCommon);

        if (header != null) {

            TextView menuIcon = header.findViewById(R.id.tvMenuIcon);

            MenuHelper.setupMenu(this, menuIcon);
        }
        mAuth = FirebaseAuth.getInstance();
        // 🔹 Initialize views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbAgree = findViewById(R.id.cbAgree);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvSignInLink = findViewById(R.id.tvSignInLink);

        // 🔹 Create Account button
        btnCreateAccount.setOnClickListener(v -> validateAndRegister());

        // 🔹 Go to Login (Welcome Back)
        tvSignInLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void validateAndRegister() {
        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // ✅ Name validation
        if (TextUtils.isEmpty(name)) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }

        // ✅ Email validation
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter valid email");
            etEmail.requestFocus();
            return;
        }

        // ✅ Password validation
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password required");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Min 6 characters");
            etPassword.requestFocus();
            return;
        }

        // ✅ Checkbox validation
        if (!cbAgree.isChecked()) {
            Toast.makeText(this, "Agree to Terms first", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        UserProfileChangeRequest profileUpdates =
                                new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .updateProfile(profileUpdates);
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .sendEmailVerification()
                                .addOnCompleteListener(task1 -> {

                                    if (task1.isSuccessful()) {

                                        Toast.makeText(SignInActivity.this,
                                                "Verification email sent!",
                                                Toast.LENGTH_LONG).show();

                                        startActivity(
                                                new Intent(
                                                        SignInActivity.this,
                                                        LoginActivity.class
                                                )
                                        );

                                        finish();

                                    } else {

                                        Toast.makeText(SignInActivity.this,
                                                "Email failed: " +
                                                        task1.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });



                    } else {
                        // Failure: Show the specific error (e.g., Email already exists)
                        String error = task.getException() != null ? task.getException().getMessage() : "Registration Failed";
                        Toast.makeText(SignInActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }
}