package com.example.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class MyDataHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "news.db";
    public static final int DATABASE_VERSION = 2; // Tăng phiên bản cơ sở dữ liệu
    public static final String TABLE_NAME = "news";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";

    public MyDataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createNewsTable =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_CONTENT + " TEXT);";
        sqLiteDatabase.execSQL(createNewsTable);

        String createCategoryTable =
                "CREATE TABLE category " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT);";
        sqLiteDatabase.execSQL(createCategoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS category");
        onCreate(sqLiteDatabase);
    }

    void addNews(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_CONTENT, content);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(String TABLE_NAME_input) {
        String query = "SELECT * FROM " + TABLE_NAME_input;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
//test github nhanh cua Phuong
