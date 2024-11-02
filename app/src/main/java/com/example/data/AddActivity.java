package com.example.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;


public class AddActivity extends AppCompatActivity {
    EditText title_input, content_input;
    Button createNews_button;
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        title_input = findViewById(R.id.title_input);
        content_input = findViewById(R.id.content_input);
        createNews_button = findViewById(R.id.createNews_button);
        createNews_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyDataHelper myDB = new MyDataHelper(AddActivity.this);
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_TITLE, title_input.getText().toString().trim());
                cv.put(COLUMN_CONTENT, content_input.getText().toString().trim());
                myDB.addData("news", cv);
                //myDB.addNews(title_input.getText().toString().trim(), content_input.getText().toString().trim());
            }
        });
    }
}