package com.example.appdoan.fragment.user;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.tool.Database;

public class MeFragment extends Fragment {
    EditText edtHoVaTen,edtNgaySinh,edtSoDienThoai,edtCCCD,edtDiaChi;
    Button btnCapNhat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        edtHoVaTen = view.findViewById(R.id.edtHoVaTen);
        edtNgaySinh = view.findViewById(R.id.edtNgaySinh);
        edtCCCD = view.findViewById(R.id.edtCCCD);
        edtSoDienThoai = view.findViewById(R.id.edtSoDienThoai);
        edtDiaChi = view.findViewById(R.id.edtDiaChi);

        btnCapNhat = view.findViewById(R.id.btnCapNhat);

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        // Lấy mã tài khoản từ SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("mataikhoan", Context.MODE_PRIVATE);
        int mataikhoan = sharedPref.getInt("mataikhoan", -1); // -1 là giá trị mặc định nếu không tìm thấy "mataikhoan"

        Cursor cursor = db.rawQuery("SELECT * FROM thongtintaikhoan WHERE mataikhoan =?",new String[] {String.valueOf(mataikhoan)});
        if(cursor.moveToFirst()){
            do{
                String hoVaTen = cursor.getString(1);
                String soDienThoai = cursor.getString(2);
                String ngaySinh = cursor.getString(3);
                String cccd = cursor.getString(4);
                String diaChi = cursor.getString(5);

                edtHoVaTen.setText(hoVaTen);
                edtSoDienThoai.setText(soDienThoai);
                edtNgaySinh.setText(ngaySinh);
                edtCCCD.setText(cccd);
                edtDiaChi.setText(diaChi);

            }while (cursor.moveToNext());
        }

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = db.rawQuery("SELECT * FROM thongtintaikhoan WHERE mataikhoan=?",new String[] {String.valueOf(mataikhoan)});
                if (cursor.getCount() == 0){
                    ContentValues cv = new ContentValues();
                    
                    cv.put("mataikhoan",mataikhoan);
                    cv.put("hovaten",edtHoVaTen.getText().toString());
                    cv.put("sodienthoai",edtSoDienThoai.getText().toString());
                    cv.put("ngaysinh",edtNgaySinh.getText().toString());
                    cv.put("cccd",edtCCCD.getText().toString());
                    cv.put("diachi",edtDiaChi.getText().toString());
                    
                    long result = db.insert("thongtintaikhoan",null,cv);
                    if (result == -1){
                        Toast.makeText(requireActivity(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(requireActivity(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    }
                    
                }else{
                    ContentValues cv = new ContentValues();

                    cv.put("hovaten",edtHoVaTen.getText().toString());
                    cv.put("sodienthoai",edtSoDienThoai.getText().toString());
                    cv.put("ngaysinh",edtNgaySinh.getText().toString());
                    cv.put("cccd",edtCCCD.getText().toString());
                    cv.put("diachi",edtDiaChi.getText().toString());

                    long result = db.update("thongtintaikhoan",cv,"mataikhoan=?",new String[] {String.valueOf(mataikhoan)});
                    if (result == -1){
                        Toast.makeText(requireActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(requireActivity(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }
}