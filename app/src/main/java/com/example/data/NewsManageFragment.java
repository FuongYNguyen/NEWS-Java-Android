package com.example.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NewsManageFragment extends Fragment {

    private static final int ADD_ACTIVITY_REQUEST_CODE = 1;
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDataHelper myDataHelper;
    ArrayList<String> news_id, news_title, news_sc, news_image;
    CustomAdapter customAdapter;

    public ActivityResultLauncher<Intent> addActivityResultLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_manage, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        add_button = view.findViewById(R.id.add_button);

        myDataHelper = new MyDataHelper(getContext());
        news_id = new ArrayList<>();
        news_title = new ArrayList<>();
        news_sc = new ArrayList<>();
        news_image = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(getContext(), news_id, news_title, null, news_image, news_sc, null,'2');
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        storeDataInArrays();
                        customAdapter.notifyDataSetChanged();
                    }
                });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddActivity.class);
                addActivityResultLauncher.launch(intent);
            }
        });


        return view;
    }

    void storeDataInArrays() {
        Cursor cursor = myDataHelper.readAllData("news");
        news_id.clear();
        news_title.clear();
        news_sc.clear();
        news_image.clear();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                news_id.add(cursor.getString(0));
                news_title.add(cursor.getString(1));
                news_sc.add(cursor.getString(3));
                news_image.add(cursor.getString(2));
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isUpdated = sharedPreferences.getBoolean("isUpdated", false);

        if (isUpdated) {
            // Reload dữ liệu
            storeDataInArrays();
            customAdapter.notifyDataSetChanged();

            // Reset trạng thái
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isUpdated", false);
            editor.apply();
        }
    }

}