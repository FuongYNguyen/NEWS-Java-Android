package com.example.data;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private TextView articleTitleTextView, articleTextView, articleCategoryTextView;
    private ImageView articleThumbnailImageView;
    private LinearLayout commentSection;
    private EditText commentEditText;
    private Button submitCommentButton;
    private MyDataHelper myDataHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

        scrollView = findViewById(R.id.scrollView);
        articleTitleTextView = findViewById(R.id.articleTitleTextView);
        articleTextView = findViewById(R.id.articleTextView);
        articleCategoryTextView = findViewById(R.id.articleCategoryTextView);
        articleThumbnailImageView = findViewById(R.id.articleThumbnailImageView);
        commentSection = findViewById(R.id.commentSection);
        commentEditText = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.submitCommentButton);

        // Nhận news_id từ Intent
        String newsId = getIntent().getStringExtra("news_id");

        // Lấy thông tin bài viết từ CSDL
        if (newsId != null) {
            loadArticleDetails(newsId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin bài viết!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadArticleDetails(String newsId) {
        MyDataHelper myDataHelper = new MyDataHelper(this);
        Cursor cursor = myDataHelper.getbyNewsId(newsId);

        if (cursor != null && cursor.moveToFirst()) {

            String title = cursor.getString(1);
            String imageUrl = cursor.getString(2);
            String content = cursor.getString(4);



            articleTitleTextView.setText(title);
            articleTextView.setText(content);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this).load(imageUrl).into(articleThumbnailImageView);
            } else {
//                articleThumbnailImageView.setImageResource(R.drawable.placeholder_image); // Placeholder nếu không có ảnh
            }
        } else {
            Toast.makeText(this, "Không tìm thấy bài viết!", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

}
