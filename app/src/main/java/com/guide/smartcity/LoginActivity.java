package com.guide.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin;
    private TextView tvSignUpLink,tvForgotPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ IMPORTANT: This is Welcome Back screen
        setContentView(R.layout.activity_login);
        View header = findViewById(R.id.headerCommon);

        if (header != null) {

            TextView menuIcon = header.findViewById(R.id.tvMenuIcon);

            MenuHelper.setupMenu(this, menuIcon);
        }
        mAuth = FirebaseAuth.getInstance();
        // 🔹 Initialize views
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // 🔹 Login button
        btnLogin.setOnClickListener(v -> validateAndLogin());

        // 🔹 Go to Join Us (Sign Up screen)
        tvSignUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
            startActivity(intent);
        });
        tvForgotPassword.setOnClickListener(v -> {

            String email =
                    etLoginEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {

                etLoginEmail.setError(
                        "Enter your email first"
                );

                etLoginEmail.requestFocus();

                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Password reset email sent",
                                    Toast.LENGTH_LONG
                            ).show();

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Failed: " +
                                            task.getException().getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });
    }

    private void validateAndLogin() {
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        // ✅ Keep your original validations
        if (TextUtils.isEmpty(email)) {
            etLoginEmail.setError("Email is required");
            etLoginEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etLoginEmail.setError("Enter valid email");
            etLoginEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etLoginPassword.setError("Password is required");
            etLoginPassword.requestFocus();
            return;
        }

        // 🔥 NEW: Firebase Real Login Check
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        if (mAuth.getCurrentUser() != null
                                && mAuth.getCurrentUser().isEmailVerified()) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Welcome back!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            startActivity(
                                    new Intent(
                                            LoginActivity.this,
                                            MainActivity.class
                                    )
                            );

                            finish();

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Please verify your email first",
                                    Toast.LENGTH_LONG
                            ).show();

                            FirebaseAuth.getInstance().signOut();
                        }

                    } else {

                        String error =
                                task.getException() != null
                                        ? task.getException().getMessage()
                                        : "Login Failed";

                        if (error.contains("no user record")) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Account doesn't exist. Please Sign Up!",
                                    Toast.LENGTH_LONG
                            ).show();

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Error: " + error,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }
}