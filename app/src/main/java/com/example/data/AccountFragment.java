package com.example.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AccountFragment extends Fragment {

    private TextView displayName, displayEmail;
    private Button editAccountButton, deleteAccountButton;
    private ImageView avatarImage;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        displayName = view.findViewById(R.id.display_name);
        displayEmail = view.findViewById(R.id.display_email);
        editAccountButton = view.findViewById(R.id.edit_account_button);
        deleteAccountButton = view.findViewById(R.id.delete_account_button);
        avatarImage = view.findViewById(R.id.avatar_image);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("account_data", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "Nguyen Dang Vi");
        String email = sharedPreferences.getString("email", "ngdangvi2910@gmail.com");
        String avatarString = sharedPreferences.getString("avatar", null);
        Uri avatarUri = avatarString != null ? Uri.parse(avatarString) : null;

        displayName.setText(name);
        displayEmail.setText(email);
        if (avatarUri != null) {
            avatarImage.setImageURI(avatarUri);
        } else {
            avatarImage.setImageResource(R.drawable.ic_avatar_placeholder);
        }

        editAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang EditAccountFragment để chỉnh sửa thông tin tài khoản
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new EditAccount())
                        .addToBackStack(null)
                        .commit();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện xóa tài khoản
                Toast.makeText(getActivity(), "Tài khoản đã bị xóa", Toast.LENGTH_SHORT).show();
                // Quay lại màn hình trước hoặc thoát ứng dụng
                getActivity().finish();
            }
        });

        return view;
    }
}
