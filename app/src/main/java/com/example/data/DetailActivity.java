package com.example.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
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
    private TextView articleTitleTextView, articleTextView;
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
        commentSection = findViewById(R.id.commentSection);
        commentEditText = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.submitCommentButton);

        myDataHelper = new MyDataHelper(this);

        // Nhận news_id từ Intent
        String newsId = getIntent().getStringExtra("news_id");

        // Lấy thông tin bài viết từ CSDL
        if (newsId != null) {
            loadArticleDetails(newsId);
            loadComments(newsId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin bài viết!", Toast.LENGTH_SHORT).show();
        }

        submitCommentButton.setOnClickListener(view -> submitComment(newsId));
    }

    private void loadArticleDetails(String newsId) {
        Cursor cursor = myDataHelper.getbyNewsId(newsId);

        if (cursor != null && cursor.moveToFirst()) {

            String title = cursor.getString(1);
            String imageUrl = cursor.getString(2);
            String content = cursor.getString(4);

            articleTitleTextView.setText(title);
            articleTextView.setText(content);
        } else {
            Toast.makeText(this, "Không tìm thấy bài viết!", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void loadComments(String newsId) {
        commentSection.removeAllViews(); // Xóa toàn bộ bình luận cũ

        SQLiteDatabase db = myDataHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT c.id, c.content, u.email, c.date_created FROM " + MyDataHelper.TABLE_COMMENT + " c " +
                        "JOIN " + MyDataHelper.TABLE_USER + " u ON c.userId = u.id " +
                        "WHERE c.newId = ? ORDER BY c.date_created DESC",
                new String[]{newsId});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int commentId = cursor.getInt(0);  // Get comment ID
                String content = cursor.getString(1);
                String email = cursor.getString(2);
                long dateCreated = cursor.getLong(3);

                LinearLayout commentLayout = new LinearLayout(this);
                commentLayout.setOrientation(LinearLayout.HORIZONTAL);
                commentLayout.setPadding(8, 8, 8, 8);

                // Create a TextView for the comment content
                TextView commentView = new TextView(this);
                commentView.setText(email + ": " + content);
                commentView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                // Create a Button to delete the comment
                Button deleteButton = new Button(this);
                deleteButton.setText("Xóa");
                deleteButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                // Set OnClickListener for the delete button
                deleteButton.setOnClickListener(view -> deleteComment(commentId, newsId));

                // Add the comment view and delete button to the layout
                commentLayout.addView(commentView);
                commentLayout.addView(deleteButton);

                // Add the comment layout to the comment section
                commentSection.addView(commentLayout);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            TextView noCommentsView = new TextView(this);
            noCommentsView.setText("Chưa có bình luận nào.");
            noCommentsView.setPadding(8, 8, 8, 8);
            commentSection.addView(noCommentsView);
        }
    }


    private void submitComment(String newsId) {
        if (newsId == null || newsId.isEmpty()) {
            Toast.makeText(this, "Không xác định được bài viết!", Toast.LENGTH_SHORT).show();
            return;
        }

        String commentContent = commentEditText.getText().toString().trim();
        if (commentContent.isEmpty()) {
            Toast.makeText(this, "Bình luận không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để bình luận!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu bình luận vào cơ sở dữ liệu
        SQLiteDatabase db = myDataHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", commentContent);
        values.put("date_created", System.currentTimeMillis()); // Lưu thời gian hiện tại
        values.put("userId", userId);
        values.put("newId", newsId);

        long result = db.insert(MyDataHelper.TABLE_COMMENT, null, values);
        if (result == -1) {
            Toast.makeText(this, "Thêm bình luận thất bại!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Bình luận thành công!", Toast.LENGTH_SHORT).show();
            commentEditText.setText(""); // Xóa nội dung trong ô nhập sau khi gửi
            loadComments(newsId); // Tải lại danh sách bình luận
        }
    }
    private void deleteComment(int commentId, String newsId) {
        // Confirm deletion
        new android.app.AlertDialog.Builder(this)
                .setTitle("Xóa bình luận")
                .setMessage("Bạn có chắc chắn muốn xóa bình luận này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    SQLiteDatabase db = myDataHelper.getWritableDatabase();
                    int rowsDeleted = db.delete(MyDataHelper.TABLE_COMMENT, "id = ?", new String[]{String.valueOf(commentId)});
                    if (rowsDeleted > 0) {
                        Toast.makeText(this, "Bình luận đã được xóa!", Toast.LENGTH_SHORT).show();
                        loadComments(newsId);  // Reload the comments
                    } else {
                        Toast.makeText(this, "Lỗi khi xóa bình luận!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
