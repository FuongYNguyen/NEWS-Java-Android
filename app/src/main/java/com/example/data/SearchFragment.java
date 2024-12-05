package com.example.data;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private EditText searchInput;
    private RecyclerView searchResults;
    private CustomAdapter customAdapter;
    private MyDataHelper myDataHelper;
    private ArrayList<String> news_id, news_title, news_content, news_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Tham chiếu các view
        searchInput = view.findViewById(R.id.search_input);
        searchResults = view.findViewById(R.id.search_results);

        // Khởi tạo danh sách và adapter
        news_id = new ArrayList<>();
        news_title = new ArrayList<>();
        news_content = new ArrayList<>();
        news_image = new ArrayList<>();
        customAdapter = new CustomAdapter(getContext(), news_id, news_title, null, news_image, news_content,null, '1');

        // Cấu hình RecyclerView
        searchResults.setAdapter(customAdapter);
        searchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo database helper
        myDataHelper = new MyDataHelper(getContext());

        //thay đổi văn bản trong EditText
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchArticles(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void searchArticles(String keyword) {
        // Đọc dữ liệu từ database
        Cursor cursor = myDataHelper.getByTitle(keyword);

        // Xóa danh sách hiện tại
        news_id.clear();
        news_title.clear();
        news_content.clear();
        news_image.clear();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                news_id.add(cursor.getString(0));
                news_title.add(cursor.getString(1));
                news_content.add(cursor.getString(3));
                news_image.add(cursor.getString(2));
            }
            cursor.close();
        }

        // Cập nhật adapter
        customAdapter.notifyDataSetChanged();
    }
}
