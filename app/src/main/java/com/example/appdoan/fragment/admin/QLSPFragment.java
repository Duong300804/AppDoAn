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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.adapter.SanPhamAdapterAdmin;
import com.example.appdoan.object.SanPham;
import com.example.appdoan.tool.Database;

import java.util.ArrayList;

public class QLSPFragment extends Fragment {
    Button btnThemSanPham;
    EditText edtTimKiem;
    ListView lvSanPham;
    ArrayList<SanPham> listSanPham;
    ArrayAdapter<SanPham> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_l_s_p,container,false);

        btnThemSanPham = view.findViewById(R.id.btnThemSanPham);
        edtTimKiem = view.findViewById(R.id.edtTimKiem);
        lvSanPham = view.findViewById(R.id.lvSanPham);
        listSanPham = new ArrayList<>();

        // Khởi tạo Database
        Database database = new Database(requireActivity());
        database.createDatabase(); // Tạo cơ sở dữ liệu nếu chưa có
        SQLiteDatabase db = database.openDatabase(); // Khai báo cơ sở dữ liệu

        Cursor cursor = db.rawQuery("SELECT * FROM sanpham",null);

        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                String maSanPham = cursor.getString(0);
                String maDanhMuc = cursor.getString(1);
                String tenSanPham = cursor.getString(2);
                byte[] anhSanPham = cursor.getBlob(3);
                int soLuong = cursor.getInt(4);
                int gia = cursor.getInt(5);
                String moTa = cursor.getString(6);

                listSanPham.add(new SanPham(maSanPham,maDanhMuc,tenSanPham,anhSanPham,soLuong,gia,moTa));
            }
        }
        cursor.close();

        adapter = new SanPhamAdapterAdmin(getActivity(),R.layout.item_sanphamadmin,listSanPham);
        lvSanPham.setAdapter(adapter);

        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo Fragment
                ThemSanPham themSanPham = new ThemSanPham();

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.FL, themSanPham).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Lấy sản phẩm đã được chọn từ danh sách
                SanPham sanPhamChon = listSanPham.get(position);

                // Khởi tạo Fragment
                SuaSanPham suaSanPham = new SuaSanPham();

                // Tạo Bundle và truyền mã sản phẩm
                Bundle bundle = new Bundle();
                bundle.putString("maSanPham", sanPhamChon.getMaSanPham()); // Thay đổi thành getter phù hợp
                suaSanPham.setArguments(bundle);

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.FL, suaSanPham).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listSanPham.clear(); // Xóa danh sách cũ trước khi tải mới
                String timKiem = charSequence.toString();

                Cursor cursor;
                if(timKiem.isEmpty()) {
                    // Truy vấn tất cả sản phẩm nếu không có từ khóa tìm kiếm
                    cursor = db.rawQuery("SELECT * FROM sanpham", null);
                } else {
                    // Truy vấn sản phẩm dựa trên từ khóa tìm kiếm
                    cursor = db.rawQuery("SELECT * FROM sanpham WHERE tenSanPham LIKE ?", new String[]{"%" + timKiem + "%"});
                }

                if (cursor.getCount() == 0) {
                } else {
                    while (cursor.moveToNext()) {
                        String maSanPham = cursor.getString(0);
                        String maDanhMuc = cursor.getString(1);
                        String tenSanPham = cursor.getString(2);
                        byte[] anhSanPham = cursor.getBlob(3);
                        int soLuong = cursor.getInt(4);
                        int gia = cursor.getInt(5);
                        String moTa = cursor.getString(6);

                        listSanPham.add(new SanPham(maSanPham, maDanhMuc, tenSanPham, anhSanPham, soLuong, gia, moTa));
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