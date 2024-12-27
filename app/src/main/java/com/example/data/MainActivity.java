package com.example.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TinChuan.net");
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load HomeFragment when Activity starts
        loadFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.navigation_dashboard) {
                    selectedFragment = new CategoryFragment();
                } else if (itemId == R.id.navigation_notifications) {
                    selectedFragment = new SearchFragment();
                } else if (itemId == R.id.navigation_account) {
                    if (isUserLoggedIn()) {
                        // Fetch roleId from SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                        int roleId = sharedPreferences.getInt("roleId", -1);
                        // Check the roleId and navigate accordingly
                        if (roleId == 1) {
                            selectedFragment = new AdminAccountFragment(); // Admin role
                        } else if (roleId == 2) {
                            selectedFragment = new UserAccountFragment(); // Regular user role
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid Role. Please contact support.", Toast.LENGTH_SHORT).show();
                            return true; // Prevent fragment loading
                        }
                    } else {
                        // Redirect to LoginActivity if not logged in
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return true; // Return early to prevent fragment loading
                    }
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}
