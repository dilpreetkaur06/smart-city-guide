package com.guide.smartcity;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;

public class MenuHelper {

    public static void setupMenu(Activity activity, TextView menuIcon) {

        if (menuIcon == null) return;

        menuIcon.setOnClickListener(v -> {

            PopupMenu popup = new PopupMenu(activity, menuIcon);

            // 🔹 Common options
            popup.getMenu().add("Contact");
            popup.getMenu().add("Dashboard");

            // 🔥 Dynamic Login / Logout
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                popup.getMenu().add("Login");
            } else {
                popup.getMenu().add("Logout");
            }

            popup.setOnMenuItemClickListener(item -> {

                String title = item.getTitle().toString();

                switch (title) {

                    case "Contact":

                        activity.startActivity(
                                new Intent(activity, ContactInfo.class)
                        );
                        break;

                    case "Dashboard":

                        activity.startActivity(
                                new Intent(activity, DashboardActivity.class)
                        );

                        break;

                    case "Login":

                        activity.startActivity(
                                new Intent(activity, LoginActivity.class)
                        );
                        break;

                    case "Logout":

                        FirebaseAuth.getInstance().signOut();

                        Toast.makeText(activity,
                                "Logged out successfully",
                                Toast.LENGTH_SHORT).show();

                        break;
                }

                return true;
            });

            popup.show();
        });
    }
}