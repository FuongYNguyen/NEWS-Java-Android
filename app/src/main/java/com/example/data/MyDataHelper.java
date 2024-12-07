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
    public static final int DATABASE_VERSION = 6;
    public static final String TABLE_NEWS = "News";
    public static final String TABLE_CATEGORY = "Category";
    public static final String TABLE_ROLE = "role";
    public static final String TABLE_USER = "user";
    public static final String TABLE_COMMENT = "Comment";

    public MyDataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Role
        String createRoleTable =
                "CREATE TABLE " + TABLE_ROLE +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL)" ;
        db.execSQL(createRoleTable);

        // Tạo bảng User
        String createUserTable =
                "CREATE TABLE " + TABLE_USER +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "email TEXT NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "status INTEGER NOT NULL, " +
                        "roleid INTEGER NOT NULL, " +
                        "FOREIGN KEY(roleid) REFERENCES " + TABLE_ROLE + "(id));";
        db.execSQL(createUserTable);

        // Tạo bảng Category
        String createCategoryTable =
                "CREATE TABLE " + TABLE_CATEGORY +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL);";
        db.execSQL(createCategoryTable);

        // Tạo bảng News
        String createNewsTable =
                "CREATE TABLE " + TABLE_NEWS +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL, " +
                        "thumbnail TEXT, " +
                        "shortDescription TEXT, " +
                        "content TEXT NOT NULL, " +
                        "date_created TIMESTAMP, " +
                        "categoryId INTEGER NOT NULL, " +
                        "FOREIGN KEY(categoryId) REFERENCES " + TABLE_CATEGORY + "(id));";
        db.execSQL(createNewsTable);

        // Tạo bảng Comment
        String createCommentTable =
                "CREATE TABLE " + TABLE_COMMENT +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "content TEXT NOT NULL, " +
                        "date_created TIMESTAMP, " +
                        "userId INTEGER NOT NULL, " +
                        "newId INTEGER NOT NULL, " +
                        "FOREIGN KEY(userId) REFERENCES " + TABLE_USER + "(id), " +
                        "FOREIGN KEY(newId) REFERENCES " + TABLE_NEWS + "(id));";
        db.execSQL(createCommentTable);
        insertDefaultRoles(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLE);
        onCreate(db);
    }

    // Hàm thêm dữ liệu vào bảng
    void addData(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(tableName, null, values);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm đọc tất cả dữ liệu từ một bảng
    Cursor readAllData(String tableName) {
        String query = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }
    Cursor getbyNewsId(String newsId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM news WHERE id = ?", new String[]{newsId});
    }
    Cursor getByTitle(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM news WHERE title LIKE ?";
        return db.rawQuery(query, new String[]{"%" + keyword + "%"});
    }
    public void deleteById(String tableName, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsDeleted = db.delete(tableName, "id = ?", new String[]{String.valueOf(id)});
            if (rowsDeleted > 0) {
                Toast.makeText(context, "Row with ID " + id + " deleted from " + tableName + "!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No row found with ID " + id + " in " + tableName + ".", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error deleting row: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void insertDefaultRoles(SQLiteDatabase db) {
        ContentValues adminRole = new ContentValues();
        adminRole.put("name", "Admin");
        db.insert(TABLE_ROLE, null, adminRole);

        ContentValues userRole = new ContentValues();
        userRole.put("name", "User");
        db.insert(TABLE_ROLE, null, userRole);

        ContentValues guestRole = new ContentValues();
        guestRole.put("name", "Guest");
        db.insert(TABLE_ROLE, null, guestRole);
    }

    public Boolean insertData(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("roleid", 1);
        contentValues.put("status", 1);
        long result = MyDatabase.insert("user", null, contentValues);
        return result != -1;
    }
    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from user where email = ?",
                new String[]{email});
        return cursor.getCount() > 0;
    }
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from user where email = ? and password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }
    public int getUserId(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM " + TABLE_USER + " WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        int userId = -1; // Default value if no user is found

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }

    public int getRoleId(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Adjust query to use a JOIN if needed to fetch details from the Roles table
        String query = "SELECT roleid FROM " + TABLE_USER + " WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        int roleid = -1; // Default value if no user is found

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                roleid = cursor.getInt(0); // Get role ID from the user table
            }
            cursor.close();
        }

        return roleid;
    }
    public Cursor getNewsByCategory(String categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NEWS + " WHERE categoryId = ?";
        return db.rawQuery(query, new String[]{categoryId});
    }


}

