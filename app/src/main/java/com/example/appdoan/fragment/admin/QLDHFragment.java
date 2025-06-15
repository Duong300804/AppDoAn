package com.example.appdoan.fragment.admin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.adapter.DonHangAdapterAdmin;
import com.example.appdoan.object.DonHang;
import com.example.appdoan.tool.Database;

import java.util.ArrayList;

public class QLDHFragment extends Fragment {
    EditText edtTimKiem;
    ListView lvDonHang;
    ArrayList<DonHang> listDonHang;
    ArrayAdapter<DonHang> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_l_d_h,container,false);
        edtTimKiem = view.findViewById(R.id.edtTimkiemDH);
        lvDonHang = view.findViewById(R.id.lvDonHang);
        listDonHang = new ArrayList<>();

        // Khởi tạo Database
        Database database = new Database(requireActivity());
        database.createDatabase(); // Tạo cơ sở dữ liệu nếu chưa có
        SQLiteDatabase db = database.openDatabase(); // Khai báo cơ sở dữ liệu

        Cursor cursor = db.rawQuery("SELECT * FROM donhang",null);

        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
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

        adapter = new DonHangAdapterAdmin(getActivity(),R.layout.item_donhangadmin,listDonHang);
        lvDonHang.setAdapter(adapter);


        lvDonHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Lấy tài khoản đã được chọn từ danh sách
                DonHang donHangChon = listDonHang.get(position);

                // Khởi tạo Fragment
                SuaDonHang suaDonHang = new SuaDonHang();

                // Tạo Bundle và truyền mã đơn hàng
                Bundle bundle = new Bundle();
                bundle.putInt("maDonHang", donHangChon.getMaDonHang()); // Thay đổi thành getter phù hợp

                suaDonHang.setArguments(bundle);

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.FL, suaDonHang).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

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
                    // Truy vấn tất cả tài khoản nếu không có từ khóa tìm kiếm
                    cursor = db.rawQuery("SELECT * FROM donhang", null);
                } else {
                    // Truy vấn tài khoản dựa trên từ khóa tìm kiếm
                    cursor = db.rawQuery("SELECT * FROM donhang WHERE maDonHang LIKE ?", new String[]{"%" + timKiem + "%"});
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }
}