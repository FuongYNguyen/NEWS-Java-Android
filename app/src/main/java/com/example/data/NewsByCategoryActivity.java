package com.example.data;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsByCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    ArrayList<String> news_id, news_title, news_content, news_image, news_sc, news_categoryId;
    MyDataHelper myDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_by_category);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Set layout manager for RecyclerView
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDataHelper = new MyDataHelper(this);
        // Kích hoạt nút Back
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(view -> finish());

        // Lấy categoryId từ Intent
        String categoryId = getIntent().getStringExtra("category_id");

        // Lấy danh sách các bài viết theo categoryId
        loadNewsByCategory(categoryId);

        if (news_id.size() > 0) {
            customAdapter = new CustomAdapter(this, news_id, news_title, news_content, news_image, news_sc, news_categoryId, '5');
            recyclerView.setAdapter(customAdapter);
        } else {
            // Nếu không có dữ liệu, hiển thị thông báo
            Toast.makeText(this, "No news found for this category", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNewsByCategory(String categoryId) {
        Cursor cursor = myDataHelper.getNewsByCategory(categoryId); // Truy vấn bài viết theo categoryId
        news_id = new ArrayList<>();
        news_title = new ArrayList<>();
        news_content = new ArrayList<>();
        news_sc = new ArrayList<>();
        news_categoryId = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                news_id.add(cursor.getString(0));  // news_id
                news_title.add(cursor.getString(1)); // title
                news_content.add(cursor.getString(4));  // content
                news_sc.add(cursor.getString(3));  // short description
                news_categoryId.add(cursor.getString(5));  // categoryId
            }
        }
        cursor.close();  // Đảm bảo đóng cursor khi hoàn tất truy vấn
    }
}
