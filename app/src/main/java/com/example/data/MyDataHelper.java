package com.example.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;
import android.util.Log;


public class MyDataHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "news.db";
    public static final int DATABASE_VERSION = 7;
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
                        "FOREIGN KEY(categoryId) REFERENCES " + TABLE_CATEGORY + "(id) ON DELETE CASCADE);";
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
        insertRecentCategories(db);
        insertRecentNews(db);

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
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true); // Kích hoạt hỗ trợ khóa ngoại
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
        String query = "SELECT * FROM " + tableName + " ORDER BY id DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }
//    Cursor readAllData(String tableName) {
//        String query;
//        if (tableName.equals(TABLE_NEWS)) {
//            query = "SELECT * FROM " + tableName + " ORDER BY id DESC";
//        } else {
//            query = "SELECT * FROM " + tableName;
//        }
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.rawQuery(query, null);
//    }

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
    private void insertRecentCategories(SQLiteDatabase db) {
        ContentValues category1 = new ContentValues();
        category1.put("name", "Công Nghệ");
        db.insert(TABLE_CATEGORY, null, category1);

        ContentValues category2 = new ContentValues();
        category2.put("name", "Khoa Học");
        db.insert(TABLE_CATEGORY, null, category2);

        ContentValues category3 = new ContentValues();
        category3.put("name", "Giáo Dục");
        db.insert(TABLE_CATEGORY, null, category3);

        ContentValues category4 = new ContentValues();
        category4.put("name", "Sức Khỏe");
        db.insert(TABLE_CATEGORY, null, category4);

        ContentValues category5 = new ContentValues();
        category5.put("name", "Thời Sự");
        db.insert(TABLE_CATEGORY, null, category5);

        ContentValues category6 = new ContentValues();
        category6.put("name", "Thể Thao");
        db.insert(TABLE_CATEGORY, null, category6);

        ContentValues category7 = new ContentValues();
        category7.put("name", "Kinh Tế");
        db.insert(TABLE_CATEGORY, null, category7);

        ContentValues category8 = new ContentValues();
        category8.put("name", "Giải Trí");
        db.insert(TABLE_CATEGORY, null, category8);

        ContentValues category9 = new ContentValues();
        category9.put("name", "Du Lịch");
        db.insert(TABLE_CATEGORY, null, category9);

        ContentValues category10 = new ContentValues();
        category10.put("name", "Ẩm Thực");
        db.insert(TABLE_CATEGORY, null, category10);
    }
    private void insertRecentNews(SQLiteDatabase db) {
        ContentValues news1 = new ContentValues();
        news1.put("title", "AI Đột Phá: ChatGPT 4.5 Chính Thức Ra Mắt");
        news1.put("thumbnail", ""); // Để trống hình ảnh
        news1.put("shortDescription", "Phiên bản mới của AI, ChatGPT 4.5, mang đến hiệu suất vượt trội và khả năng trả lời thông minh hơn.");
        news1.put("content", "OpenAI vừa công bố phiên bản ChatGPT 4.5 với hàng loạt cải tiến. Công cụ này hứa hẹn hỗ trợ tốt hơn cho các lĩnh vực như giáo dục, lập trình, và sáng tạo nội dung.");
        news1.put("date_created", "26/12/2024");
        news1.put("categoryId", 1);
        db.insert(TABLE_NEWS, null, news1);

        ContentValues news2 = new ContentValues();
        news2.put("title", "Công Nghệ Pin Mới: Sạc Trong 5 Phút");
        news2.put("thumbnail", ""); // Để trống hình ảnh
        news2.put("shortDescription", "Công nghệ pin lithium mới có thể cách mạng hóa ngành điện tử với tốc độ sạc siêu nhanh.");
        news2.put("content", "Các nhà khoa học đã phát triển một loại pin lithium mới, có thể sạc đầy chỉ trong 5 phút, mở ra tương lai cho xe điện và thiết bị di động.");
        news2.put("date_created", "26/12/2024");
        news2.put("categoryId", 1);
        db.insert(TABLE_NEWS, null, news2);

        ContentValues news3 = new ContentValues();
        news3.put("title", "Khám Phá Vũ Trụ: Tàu Vũ Trụ Artemis II Chuẩn Bị Cất Cánh");
        news3.put("thumbnail", ""); // Để trống hình ảnh
        news3.put("shortDescription", "NASA tiết lộ thông tin chi tiết về sứ mệnh Artemis II, đánh dấu bước tiến lớn trong hành trình quay lại Mặt Trăng.");
        news3.put("content", "Sứ mệnh Artemis II của NASA sẽ đưa con người quay lại khám phá Mặt Trăng, với mục tiêu chuẩn bị cho việc định cư lâu dài trên hành tinh này.");
        news3.put("date_created", "26/12/2024");
        news3.put("categoryId", 1);
        db.insert(TABLE_NEWS, null, news3);

        ContentValues news4 = new ContentValues();
        news4.put("title", "Rashford quay xe muốn ở lại MU, Osimhen từ chối \"Quỷ đỏ\" vì một lý do");
        news4.put("thumbnail", ""); // Để trống hình ảnh
        news4.put("shortDescription", "Một bộ phim tài liệu mới về cuộc đời HLV huyền thoại của Manchester United Sir Alex Ferguson được phát hành vào Ngày tặng quà (Boxing Day), và một sự cố liên quan đến Cristiano Ronaldo đã được đề cập");
        news4.put("content", "Bộ phim tài liệu cung cấp cái nhìn sâu sắc về con người đằng sau vị HLV huyền thoại Sir Alex Ferguson, phản ánh về tuổi thơ, gia đình, những ngày tháng thi đấu và những ảnh hưởng của ông, cũng như đề cập đến những tranh cãi như việc nhà Glazer tiếp quản MU và tranh cãi về ngựa đua Rock of Gibraltar.\n" +
                "\n" +
                "Thời gian Sir Alex Ferguson ở MU chiếm phần lớn thời lượng của bộ phim tài liệu nhưng bộ phim cũng đề cập về tác động của Jock Stein đối với Sir Alex Ferguson và cách ông bước vào vị trí HLV trưởng đội tuyển Scotland.\n" +
                "\n" +
                "Thành công vang dội của HLV người Scotland Sir Alex Ferguson tại Aberdeen, đỉnh cao là vinh quang tại Cúp châu Âu dành cho các CLB đoạt cúp quốc gia năm 1983 trước đội bóng hùng mạnh Real Madrid, nhận được sự chú ý quan trọng.\n" +
                "\n" +
                "Khi đó, cựu hậu vệ phải Stuart Kennedy nhớ lại “máy sấy tóc” của Sir Alex Ferguson như một “lò áp suất cao”, với Mark McGhee ví nó như “núi lửa” có sức tàn phá như Pompeii. Không ai là quá lớn để thoát khỏi máy sấy tóc của Sir Alex Ferguson, kể cả ngôi sao đang lên Cristiano Ronaldo của MU.\n" +
                "\n" +
                "Cựu hậu vệ của MU, Rio Ferdinand cho biết: \"Cristiano Ronaldo đã không chơi tốt, và HLV đã chỉ trích cậu ấy. Sir Alex Ferguson nói với Ronaldo, “Anh nghĩ mình là ai? Anh đến đây để cố gắng chứng minh với mọi người về cái gì, anh nghĩ mình là ai, anh nghĩ mình là một siêu sao à”. Tôi nhớ Cristiano Ronaldo đã khóc và tôi nghĩ, “Người quản lý này chẳng quan tâm đâu, anh bạn ạ. Ông ấy không quan tâm bạn là ai””.");
        news4.put("date_created", "26/12/2024");
        news4.put("categoryId", 6);
        db.insert(TABLE_NEWS, null, news4);

        ContentValues news5 = new ContentValues();
        news5.put("title", "Cách ông Lưu Bình Nhưỡng can thiệp chính quyền giúp doanh nghiệp để hưởng lợi");
        news5.put("thumbnail", ""); // Để trống hình ảnh
        news5.put("shortDescription", "Ông Lưu Bình Nhưỡng bị cáo buộc dùng danh nghĩa đại biểu Quốc hội để giúp nhóm bảo kê cưỡng đoạt tài sản và nhận cả trăm nghìn USD của doanh nghiệp để can thiệp đến chính quyền một số nơi");
        news5.put("content", "'Bảo kê' giang hồ\n" +
                "\n" +
                "Theo cáo trạng, Công ty TNHH MTV kinh doanh khai thác vật liệu xây dựng Sao Đỏ được UBND tỉnh Thái Bình cấp phép khai thác mỏ cát tại vùng biển xã Thụy Trường, huyện Thái Thụy. Khi Sao Đỏ và các doanh nghiệp khai thác cát, Cường và Phương đã gây chuyện, chặn lối ra vào bãi triều nhằm ép doanh nghiệp phải trả tiền theo khối lượng cát khai thác. Nếu không đồng ý, Cường sẽ gây cản trở, không cho các tàu vào khai thác.\n" +
                "\n" +
                "Cường yêu cầu Công ty Sao Đỏ phải trả 1.500 đồng/m3 cát mà công ty khai thác được, tương đương 1,05 triệu đồng/một tàu khai thác cát. Vì không còn cách nào khác, doanh nghiệp buộc phải chấp nhận yêu cầu Cường đưa ra. Nhằm che giấu hành vi cưỡng đoạt, Cường đã ký hợp đồng làm bảo vệ cho Sao Đỏ. Từ tháng 9/2020 đến 12/2020, Sao Đỏ đã buộc phải trả cho Cường 3,3 tỷ đồng.\n" +
                "\n" +
                "Quá trình khai thác cát, tàu của Sao Đỏ làm đổ cọc, vây tại bãi triều trái phép của giang hồ Dũng \"Chiến\" nên dẫn đến nhóm của Cường thường xuyên phải ra mặt đánh nhau. Thấy không an toàn nên từ tháng 1/2021, Sao Đỏ dừng việc khai thác cát và không trả tiền cho Cường.\n" +
                "\n" +
                "Bị thất thu, trong tháng 5 và 6/2021, Cường, Phương nhiều lần đến nhà riêng của ông Nhưỡng ở quận Tây Hồ, Hà Nội và nhà thờ của bị can tại huyện Hưng Hà, Thái Bình, nhờ giúp đỡ. Ông Nhưỡng khi đó là đại biểu Quốc hội khóa 14, Phó trưởng Ban dân nguyện thuộc Ủy ban thường vụ Quốc hội.\n" +
                "\n" +
                "Tại các lần gặp gỡ, Cường nhờ ông Nhưỡng can thiệp với lãnh đạo Công an tỉnh Thái Bình tạo điều kiện giúp đỡ. Cường còn khoe với ông Nhưỡng là dùng bãi triều để thu tiền của Sao Đỏ, mỗi tháng được 400-500 triệu đồng. Ông Nhưỡng đồng ý giúp nhưng không gọi điện can thiệp ngay.\n" +
                "\n" +
                "Sau đó, để tăng cường thêm sự gắn kết, Cường rủ vợ chồng vị đại biểu Quốc hội đầu tư mua bãi triều được lập trái phép. Trong lần dẫn ra biển chơi, Cường còn chỉ cho ông Nhưỡng biết bãi triều này đang dùng để thu tiền bảo kê của Sao Đỏ.\n" +
                "\n" +
                "Cuối tháng 7/2021, vợ chồng ông Nhưỡng đồng ý mua 30 ha bãi triều mà Cường tự cắm trái phép, lấn chiếm từ trước. Cường nói giá thị trường là 1,2 tỷ đồng nhưng chỉ lấy của vợ chồng ông 900 triệu. Mua xong, vợ chồng ông Nhưỡng giao lại cho Cường quản lý để thu tiền theo tháng, từ 60-80 triệu đồng.\n" +
                "\n" +
                "Trên thực tế không có công ty nào khai thác nhưng để \"làm thân\" với vị Phó ban Dân nguyện, Cường đã trích từ tiền cưỡng đoạt được để chi trả. Khi vợ ông Nhưỡng nhắn tin hỏi lợi nhuận, Cường đã tự tính toán số tiền phải đưa là 400 triệu đồng. Tuy nhiên vợ ông Nhưỡng chưa cầm tiền luôn vì để gom lại mua thêm mảnh đất gần khu trang trại nhà Cường, theo cáo trạng.\n" +
                "\n" +
                "Ngày 4/9/2021, Cường tiếp tục gọi điện, nhắn tin nhờ ông Nhưỡng can thiệp thêm vì liên tục bị nhóm xã hội đen Dũng \"Chiến\" cản trở việc bảo kê. Để thuận lợi cho công việc làm ăn chung với Cường ngoài bãi triều và giúp đỡ anh ta không bị quấy rối khi đi bảo kê, ông Nhưỡng đã gọi điện cho ông Phạm Mạnh Hùng, Phó giám đốc Công an tỉnh Thái Bình.\n" +
                "\n" +
                "Ông Nhưỡng nói Cường là cháu, nhờ ông Hùng giải quyết, xử lý giúp nhóm Dũng \"Chiến\". Toàn bộ cuộc gọi được ông Nhưỡng ghi âm lại, gửi cho Cường nghe.\n" +
                "\n" +
                "Nhận được ghi âm, Cường mang ra khoe với đàn em để đánh tiếng đến nhóm Dũng \"Chiến\". Nhằm gây thanh thế, ông Nhưỡng còn dẫn Cường đi cùng đến Đồn biên phòng, gặp chính quyền xã Thụy Xuân, huyện Thái Thụy. Khi biết Cường có ông Nhưỡng \"chống lưng\", nhóm Dũng \"Chiến\" sợ hãi, bỏ đi nơi khác làm ăn.\n" +
                "\n" +
                "Cường sau đó thông báo cho Sao Đỏ đã \"dẹp loạn\" xong để quay lại khai thác. Từ tháng 10/2021 đến 4/2022, Cường cùng đồng phạm đã cưỡng đoạt của công ty Sao Đỏ hơn 1,3 tỷ đồng. Tháng 4/2022, Cường bị bắt về tội Gây rối trật tự công cộng nên các phần việc do Phương đảm nhận và thu thêm của Sao Đỏ 230 triệu đồng.\n" +
                "\n" +
                "Theo VKS, tổng số tiền Cường và đồng phạm đã cưỡng đoạt của Sao Đỏ là 4,9 tỷ đồng. Trong đó, việc cưỡng đoạt 1,6 tỷ đồng từ 10/2021 đến 7/2022 có sự giúp sức của ông Nhưỡng. Toàn bộ số tiền này, Cường chi tiêu cá nhân và trả cho Phương khoảng 180 triệu đồng.");
        news5.put("date_created", "26/12/2024");
        news5.put("categoryId", 5);
        db.insert(TABLE_NEWS, null, news5);
    }
    public boolean updateUser(String oldEmail, String newEmail, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", newEmail);
        contentValues.put("password", newPassword);

        // In giá trị để kiểm tra
        Log.d("DatabaseHelper", "Updating user - Old Email: " + oldEmail + ", New Email: " + newEmail);

        // Cập nhật bản ghi dựa trên email cũ
        int result = db.update("user", contentValues, "email=?", new String[]{oldEmail});

        // In kết quả cập nhật
        Log.d("DatabaseHelper", "Update result: " + result);

        return result > 0; // Trả về true nếu cập nhật thành công
    }
    public boolean deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        // In giá trị để kiểm tra
        Log.d("DatabaseHelper", "Deleting user with email: " + email);

        // Xóa bản ghi dựa trên email
        int result = db.delete("user", "email=?", new String[]{email});

        // In kết quả xóa
        Log.d("DatabaseHelper", "Delete result: " + result);

        return result > 0; // Trả về true nếu xóa thành công
    }
}







