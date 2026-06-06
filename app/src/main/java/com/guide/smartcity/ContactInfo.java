package com.guide.smartcity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ContactInfo extends AppCompatActivity {

    // Declaring UI components[cite: 2]
    private EditText etName, etEmail, etSubject, etMessage;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Linking to your specific layout file
        setContentView(R.layout.contact_info);
        View header = findViewById(R.id.headerCommon);

        if (header != null) {

            TextView menuIcon = header.findViewById(R.id.tvMenuIcon);

            MenuHelper.setupMenu(this, menuIcon);
        }
        // 1. Initializing Views using the IDs from your XML[cite: 2, 3]
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etSubject = findViewById(R.id.etSubject);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        // 2. Setting up the Click Listener for the "Send Message" Button[cite: 2]
        btnSend.setOnClickListener(v -> {
            handleFormSubmission();
        });
    }

    /**
     * Logic to validate and "send" the contact form[cite: 2]
     */
    private void handleFormSubmission() {
        // Retrieve and trim input text[cite: 2]
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        // Basic Validation[cite: 2]
        if (name.isEmpty() || email.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Optional: Email format check
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        } else {
            // Success logic[cite: 2]
            // In a real app, you would send this data to a server or Firebase
            Toast.makeText(this, "Message Sent Successfully!", Toast.LENGTH_LONG).show();

            // Clear the form fields after successful "sending"[cite: 2]
            etName.setText("");
            etEmail.setText("");
            etSubject.setText("");
            etMessage.setText("");
        }
    }
}