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
import com.example.appdoan.adapter.DanhMucAdapterAdmin;
import com.example.appdoan.object.DanhMuc;
import com.example.appdoan.tool.Database;

import java.util.ArrayList;

public class QLDMFragment extends Fragment {
    Button btnThemDanhMuc;
    EditText edtTimKiem;
    ListView lvDanhMuc;
    ArrayList<DanhMuc> listDanhMuc;
    ArrayAdapter<DanhMuc> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_l_d_m,container,false);

        btnThemDanhMuc = view.findViewById(R.id.btnthemDM);
        edtTimKiem = view.findViewById(R.id.edtTimkiemDM);
        lvDanhMuc = view.findViewById(R.id.lvDanhMuc);
        listDanhMuc = new ArrayList<>();

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM danhmuc",null);

        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                String maDanhMuc = cursor.getString(0);
                String tenDanhMuc = cursor.getString(1);
                byte[] anhDanhMuc = cursor.getBlob(2);
                listDanhMuc.add(new DanhMuc(maDanhMuc,tenDanhMuc,anhDanhMuc));
            }
        }
        cursor.close();

        adapter = new DanhMucAdapterAdmin(getActivity(),R.layout.item_danhmucadmin,listDanhMuc);
        lvDanhMuc.setAdapter(adapter);

        btnThemDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemDanhMuc themDanhMuc = new ThemDanhMuc();

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.FL, themDanhMuc).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        lvDanhMuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                DanhMuc danhMucChon = listDanhMuc.get(position);

                SuaDanhMuc suaDanhMuc = new SuaDanhMuc();

                Bundle bundle = new Bundle();
                bundle.putString("maDanhMuc", danhMucChon.getMaDanhMuc());
                bundle.putString("tenDanhMuc", danhMucChon.getTenDanhMuc());
                bundle.putByteArray("anhDanhMuc", danhMucChon.getAnhDanhMuc());
                suaDanhMuc.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.FL, suaDanhMuc).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listDanhMuc.clear();
                String timKiem = charSequence.toString();

                Cursor cursor;
                if(timKiem.isEmpty()) {

                    cursor = db.rawQuery("SELECT * FROM danhmuc", null);
                } else {

                    cursor = db.rawQuery("SELECT * FROM danhmuc WHERE tenDanhMuc LIKE ?", new String[]{"%" + timKiem + "%"});
                }

                if (cursor.getCount() == 0) {
                } else {
                    while (cursor.moveToNext()) {
                        String maDanhMuc = cursor.getString(0);
                        String tenDanhMuc = cursor.getString(1);
                        byte[] anhDanhMuc = cursor.getBlob(2);

                        listDanhMuc.add(new DanhMuc(maDanhMuc, tenDanhMuc,anhDanhMuc));
                    }
                }
                cursor.close();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }
}