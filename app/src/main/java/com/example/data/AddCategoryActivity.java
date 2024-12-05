package com.example.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddCategoryActivity extends AppCompatActivity {
    EditText name_input;
    Button create_button;

    public static final String COLUMN_NAME = "name";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // Liên kết với các view trong layout
        name_input = findViewById(R.id.name_input);
        create_button = findViewById(R.id.create_button);


        // Thiết lập sự kiện click cho nút "Create News"
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra input
                if (name_input.getText().toString().trim().isEmpty()
                        ) {
                    Toast.makeText(AddCategoryActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thêm dữ liệu vào cơ sở dữ liệu
                MyDataHelper myDB = new MyDataHelper(AddCategoryActivity.this);
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME, name_input.getText().toString().trim());

                // Thêm dữ liệu vào bảng News
                myDB.addData("Category", cv);

                // Trả về kết quả và đóng Activity
                Toast.makeText(AddCategoryActivity.this, "Category created successfully!", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

}
