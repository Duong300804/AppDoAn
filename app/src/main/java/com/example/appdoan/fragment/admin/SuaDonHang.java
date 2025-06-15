package com.example.appdoan.fragment.admin;

import android.content.ContentValues;
import android.database.Cursor;
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

public class SuaDonHang extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    Button btnHuySua,btnLuu;
    EditText edtsuaMaDH,edtsuaMaTK,edtsuaTongTien,edtSuaDanhSachSanPham,edtSuaNgayMua;
    int maDonHang = 0;
    Spinner spinnerTrangThai;
    String trangthai = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sua_don_hang,container,false);

        btnHuySua = view.findViewById(R.id.btnHuysua);
        btnLuu = view.findViewById(R.id.btnLuusua);
        edtsuaMaDH = view.findViewById(R.id.edtsuaMaDH);
        edtsuaMaTK = view.findViewById(R.id.edtsuaMaTK);
        edtsuaTongTien = view.findViewById(R.id.edtsuaTongTien);
        edtSuaDanhSachSanPham = view.findViewById(R.id.edtSuaDanhSachSanPham);
        edtSuaNgayMua = view.findViewById(R.id.edtSuaNgayMua);
        spinnerTrangThai = view.findViewById(R.id.spinnerTrangThai);

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        // Lấy mã đơn hàng từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            maDonHang = bundle.getInt("maDonHang");
            edtsuaMaDH.setText(String.valueOf(maDonHang));
            String[] selectionArg = {String.valueOf(maDonHang)};

            Cursor cursor = db.rawQuery("SELECT * FROM donhang WHERE madonhang = ?", selectionArg);
            if (cursor.moveToFirst()) {
                // Không cần sử dụng do-while, chỉ cần sử dụng if vì chỉ có một bản ghi
                // Chuyển giá trị từ cursor vào các EditText
                edtsuaMaDH.setText(String.valueOf(cursor.getInt(0)));
                edtsuaMaTK.setText(String.valueOf(cursor.getInt(1)));
                edtSuaDanhSachSanPham.setText(cursor.getString(2));
                edtsuaTongTien.setText(String.valueOf(cursor.getInt(3)));
                edtSuaNgayMua.setText(cursor.getString(4));
                trangthai = cursor.getString(5);

                //khóa các ô edittext
                edtsuaMaDH.setEnabled(false);
                edtsuaMaTK.setEnabled(false);
                edtsuaTongTien.setEnabled(false);
                edtSuaNgayMua.setEnabled(false);
                edtSuaDanhSachSanPham.setEnabled(false);
            }
            cursor.close();
        }

        loadtrangthai();

        btnHuySua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo Fragment
                QLDHFragment qldhFragment = new QLDHFragment();

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.FL, qldhFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });


        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = db.rawQuery("SELECT trangthai FROM donhang WHERE madonhang = ?",new String[] {String.valueOf(maDonHang)});
                if (cursor.moveToFirst()) {
                    do {
                        trangthai = cursor.getString(0); // Lấy trạng thái
                    } while (cursor.moveToNext());
                }
                String trangThai = spinnerTrangThai.getSelectedItem().toString();

                ContentValues cv = new ContentValues();
                cv.put("trangthai",trangThai);

                long result = db.update("donhang",cv,"madonhang=?",new String[] {String.valueOf(maDonHang)});
                if (result == -1){
                    Toast.makeText(requireActivity(), "Sửa dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireActivity(), "Sửa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    // Khởi tạo Fragment
                    QLDHFragment qldhFragment = new QLDHFragment();

                    // Lấy FragmentManager và bắt đầu giao dịch
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                    // Thay thế Fragment hiện tại bằng Fragment mới
                    transaction.replace(R.id.FL, qldhFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
                }
            }
        });
        return view;
    }

    private void loadtrangthai() {
        // Tạo danh sách để chứa tên danh mục
        List<String> listTrangThai = new ArrayList<>();
        listTrangThai.add("Hoàn Thành"); // Thêm vào danh sách
        listTrangThai.add("Đang Giao"); // Thêm vào danh sách
        listTrangThai.add("Đã Giao"); // Thêm vào danh sách

        // Tạo adapter và gán cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, listTrangThai);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(adapter); // Gán adapter cho Spinner

        int position = adapter.getPosition(trangthai);
        if (position >= 0) { // Kiểm tra nếu position hợp lệ
            spinnerTrangThai.setSelection(position);
        }
    }
}