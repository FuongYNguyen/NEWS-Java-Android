package com.example.data;

import android.os.Bundle;
import android.view.MenuItem;

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

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load HomeFragment khi Activity bắt đầu
        loadFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if(itemId == R.id.navigation_home){
                    selectedFragment = new HomeFragment();
                }else if (itemId == R.id.navigation_dashboard) {
                    selectedFragment = new CategoryFragment();
                }else if (itemId == R.id.navigation_notifications) {
                    selectedFragment = new SearchFragment();
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
}


//package com.example.data;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.util.ArrayList;
//
//public class MainActivity extends AppCompatActivity {
//    RecyclerView recyclerView;
//    FloatingActionButton add_button;
//    MyDataHelper myDataHelper;
//    ArrayList<String> news_id, news_title, news_content;
//    CustomAdapter customAdapter;
//    BottomNavigationView bottomNavigationView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        add_button = findViewById(R.id.add_button);
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
//
//        // Sử dụng setOnItemSelectedListener thay vì setOnNavigationItemSelectedListener
//        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                int itemId =item.getItemId();
//                if(itemId == R.id.navigation_home){
//                    return true;
//                } else if (itemId == R.id.navigation_dashboard) {
//                    Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
//                    startActivity(intent);
//                } else if (itemId == R.id.navigation_notifications){
//                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                    startActivity(intent);
//                }
//                return false;
//            }
//        });
//
//        add_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, AddActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        myDataHelper = new MyDataHelper(MainActivity.this);
//        news_id = new ArrayList<>();
//        news_title = new ArrayList<>();
//        news_content = new ArrayList<>();
//
//        storeDataInArrays();
//
//        customAdapter = new CustomAdapter(MainActivity.this, news_id, news_title, news_content);
//        recyclerView.setAdapter(customAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//    }
//
//    void storeDataInArrays() {
//        Cursor cursor = myDataHelper.readAllData("news");
//        if (cursor.getCount() == 0) {
//            Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT).show();
//        } else {
//            while (cursor.moveToNext()) {
//                news_id.add(cursor.getString(0));
//                news_title.add(cursor.getString(1));
//                news_content.add(cursor.getString(2));
//            }
//        }
//    }
//}
