package com.example.data;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.data.MainActivity;
import com.example.data.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    MyDataHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new MyDataHelper(requireContext());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                    if (checkCredentials) {
                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true); // Set login status to true
                        editor.putString("email", email); // Save a string value (optional)
                        editor.apply();

                        Intent intent = new Intent(requireActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(requireContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the SignupActivity
                Intent intent = new Intent(requireActivity(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
