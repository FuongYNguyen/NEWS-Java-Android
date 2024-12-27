package com.example.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

public class EditUserAccountActivity extends AppCompatActivity {
    private ImageView avatarImageView;
    private EditText emailEditText;
    private EditText passwordEditText;
    private SharedPreferences sharedPreferences;
    private MyDataHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_edit_account);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button confirmButton = findViewById(R.id.confirmButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        databaseHelper = new MyDataHelper(this);

        String currentEmail = sharedPreferences.getString("email", "No Email");

        emailEditText.setText(currentEmail);

        // Handle the confirm button click event
        confirmButton.setOnClickListener(v -> {
            String newEmail = emailEditText.getText().toString();
            String newPassword = passwordEditText.getText().toString();

            if (!newEmail.isEmpty() && !newPassword.isEmpty()) {
                // In giá trị để kiểm tra
                Log.d("EditAccountActivity", "Old Email: " + currentEmail + ", New Email: " + newEmail + ", New Password: " + newPassword);

                // Update the user information in the database
                boolean updateSuccess = databaseHelper.updateUser(currentEmail, newEmail, newPassword);

                if (updateSuccess) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", newEmail);
                    editor.putString("password", newPassword); // Lưu mật khẩu mới vào SharedPreferences
                    editor.apply();

                    // Hiển thị thông báo xác nhận
                    Toast.makeText(this, "Thông tin tài khoản đã được cập nhật", Toast.LENGTH_SHORT).show();

                    // Chuyển về `UserAccountManagementActivity`
                    Intent intent = new Intent(this, UserAccountManagementActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Email và mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            }
    });

        // Handle the cancel button click event
        cancelButton.setOnClickListener(v -> {
            // Chuyển về `UserAccountManagementActivity` mà không thay đổi gì
            Intent intent = new Intent(this, UserAccountManagementActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
