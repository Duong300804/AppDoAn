package com.example.appdoan.fragment.admin;

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

import com.example.appdoan.R;
import com.example.appdoan.adapter.DanhGiaAdapterAdmin;
import com.example.appdoan.object.DanhGia;
import com.example.appdoan.object.DanhMuc;
import com.example.appdoan.tool.Database;

import java.util.ArrayList;

public class QLDGFragment extends Fragment {
    ListView lvDanhGia;
    ArrayAdapter<DanhGia> arrayAdapter;
    ArrayList<DanhGia> listDanhGia;
    EditText edtTimKiem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_l_d_g, container, false);

        lvDanhGia = view.findViewById(R.id.lvDanhGia);
        edtTimKiem = view.findViewById(R.id.edtTimkiemDH);
        listDanhGia = new ArrayList<>();

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM danhgia",null);
        if(cursor.moveToFirst()){
            do {
                int maDanhGia = cursor.getInt(1);
                int maDonHang = cursor.getInt(0);
                int soSao = cursor.getInt(2);
                String danhGia = cursor.getString(3);

                listDanhGia.add(new DanhGia(maDanhGia,maDonHang,soSao,danhGia));
            }while (cursor.moveToNext());
        }

        arrayAdapter = new DanhGiaAdapterAdmin(requireActivity(),R.layout.item_danhgiaadmin,listDanhGia);
        lvDanhGia.setAdapter(arrayAdapter);

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listDanhGia.clear();
                String timKiem = charSequence.toString();

                Cursor cursor;
                if(timKiem.isEmpty()) {
                    cursor = db.rawQuery("SELECT * FROM danhgia", null);
                } else {
                    cursor = db.rawQuery("SELECT * FROM danhgia WHERE madonhang LIKE ?", new String[]{"%" + timKiem + "%"});
                }

                if (cursor.getCount() == 0) {
                } else {
                    while (cursor.moveToNext()) {
                        int maDanhGia = cursor.getInt(1);
                        int maDonHang = cursor.getInt(0);
                        int soSao = cursor.getInt(2);
                        String danhGia = cursor.getString(3);

                        listDanhGia.add(new DanhGia(maDanhGia,maDonHang,soSao,danhGia));
                    }
                }
                cursor.close();
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }
}