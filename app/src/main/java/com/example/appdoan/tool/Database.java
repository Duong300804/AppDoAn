package com.example.appdoan.tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "appdoan.db"; // Chỉ cần tên tệp
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private SQLiteDatabase database;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Phương thức này sẽ lấy đường dẫn chính xác tới tệp cơ sở dữ liệu
    private String getDatabasePath() {
        return context.getDatabasePath(DATABASE_NAME).getPath();
    }

    public void createDatabase(){
        boolean dbExist = checkDatabase();

        if (!dbExist) {
            this.getWritableDatabase();
            try {
                copyDatabase(); // Sao chép cơ sở dữ liệu từ assets
                Log.d("Database", "Cơ sở dữ liệu đã được sao chép thành công.");
            } catch (Exception e) {
                Log.e("Database", "Lỗi khi sao chép cơ sở dữ liệu: " + e.getMessage());
                throw new Error("Error copying database");
            }
        } else {
            Log.d("Database", "Cơ sở dữ liệu đã tồn tại.");
        }
    }

    // Kiểm tra xem tệp cơ sở dữ liệu đã tồn tại hay chưa
    private boolean checkDatabase() {
        File dbFile = new File(getDatabasePath());
        return dbFile.exists();
    }

    // Sao chép tệp cơ sở dữ liệu từ thư mục assets vào thư mục cơ sở dữ liệu của ứng dụng
    private void copyDatabase() throws IOException {
        InputStream input = context.getAssets().open(DATABASE_NAME);
        String outFileName = getDatabasePath();
        OutputStream output = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }

    // Mở cơ sở dữ liệu đã sao chép
    public SQLiteDatabase openDatabase() {
        String path = getDatabasePath();
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return database;
    }

    // Đóng cơ sở dữ liệu
    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
            Log.d("Database", "Cơ sở dữ liệu đã được đóng.");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
