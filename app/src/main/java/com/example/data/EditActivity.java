package com.example.data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    EditText titleInput, shortDescInput, contentInput;
    Button updateButton;
    String newsId;
    MyDataHelper myDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        titleInput = findViewById(R.id.edit_title);
        shortDescInput = findViewById(R.id.edit_short_description);
        contentInput = findViewById(R.id.edit_content);
        updateButton = findViewById(R.id.update_button);

        myDataHelper = new MyDataHelper(this);
        newsId = getIntent().getStringExtra("news_id");

        loadNewsData(newsId);

        updateButton.setOnClickListener(view -> updateNewsData());
    }

    private void loadNewsData(String id) {
        Cursor cursor = myDataHelper.getbyNewsId(id);
        if (cursor.moveToFirst()) {
            titleInput.setText(cursor.getString(1)); // Title
            shortDescInput.setText(cursor.getString(3)); // Short description
            contentInput.setText(cursor.getString(4)); // Content
        }
    }

    private void updateNewsData() {
        String title = titleInput.getText().toString();
        String shortDesc = shortDescInput.getText().toString();
        String content = contentInput.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Title and Content cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("shortDescription", shortDesc);
        values.put("content", content);

        myDataHelper.getWritableDatabase().update(MyDataHelper.TABLE_NEWS, values, "id = ?", new String[]{newsId});
        Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();

        // Trả kết quả về Fragment
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isUpdated", true);
        editor.apply();
        finish();
    }

}
