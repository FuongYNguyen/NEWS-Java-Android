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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    EditText title_input, content_input, categoryId_input, thumbnail_input, shortDescription_input, createdDate_input;
    Button createNews_button, selectThumbnail_button;
    Uri selectedImageUri;

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CATEGORY_ID = "categoryId";
    public static final String COLUMN_THUMBNAIL = "thumbnail";
    public static final String COLUMN_CREATEDDATE = "date_created";
    public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Liên kết với các view trong layout
        title_input = findViewById(R.id.title_input);
        content_input = findViewById(R.id.content_input);
        categoryId_input = findViewById(R.id.categoryId_input);
        thumbnail_input = findViewById(R.id.thumbnail_input);
        shortDescription_input = findViewById(R.id.shortDescription_input);
        createNews_button = findViewById(R.id.createNews_button);
        selectThumbnail_button = findViewById(R.id.selectThumbnail_button);

        // Cài đặt Intent để chọn ảnh từ thư viện
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            thumbnail_input.setText(getFileName(selectedImageUri)); // Hiển thị tên tệp hình ảnh
                        }
                    }
                });

        // Khi nhấn vào nút "Select Thumbnail"
        selectThumbnail_button.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Thiết lập sự kiện click cho nút "Create News"
        createNews_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra input
                if (title_input.getText().toString().trim().isEmpty() ||
                        content_input.getText().toString().trim().isEmpty() ||
                        categoryId_input.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                String currentDate = getCurrentDate();
                // Thêm dữ liệu vào cơ sở dữ liệu
                MyDataHelper myDB = new MyDataHelper(AddActivity.this);
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_TITLE, title_input.getText().toString().trim());
                cv.put(COLUMN_CONTENT, content_input.getText().toString().trim());
                cv.put(COLUMN_CATEGORY_ID, Integer.parseInt(categoryId_input.getText().toString().trim()));

                // Thumbnail và Short Description là không bắt buộc
                if (selectedImageUri != null) {
                    cv.put(COLUMN_THUMBNAIL, selectedImageUri.toString());
                } else {
                    cv.put(COLUMN_THUMBNAIL, "No thumbnail");
                }
                cv.put(COLUMN_SHORT_DESCRIPTION, shortDescription_input.getText().toString().trim());
                cv.put(COLUMN_CREATEDDATE, currentDate);

                // Thêm dữ liệu vào bảng News
                myDB.addData("News", cv);

                // Trả về kết quả và đóng Activity
                Toast.makeText(AddActivity.this, "News created successfully!", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }
    private String getFileName(Uri uri) {
        String result = null;

        // Kiểm tra nếu URI thuộc loại 'content'
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }

        // Fallback nếu không lấy được DISPLAY_NAME
        if (result == null) {
            result = uri.getLastPathSegment();
        }

        // Kiểm tra và xử lý trường hợp tên file không xác định
        if (result == null || result.isEmpty()) {
            Toast.makeText(this, "Cannot retrieve file name", Toast.LENGTH_SHORT).show();
            result = "Unknown File";
        }

        return result;
    }


}
