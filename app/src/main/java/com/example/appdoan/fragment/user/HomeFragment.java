package com.example.appdoan.fragment.user;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.adapter.DanhMucAdapterUser;
import com.example.appdoan.adapter.SanPhamAdapterUser;
import com.example.appdoan.object.DanhMuc;
import com.example.appdoan.object.SanPham;
import com.example.appdoan.tool.Database;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerDanhMuc;
    DanhMucAdapterUser danhMucAdapter;
    ArrayList<DanhMuc> listDanhMuc;

    GridView grSanPham;
    ArrayAdapter<SanPham> adapter;
    ArrayList<SanPham> listSanPham;
    TextView tvxemtatcasp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        grSanPham = view.findViewById(R.id.grSanPham);
        recyclerDanhMuc = view.findViewById(R.id.recyclerDanhMuc);

        tvxemtatcasp = view.findViewById(R.id.tvxemtatcasp);

        listSanPham = new ArrayList<>();
        listDanhMuc = new ArrayList<>();

        // Khởi tạo Database
        Database database = new Database(requireActivity());
        database.createDatabase(); // Tạo cơ sở dữ liệu nếu chưa có
        SQLiteDatabase db = database.openDatabase(); // Khai báo cơ sở dữ liệu

        Cursor cursorDM = db.rawQuery("SELECT * FROM danhmuc", null);

        if (cursorDM.getCount() == 0) {
            Toast.makeText(getActivity(), "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursorDM.moveToNext()) {
                String maDanhMuc = cursorDM.getString(0);
                String tenDanhMuc = cursorDM.getString(1);
                byte[] anhDanhMuc = cursorDM.getBlob(2);

                listDanhMuc.add(new DanhMuc(maDanhMuc, tenDanhMuc, anhDanhMuc));
            }
        }
        cursorDM.close();
        // Thiết lập layout cho RecyclerView (LinearLayoutManager theo chiều ngang)
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerDanhMuc.setLayoutManager(layoutManager);

        // Thiết lập adapter cho RecyclerView
        danhMucAdapter = new DanhMucAdapterUser(requireActivity(), R.layout.item_danhmucuser, listDanhMuc);
        recyclerDanhMuc.setAdapter(danhMucAdapter);


        Cursor cursorSP = db.rawQuery("SELECT * FROM sanpham",null);

        if(cursorSP.getCount() == 0){
            Toast.makeText(getActivity(), "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursorSP.moveToNext()){
                String maSanPham = cursorSP.getString(0);
                String maDanhMuc = cursorSP.getString(1);
                String tenSanPham = cursorSP.getString(2);
                byte[] anhSanPham = cursorSP.getBlob(3);
                int soLuong = cursorSP.getInt(4);
                int gia = cursorSP.getInt(5);
                String moTa = cursorSP.getString(6);

                listSanPham.add(new SanPham(maSanPham, maDanhMuc, tenSanPham, anhSanPham, soLuong, gia, moTa));
            }
        }
        cursorSP.close();

        adapter = new SanPhamAdapterUser(requireActivity(),R.layout.item_sanphamuser,listSanPham);
        grSanPham.setAdapter(adapter);

        grSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,int position, long id) {
                // Lấy sản phẩm đã được chọn từ danh sách
                SanPham sanPhamChon = listSanPham.get(position);
                // Khởi tạo Fragment
                ProductDetailFragment productDetailFragment = new ProductDetailFragment();

                // Tạo Bundle và truyền mã sản phẩm
                Bundle bundle = new Bundle();
                bundle.putString("maSanPham", sanPhamChon.getMaSanPham());
                productDetailFragment.setArguments(bundle);

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.frame_layout, productDetailFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        tvxemtatcasp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Khởi tạo Fragment Tất cả sản phẩm
                AllProductsFragment allProductsFragment = new AllProductsFragment();
                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.frame_layout, allProductsFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        return view;

    }
}