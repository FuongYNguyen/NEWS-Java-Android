package com.example.data;

import android.content.Intent;
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

public class CategoryFragment extends Fragment {


    private static final int ADD_ACTIVITY_REQUEST_CODE = 1;
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDataHelper myDataHelper;
    ArrayList<String> category_id, category_name;
    CustomAdapter customAdapter;

    private ActivityResultLauncher<Intent> addActivityResultLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        add_button = view.findViewById(R.id.add_button);

        myDataHelper = new MyDataHelper(getContext());
        category_id = new ArrayList<>();
        category_name = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(getContext(), category_id, category_name, null, null,null,null,'3');
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
                Intent intent = new Intent(getContext(), AddCategoryActivity.class);
                addActivityResultLauncher.launch(intent);
            }
        });

        return view;
    }

    void storeDataInArrays() {
        Cursor cursor = myDataHelper.readAllData("category");
        category_id.clear();
        category_name.clear();

        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                category_id.add(cursor.getString(0));
                category_name.add(cursor.getString(1));
            }
        }
    }
}