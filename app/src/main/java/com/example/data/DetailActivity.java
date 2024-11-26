package com.example.data;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private TextView commentTextView;
    private LinearLayout commentSection;
    private EditText commentEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

        scrollView = findViewById(R.id.scrollView);
        TextView articleTextView = findViewById(R.id.articleTextView);
        commentTextView = findViewById(R.id.commentTextView);
        commentSection = findViewById(R.id.commentSection);
        commentEditText = findViewById(R.id.commentEditText);
        Button submitCommentButton = findViewById(R.id.submitCommentButton);

        // Set article text (replace with your actual article content)
        articleTextView.setText("Nội dung bài báo sẽ được hiển thị ở đây...");

        // Add scroll listener to show comments section when scrolled to bottom
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                if (diff == 0) {
                    // Show comment section when scrolled to bottom
                    commentSection.setVisibility(View.VISIBLE);
                }
            }
        });

        submitCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEditText.getText().toString();
                if (!comment.isEmpty()) {
                    commentTextView.setText(comment);
                    commentEditText.setText("");
                    Toast.makeText(DetailActivity.this, "Bình luận đã được gửi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "Vui lòng nhập bình luận của bạn", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
