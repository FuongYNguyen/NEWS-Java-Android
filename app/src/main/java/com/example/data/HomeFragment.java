package com.example.data;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDataHelper myDataHelper;
    ArrayList<String> news_id, news_title, news_content;
    CustomAdapter customAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        add_button = view.findViewById(R.id.add_button);

        myDataHelper = new MyDataHelper(getContext());
        news_id = new ArrayList<>();
        news_title = new ArrayList<>();
        news_content = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(getContext(), news_id, news_title, news_content);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    void storeDataInArrays() {
        Cursor cursor = myDataHelper.readAllData("news");
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                news_id.add(cursor.getString(0));
                news_title.add(cursor.getString(1));
                news_content.add(cursor.getString(2));
            }
        }
    }
}
