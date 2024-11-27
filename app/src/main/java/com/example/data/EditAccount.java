package com.example.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditAccount extends Fragment {

    private EditText editName, editEmail;
    private Button saveChangesButton, cancelChangesButton;
    private ImageView editAvatarImage;
    private Uri avatarUri;

    public EditAccount() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);

        editName = view.findViewById(R.id.edit_name);
        editEmail = view.findViewById(R.id.edit_email);
        saveChangesButton = view.findViewById(R.id.save_changes_button);
        cancelChangesButton = view.findViewById(R.id.cancel_changes_button);
        editAvatarImage = view.findViewById(R.id.edit_avatar_image);

        // Giả lập dữ liệu tài khoản hiện tại
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account_data", Context.MODE_PRIVATE);
        String currentName = sharedPreferences.getString("name", "Nguyen Dang Vi");
        String currentEmail = sharedPreferences.getString("email", "ngdangvi2910@gmail.com");
        editName.setText(currentName);
        editEmail.setText(currentEmail);
        editAvatarImage.setImageResource(R.drawable.ic_avatar_placeholder);

        // Xử lý sự kiện chọn avatar
        editAvatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở thư viện ảnh để chọn avatar
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        // Xử lý sự kiện lưu thay đổi
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String email = editEmail.getText().toString();

                // Lưu dữ liệu vào SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("email", email);
                if (avatarUri != null) {
                    editor.putString("avatar", avatarUri.toString());
                }
                editor.apply();

                Toast.makeText(getActivity(), "Đã lưu thay đổi", Toast.LENGTH_SHORT).show();

                // Quay lại AccountFragment với thông tin đã cập nhật
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AccountFragment())
                        .commit();
            }
        });

        // Xử lý sự kiện hủy thay đổi
        cancelChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại AccountFragment mà không lưu thay đổi
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AccountFragment())
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            avatarUri = data.getData();
            editAvatarImage.setImageURI(avatarUri);
        }
    }
}
