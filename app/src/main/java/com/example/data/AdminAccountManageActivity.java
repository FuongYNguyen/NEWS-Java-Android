package com.example.data;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminAccountManageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyDataHelper myDataHelper;
    ArrayList<String> userId, userEmail, userRole;
    ArrayList<String> newsId = new ArrayList<>();  // Empty list
    ArrayList<String> newsTitle = new ArrayList<>();  // Empty list
    ArrayList<String> newsContent = new ArrayList<>();  // Empty list
    ArrayList<String> newsImage = new ArrayList<>();  // Empty list
    ArrayList<String> newsSc = new ArrayList<>();  // Empty list
    ArrayList<String> newsCategoryId = new ArrayList<>();  // Empty list
    CustomAdapter customAdapter;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_account_manage);

            recyclerView = findViewById(R.id.recyclerView);

            myDataHelper = new MyDataHelper(this);
            userId = new ArrayList<>();
            userEmail = new ArrayList<>();
            userRole = new ArrayList<>();

            storeUserDataInArrays();

            customAdapter = new CustomAdapter(this, userId, userEmail, userRole, null, null, null, '4');
            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        void storeUserDataInArrays() {
            Cursor cursor = myDataHelper.readAllData("user");
            userId.clear();
            userEmail.clear();
            userRole.clear();

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    userId.add(cursor.getString(0));      // User ID
                    userEmail.add(cursor.getString(1));   // User Email
                    userRole.add(cursor.getString(4));    // Role ID
                }
            }
        }
    }