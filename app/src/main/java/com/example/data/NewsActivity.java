package com.example.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnews);

        // Lấy LinearLayout cha chứa danh sách bài viết
        LinearLayout postContainer = findViewById(R.id.postContainer);

        // Thêm nhiều bài viết mẫu
        for (int i = 1; i <= 10; i++) {
            // Tạo một bản sao của i
            final int index = i;

            // Inflate bài viết từ file post_item.xml
            View Addnews = LayoutInflater.from(this).inflate(R.layout.activity_addnews, null);

            // Gán nội dung tiêu đề và caption
            TextView tvTitle = Addnews.findViewById(R.id.item_title);
            TextView tvCaption = Addnews.findViewById(R.id.item_caption);
            tvTitle.setText("Tiêu đề bài " + index);
            tvCaption.setText("Caption bài " + index);

            // Nút Sửa
            Button btnEdit = Addnews.findViewById(R.id.edit_button);
            btnEdit.setOnClickListener(view -> {
                Toast.makeText(NewsActivity.this, "Sửa bài viết " + index, Toast.LENGTH_SHORT).show();
            });

            // Nút Xóa
            Button btnDelete = Addnews.findViewById(R.id.delete_button);
            btnDelete.setOnClickListener(view -> {
                postContainer.removeView(Addnews); // Xóa bài viết khỏi danh sách
                Toast.makeText(NewsActivity.this, "Đã xóa bài viết " + index, Toast.LENGTH_SHORT).show();
            });

            // Thêm bài viết vào container
            postContainer.addView(Addnews);
        }
    }
}
