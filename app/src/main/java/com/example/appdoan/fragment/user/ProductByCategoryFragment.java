package com.example.appdoan.fragment.user;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

public class ProductByCategoryFragment extends Fragment {
    GridView grSanPham;
    ArrayAdapter<SanPham> adapter;
    ArrayList<SanPham> listSanPham;

    RecyclerView recyclerDanhMuc;
    DanhMucAdapterUser danhMucAdapter;
    ArrayList<DanhMuc> listDanhMuc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_by_category, container, false);
        // Inflate the layout for this fragment
        grSanPham = view.findViewById(R.id.grSanPham);
        listSanPham = new ArrayList<>();

        listDanhMuc = new ArrayList<>();
        TextView tvTenDanhMuc = view.findViewById(R.id.tvTenDanhMuc);
        // Lấy mã danh mục từ Bundle
        Bundle bundle = getArguments();
        String maDanhMuc = bundle != null ? bundle.getString("maDanhMuc") : null;

        // Khởi tạo Database
        Database database = new Database(requireActivity());
        database.createDatabase(); // Tạo cơ sở dữ liệu nếu chưa có
        SQLiteDatabase db = database.openDatabase(); // Khai báo cơ sở dữ liệu
        // Truy vấn tên danh mục theo mã danh mục
        Cursor cursorDM = db.rawQuery("SELECT tenDanhMuc FROM danhmuc WHERE maDanhMuc = ?", new String[]{maDanhMuc});
        if (cursorDM.moveToFirst()) {
            String tenDanhMuc = cursorDM.getString(0);
            tvTenDanhMuc.setText(tenDanhMuc); // Hiển thị tên danh mục lên TextView
        }
        cursorDM.close();
        // Truy vấn các sản phẩm theo mã danh mục
        Cursor cursorSP = db.rawQuery("SELECT * FROM sanpham WHERE maDanhMuc = ?", new String[]{maDanhMuc});

        if (cursorSP.getCount() == 0) {
            Toast.makeText(getActivity(), "Không có sản phẩm nào thuộc danh mục này!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursorSP.moveToNext()) {
                String maSanPham = cursorSP.getString(0);
                String tenSanPham = cursorSP.getString(2);
                byte[] anhSanPham = cursorSP.getBlob(3);
                int soLuong = cursorSP.getInt(4);
                int gia = cursorSP.getInt(5);
                String moTa = cursorSP.getString(6);

                listSanPham.add(new SanPham(maSanPham, maDanhMuc, tenSanPham, anhSanPham, soLuong, gia, moTa));
            }
        }
        cursorSP.close();

        // Thiết lập adapter và GridView
        adapter = new SanPhamAdapterUser(requireActivity(), R.layout.item_sanphamuser, listSanPham);
        grSanPham.setAdapter(adapter);

        // Sự kiện click vào sản phẩm
        grSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SanPham sanPhamChon = listSanPham.get(position);
                ProductDetailFragment productDetailFragment = new ProductDetailFragment();

                Bundle bundle = new Bundle();
                bundle.putString("maSanPham", sanPhamChon.getMaSanPham());
                productDetailFragment.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, productDetailFragment).addToBackStack(null).commit();
            }
        });
        return view;
    }
}