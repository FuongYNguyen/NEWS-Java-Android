package com.example.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserAccountFragment extends Fragment {
    public UserAccountFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_account, container, false);

        // Initialize the logout button
        Button logoutButton = view.findViewById(R.id.logoutButton);
        // Find the TextView
        TextView emailTextView = view.findViewById(R.id.emailTextView);

        // Retrieve the email from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "Guest");
        int roleId = sharedPreferences.getInt("roleId", -1);
        // Set the email to the TextView
        emailTextView.setText(userEmail);
        // Set up the logout button click listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear session data
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clear all stored preferences
                editor.apply();

                // Show a success message
                Toast.makeText(requireContext(), "Logout Successfully!", Toast.LENGTH_SHORT).show();

                // Navigate to LoginActivity
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                // Clear back stack to prevent navigation back to this fragment
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}