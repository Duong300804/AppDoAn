package com.example.appdoan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appdoan.NguoiDung.NguoiDung;
import com.example.appdoan.QuanLy.QuanLy;
import com.example.appdoan.tool.Database;

public class DangNhap extends AppCompatActivity {
    EditText edtTenDangNhap,edtMatKhau;
    Button btnDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khai báo và sử dụng database
        // Khởi tạo Database
        Database database = new Database(DangNhap.this);
        database.createDatabase(); // Tạo cơ sở dữ liệu nếu chưa có

        SQLiteDatabase db = database.openDatabase(); // Khai báo cơ sở dữ liệu

        // Ánh xạ ID
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy tên đăng nhập và tài khoản từ EDT
                String tentaikhoan = edtTenDangNhap.getText().toString();
                String matkhau = edtMatKhau.getText().toString();

                // Mệnh Đề WHERE trong SQL
                String WHERE = "tentaikhoan=? AND matkhau=?";

                // Mảng tham số truyền vào ?
                String[] selectionArgs = { tentaikhoan, matkhau };

                // Khai báo con trỏ cursor
                Cursor cursor = db.query("taikhoan",null,WHERE,selectionArgs,null,null,null,null);

                //Đếm dòng nếu không có dòng nào => Tên tài khoản hoặc mật khẩu không tồn tại
                int cursorCount = cursor.getCount();
                if(cursorCount == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DangNhap.this);
                    builder.setTitle("Đăng nhập không thành công");
                    builder.setMessage("Sai tên tài khoản hoặc mật khẩu!");

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    cursor.moveToFirst();
                    //Lấy loại tài khoản ra
                    String loaitaikhoan = cursor.getString(4);
                    //Lấy mã tài khoản ra
                    int mataikhoan = cursor.getInt(0);

                    //Kiểm tra xem loại tài khoản gì để chuyển hướng
                    if (loaitaikhoan.equals("admin")){
                        // Lưu biến vào SharedPreferences
                        SharedPreferences sharedPref = getSharedPreferences("mataikhoan", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("mataikhoan",mataikhoan);
                        editor.apply();

                        Intent intent = new Intent(DangNhap.this, QuanLy.class);
                        startActivity(intent);
                    }else{
                        // Lưu biến vào SharedPreferences
                        SharedPreferences sharedPref = getSharedPreferences("mataikhoan", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("mataikhoan",mataikhoan);
                        editor.apply();

                        Intent intent = new Intent(DangNhap.this, NguoiDung.class);
                        startActivity(intent);
                    }
                }
                cursor.close();
            }
        });
    }

    public void register(View view) {
        startActivity(new Intent(DangNhap.this, DangKy.class));
    }

    public void forgot_password(View view) {
        startActivity(new Intent(DangNhap.this, QuenMatKhau.class));
    }
}

