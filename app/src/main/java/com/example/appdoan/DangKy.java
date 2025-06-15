package com.example.appdoan;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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

public class DangKy extends AppCompatActivity {
    //Khai báo control
    Button btnDangKy;
    EditText edtTenDangNhap,edtEmail,edtMatKhau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Database
        Database database = new Database(DangKy.this);
        database.createDatabase(); // Tạo cơ sở dữ liệu nếu chưa có

        SQLiteDatabase db = database.openDatabase(); //Khai báo cơ sở dữ liệu

        //Ánh xạ ID
        btnDangKy = findViewById(R.id.btnDangKy);
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu trong EDT
                String tenDangNhap = edtTenDangNhap.getText().toString();
                String email = edtEmail.getText().toString();
                String matKhau = edtMatKhau.getText().toString();

                //Khai báo đối tượng ERROR để bắt lỗi, xem chi tiết trong class BatLoi
                BatLoi error = new BatLoi();
                if(!error.kiemTraTenDangNhap(tenDangNhap)){
                    edtTenDangNhap.setError("Tên đăng nhập gồm 6-20 kí tự gồm chữ hoặc số");
                    return;
                }
                if(!error.kiemTraTenDangNhapTonTai(tenDangNhap, db)){
                    edtTenDangNhap.setError("Tên đăng nhập đã tồn tại");
                    return;
                }
                if(!error.kiemTraEmail(email)){
                    edtEmail.setError("Email không hợp lệ");
                    return;
                }
                if(!error.kiemTraEmailTonTai(email, db)){
                    edtEmail.setError("Email đã tồn tại");
                    return;
                }
                if(!error.kiemTraMatKhau(matKhau)){
                    edtMatKhau.setError("Mật khẩu gồm 6-20 kí tự gồm chữ hoặc số");
                    return;
                }

                //Khai báo content values để truyền những thứ cần thêm vào SQLite
                ContentValues cv = new ContentValues();

                //Đẩy dữ lieu vào contentvalues
                cv.put("tentaikhoan",tenDangNhap);
                cv.put("email",email);
                cv.put("matkhau",matKhau);
                cv.put("loaitaikhoan","user");

                //Khai báo biến Result để thực thi câu lệnh SQL
                long result = db.insert("taikhoan",null,cv);

                //Nếu Result == -1 tức là thêm không thành công
                if (result == -1){
                    Toast.makeText(DangKy.this, "Đăng Ký Thất Bại", Toast.LENGTH_SHORT).show();
                }else {
                    //Xoá dữ liệu các ô EDT sau khi đăng ký thành công
                    edtMatKhau.setText("");
                    edtEmail.setText("");
                    edtTenDangNhap.setText("");

                    //Tạo thông báo
                    AlertDialog.Builder builder = new AlertDialog.Builder(DangKy.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Đăng ký thành công. Bạn có muốn đăng nhập lại không?");
                    builder.setCancelable(false); //Không cho phép thoát bằng cách nhấn ngoài

                    //Trường Hợp người dùng bấm "Quay về trang Đăng nhập"
                    builder.setPositiveButton("Quay về trang Đăng Nhập", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // code chuyển hướng đến trang đăng nhập
                            startActivity(new Intent(DangKy.this,DangNhap.class));
                        }
                    });

                    //Trường Hợp người dùng bấm "Quay về trang Đăng Ký"
                    builder.setNegativeButton("Ở lại trang Đăng Ký", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    //Khởi tạo và hiển thị hộp thoại Dialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    public void login(View view) {
        startActivity(new Intent(DangKy.this, DangNhap.class));
    }
}