package com.example.appdoan;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.appdoan.tool.BatLoi;
import com.example.appdoan.tool.Database;

public class QuenMatKhau extends AppCompatActivity {
    //Khai báo Control
    Button btnDoiMatKhau;
    EditText edtEmail,edtMatKhauMoi,edtNhapLaiMK;
    BatLoi error = new BatLoi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quen_mat_khau);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Ánh Xạ ID
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhauMoi);
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtNhapLaiMK = findViewById(R.id.edtNhapLaiMK);

        //Khởi tạo DATABASE
        Database database = new Database(QuenMatKhau.this);
        database.createDatabase(); // Tạo cơ sở dữ liệu nếu chưa có

        SQLiteDatabase db = database.openDatabase(); // Khai báo cơ sở dữ liệu

        //Code Sự Kiện Click Button DoiMatKhau
        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lấy dữ liệu từ EDT
                String email = edtEmail.getText().toString();
                String matkhaumoi = edtMatKhauMoi.getText().toString();
                String nhaplaimk = edtNhapLaiMK.getText().toString();

                //Khai báo mảng tham số truyền vào
                String[] selectionArg = { email };

                //Câu lệnh SQL
                String sql = "SELECT * FROM taikhoan WHERE email = ?";
                //Con trỏ cursor thuc thi câu lệnh sql + tham số truyền vào
                Cursor cursor = db.rawQuery(sql,selectionArg);

                cursor.moveToFirst();
                // Lấy mã tài khoản vì UPDATE cần điều kiện WHERE mataikhoan = ?
                String mataikhoan = cursor.getString(0);

                // Đếm dòng dữ liệu nếu = 0 => email chưa tồn tại
                int cursorCount = cursor.getCount();
                if(cursorCount == 0){
                    edtEmail.setError("Email không tồn tại");
                }
                else{
                    // Kiểm tra mật khẩu đã đúng định dạng chưa
                    if(!error.kiemTraMatKhau(matkhaumoi)){
                        edtMatKhauMoi.setError("Mật khẩu gồm 6-20 kí tự chữ hoặc số!");
                        return;
                    }

                    // Kiểm Tra EDTnhaplaimatkhau có khớp với mật khẩu không
                    if (nhaplaimk.equals(matkhaumoi)){
                        //Khai báo contentvalues để them dữ liệu vào SQLite
                        ContentValues cv = new ContentValues();

                        // Đẩy dữ liệu vào contentvalues
                        cv.put("matkhau",matkhaumoi);

                        // Biến result để thực thi lệnh sql + tham số truyền vào
                        long result = db.update("taikhoan",cv,"mataikhoan=?",new String[] {mataikhoan});

                        // Nếu result == -1 tức là UPDATE lỗi
                        if (result == -1){
                            Toast.makeText(QuenMatKhau.this, "Đổi Mật Khẩu Thất Bại", Toast.LENGTH_SHORT).show();
                        }else{
                            //Làm các ô EDT trống sau khi đổi thành công
                            edtMatKhauMoi.setText("");
                            edtEmail.setText("");
                            edtNhapLaiMK.setText("");

                            //Tạo thong báo
                            AlertDialog.Builder builder = new AlertDialog.Builder(QuenMatKhau.this);
                            builder.setTitle("Thông báo");
                            builder.setMessage("Đổi mật khẩu thành công, bạn có muốn về trang đăng nhập?");
                            builder.setCancelable(false); //Không cho phép thoát bằng cách nhấn ngoài
                            builder.setPositiveButton("Quay về trang Đăng Nhập", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // code chuyển hướng đến trang đăng nhập
                                    startActivity(new Intent(QuenMatKhau.this,DangNhap.class));
                                }
                            });
                            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                            //Khởi tạo và hiển thị thông báo Dialog
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }else{
                        edtNhapLaiMK.setError("Mật khẩu không khớp!");
                    }
                }
                cursor.close();
            }
        });
    }

    public void login(View view) {
        startActivity(new Intent(QuenMatKhau.this, DangNhap.class));
    }
}