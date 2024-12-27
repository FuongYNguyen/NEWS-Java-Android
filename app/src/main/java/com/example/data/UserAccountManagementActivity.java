package com.example.data;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UserAccountManagementActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private MyDataHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account_management);

        // Initialize the views
        TextView emailTextView = findViewById(R.id.emailTextView);
        Button editAccountButton = findViewById(R.id.editAccountButton);
        Button deleteAccountButton = findViewById(R.id.deleteAccountButton);

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        databaseHelper = new MyDataHelper(this);

        String userEmail = sharedPreferences.getString("email", "No Email");

        // Set the email to the TextView
        emailTextView.setText(userEmail);

        // Set up the edit account button click listener
        editAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserAccountManagementActivity.this, EditUserAccountActivity.class);
            startActivity(intent);
        });

        // Set up the delete account button click listener
        deleteAccountButton.setOnClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Xóa tài khoản")
                    .setMessage("Bạn có chắc chắn muốn xóa tài khoản này không?")
                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Xóa tài khoản khỏi cơ sở dữ liệu
                            boolean deleteSuccess = databaseHelper.deleteUser(userEmail);

                            if (deleteSuccess) {
                                // Xóa dữ liệu phiên làm việc
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();

                                // Hiển thị thông báo xác nhận
                                Toast.makeText(UserAccountManagementActivity.this, "Tài khoản đã được xóa", Toast.LENGTH_SHORT).show();

                                // Chuyển về màn hình đăng nhập
                                Intent intent = new Intent(UserAccountManagementActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(UserAccountManagementActivity.this, "Xóa tài khoản thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }
}
