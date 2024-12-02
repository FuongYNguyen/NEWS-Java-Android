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
import com.squareup.picasso.Picasso;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        scrollView = findViewById(R.id.scrollView);
        articleTitleTextView = findViewById(R.id.articleTitleTextView);
        articleTextView = findViewById(R.id.articleTextView);
        articleCategoryTextView = findViewById(R.id.articleCategoryTextView);
        articleThumbnailImageView = findViewById(R.id.articleThumbnailImageView);
        commentSection = findViewById(R.id.commentSection);
        commentEditText = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.submitCommentButton);
        myDataHelper = new MyDataHelper(this);

        // Nhận bài báo ID từ Intent
        Intent intent = getIntent();
        int newsId = intent.getIntExtra("news_id", -1);

        if (newsId != -1) {
            displayArticleDetails(newsId);
            displayComments(newsId);
        }

        submitCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEditText.getText().toString();
                if (!comment.isEmpty()) {
                    // Lưu bình luận vào cơ sở dữ liệu
                    saveComment(newsId, comment);

                    // Hiển thị bình luận mới
                    TextView commentTextView = new TextView(DetailActivity.this);
                    commentTextView.setText(comment + "\n(Mới)");
                    commentSection.addView(commentTextView);

                    commentEditText.setText("");
                    Toast.makeText(DetailActivity.this, "Bình luận đã được gửi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "Vui lòng nhập bình luận của bạn", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayArticleDetails(int newsId) {
        SQLiteDatabase db = myDataHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT n.*, c.name as category_name FROM " + MyDataHelper.TABLE_NEWS + " n " +
                "INNER JOIN " + MyDataHelper.TABLE_CATEGORY + " c ON n.categoryId = c.id WHERE n.id = ?", new String[]{String.valueOf(newsId)});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
            @SuppressLint("Range") String thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category_name"));

            articleTitleTextView.setText(title);
            articleTextView.setText(content);
            articleCategoryTextView.setText(category);

            if (thumbnail != null && !thumbnail.isEmpty()) {
                Picasso.get().load(thumbnail).into(articleThumbnailImageView);  // Using Picasso to load image from URL
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Không tìm thấy bài báo", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayComments(int newsId) {
        SQLiteDatabase db = myDataHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + MyDataHelper.TABLE_COMMENT + " WHERE newId = ?", new String[]{String.valueOf(newsId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String commentContent = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String dateCreated = cursor.getString(cursor.getColumnIndex("date_created"));

                // Tạo một TextView mới cho mỗi bình luận
                TextView commentTextView = new TextView(this);
                commentTextView.setText(commentContent + "\n(" + dateCreated + ")");
                commentSection.addView(commentTextView);
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Không tìm thấy bình luận", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveComment(int newsId, String comment) {
        SQLiteDatabase db = myDataHelper.getWritableDatabase();
        db.execSQL("INSERT INTO " + MyDataHelper.TABLE_COMMENT + " (content, date_created, userId, newId) VALUES (?, CURRENT_TIMESTAMP, ?, ?)",
                new String[]{comment, "1", String.valueOf(newsId)}); // Thay "1" bằng userId thực tế nếu có
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Quay lại màn hình trước đó
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
