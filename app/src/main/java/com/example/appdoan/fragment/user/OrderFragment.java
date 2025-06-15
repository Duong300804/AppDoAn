package com.example.appdoan.fragment.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.adapter.DonHangAdapterUser;
import com.example.appdoan.object.DonHang;
import com.example.appdoan.object.SanPham;
import com.example.appdoan.tool.Database;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    ListView lvDonHang;
    ArrayList<DonHang> listDonHang;
    ArrayAdapter<DonHang> donHangArrayAdapter;
    EditText edtTimKiem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        lvDonHang = view.findViewById(R.id.lvdonhang);
        edtTimKiem = view.findViewById(R.id.edtTimKiem);
        listDonHang = new ArrayList<>();

        // Lấy mã tài khoản từ SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("mataikhoan", Context.MODE_PRIVATE);
        int mataikhoan = sharedPref.getInt("mataikhoan", -1); // -1 là giá trị mặc định nếu không tìm thấy "mataikhoan"

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        String[] selectionArg = {String.valueOf(mataikhoan)};

        Cursor cursor = db.rawQuery("SELECT * FROM donhang WHERE mataikhoan=?",selectionArg);
        if (cursor.moveToFirst()){
            do {
                if (cursor.getCount() == 0){
                    Toast.makeText(requireActivity(), "Hiện tại bạn chưa có đơn hàng nào", Toast.LENGTH_SHORT).show();
                }else{
                    int maDonHang = cursor.getInt(0);
                    int maTaiKhoan = cursor.getInt(1);
                    String danhSachSanPham = cursor.getString(2);
                    int tongTien = cursor.getInt(3);
                    String ngayMua = cursor.getString(4);
                    String trangThai = cursor.getString(5);

                    listDonHang.add(new DonHang(maDonHang,maTaiKhoan,danhSachSanPham,tongTien,ngayMua,trangThai));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();

        donHangArrayAdapter = new DonHangAdapterUser(requireActivity(),R.layout.item_orderuser,listDonHang);
        lvDonHang.setAdapter(donHangArrayAdapter);

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listDonHang.clear(); // Xóa danh sách cũ trước khi tải mới
                String timKiem = charSequence.toString();

                Cursor cursor;
                if(timKiem.isEmpty()) {
                    // Truy vấn tất cả sản phẩm nếu không có từ khóa tìm kiếm
                    cursor = db.rawQuery("SELECT * FROM donhang", null);
                } else {
                    // Truy vấn sản phẩm dựa trên từ khóa tìm kiếm
                    cursor = db.rawQuery("SELECT * FROM donhang WHERE madonhang LIKE ?", new String[]{"%" + timKiem + "%"});
                }

                if (cursor.getCount() == 0) {
                } else {
                    while (cursor.moveToNext()) {
                        int maDonHang = cursor.getInt(0);
                        int maTaiKhoan = cursor.getInt(1);
                        String danhSachSanPham = cursor.getString(2);
                        int tongTien = cursor.getInt(3);
                        String ngayMua = cursor.getString(4);
                        String trangThai = cursor.getString(5);

                        listDonHang.add(new DonHang(maDonHang,maTaiKhoan,danhSachSanPham,tongTien,ngayMua,trangThai));
                    }
                }
                cursor.close();

                // Cập nhật lại adapter với danh sách mới
                donHangArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }
}