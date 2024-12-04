package com.example.data;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AccountFragment extends Fragment {

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize the logout button
        Button logoutButton = view.findViewById(R.id.logoutButton);

        // Set up the logout button click listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear session data
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserSession", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(requireContext(), "Logout Successfully!", Toast.LENGTH_SHORT).show();

                // Navigate back to LoginFragment
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}
