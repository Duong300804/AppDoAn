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
import com.example.appdoan.adapter.TaiKhoanAdapterAdmin;
import com.example.appdoan.object.TaiKhoan;
import com.example.appdoan.tool.Database;

import java.util.ArrayList;

public class QLTKFragment extends Fragment {
    Button btnThemTaiKhoan;
    EditText edtTimKiem;
    ListView lvTaiKhoan;
    ArrayList<TaiKhoan> listTaiKhoan;
    ArrayAdapter<TaiKhoan> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_l_t_k,container,false);

        btnThemTaiKhoan = view.findViewById(R.id.btnthemTK);
        edtTimKiem = view.findViewById(R.id.edtTimkiemTK);
        lvTaiKhoan = view.findViewById(R.id.lvTaiKhoan);
        listTaiKhoan = new ArrayList<>();

        // Khởi tạo Database
        Database database = new Database(requireActivity());
        database.createDatabase(); // Tạo cơ sở dữ liệu nếu chưa có
        SQLiteDatabase db = database.openDatabase(); // Khai báo cơ sở dữ liệu

        Cursor cursor = db.rawQuery("SELECT * FROM taikhoan",null);

        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                String maTaiKhoan = cursor.getString(0);
                String tenTaiKhoan = cursor.getString(1);
                String matKhau = cursor.getString(2);
                String email = cursor.getString(3);
                String loaiTaiKhoan = cursor.getString(4);

                listTaiKhoan.add(new TaiKhoan(maTaiKhoan,tenTaiKhoan,matKhau,email,loaiTaiKhoan));
            }
        }
        cursor.close();

        adapter = new TaiKhoanAdapterAdmin(getActivity(),R.layout.item_taikhoan,listTaiKhoan);
        lvTaiKhoan.setAdapter(adapter);

        btnThemTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo Fragment
                ThemTaiKhoan themTaiKhoan = new ThemTaiKhoan();

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.FL, themTaiKhoan).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        lvTaiKhoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Lấy tài khoản đã được chọn từ danh sách
                TaiKhoan taiKhoanChon = listTaiKhoan.get(position);

                // Khởi tạo Fragment
                SuaTaiKhoan suaTaiKhoan = new SuaTaiKhoan();

                // Tạo Bundle và truyền mã tài khoản
                Bundle bundle = new Bundle();
                bundle.putString("maTaiKhoan", taiKhoanChon.getMaTaiKhoan()); // Thay đổi thành getter phù hợp
                bundle.putString("tenTaiKhoan", taiKhoanChon.getTenTaiKhoan());
                bundle.putString("matKhau", taiKhoanChon.getMatKhau());
                bundle.putString("email",taiKhoanChon.getEmail());
                bundle.putString("loaiTaiKhoan",taiKhoanChon.getLoaiTaiKhoan());
                suaTaiKhoan.setArguments(bundle);

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.FL, suaTaiKhoan).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listTaiKhoan.clear(); // Xóa danh sách cũ trước khi tải mới
                String timKiem = charSequence.toString();
                Cursor cursor;
                if(timKiem.isEmpty()) {
                    // Truy vấn tất cả tài khoản nếu không có từ khóa tìm kiếm
                    cursor = db.rawQuery("SELECT * FROM taikhoan", null);
                } else {
                    // Truy vấn tài khoản dựa trên từ khóa tìm kiếm
                    cursor = db.rawQuery("SELECT * FROM taikhoan WHERE tenTaiKhoan LIKE ?", new String[]{"%" + timKiem + "%"});
                }

                if (cursor.getCount() == 0) {
                } else {
                    while (cursor.moveToNext()) {
                        String maTaiKhoan = cursor.getString(0);
                        String tenTaiKhoan = cursor.getString(1);
                        String matKhau = cursor.getString(2);
                        String email = cursor.getString(3);
                        String loaiTaiKhoan = cursor.getString(4);

                        listTaiKhoan.add(new TaiKhoan(maTaiKhoan,tenTaiKhoan,matKhau,email,loaiTaiKhoan));
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