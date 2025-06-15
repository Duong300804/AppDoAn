package com.example.appdoan.fragment.admin;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.tool.Database;

import java.util.ArrayList;
import java.util.List;

public class ThemTaiKhoan extends Fragment {
    Button btnHuy,btnLuu;
    EditText edtTenTK,edtMatKhau,edtEmial;
    Spinner spinnerLoaiTaiKhoan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them_tai_khoan, container, false);

        btnHuy = view.findViewById(R.id.btnHuyTK);
        btnLuu = view.findViewById(R.id.btnLuuTK);

        edtTenTK = view.findViewById(R.id.edtthemTenTK);
        edtMatKhau = view.findViewById(R.id.edtthemMatKhauTK);
        edtEmial = view.findViewById(R.id.edtthemEmail);
        spinnerLoaiTaiKhoan = view.findViewById(R.id.spinnerLoaiTaiKhoan);

        ArrayList<String> listloaitaikhoan = new ArrayList<>();
        listloaitaikhoan.add("user");
        listloaitaikhoan.add("admin");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, listloaitaikhoan);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiTaiKhoan.setAdapter(adapter); // Gán adapter cho Spinner

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo Fragment
                QLTKFragment qltkFragment = new QLTKFragment();

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.FL, qltkFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenTaiKhoan = edtTenTK.getText().toString().trim();
                String matKhau = edtMatKhau.getText().toString().trim();
                String email = edtEmial.getText().toString().trim();
                String loaiTaiKhoan= spinnerLoaiTaiKhoan.getSelectedItem().toString();


                ContentValues cv = new ContentValues();
                cv.put("tentaikhoan",tenTaiKhoan);
                cv.put("matkhau",matKhau);
                cv.put("email",email);
                cv.put("loaitaikhoan",loaiTaiKhoan);


                long result = db.insert("taikhoan",null,cv);
                if (result == -1){
                    Toast.makeText(requireActivity(), "Thêm Tài Khoản Thất Bại", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireActivity(), "Thêm Tài Khoản Thành Công", Toast.LENGTH_SHORT).show();
                    edtTenTK.setText("");
                    edtMatKhau.setText("");
                    edtEmial.setText("");
                }
            }
        });

        return view;
    }
}