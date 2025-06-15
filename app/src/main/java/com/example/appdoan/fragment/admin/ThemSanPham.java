package com.example.appdoan.fragment.admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.tool.BatLoi;
import com.example.appdoan.tool.Database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThemSanPham extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private byte[] anhSP; // Biến lưu trữ ảnh dưới dạng byte[]

    Button btnHuy,btnLuu,btnChonAnh;
    EditText edtMaSP,edtTenSP,edtGiaSP,edtSoLuong,edtMoTa;
    Spinner spinnerMaDM;
    ImageView anhSanPham;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them_san_pham, container, false);

        btnHuy = view.findViewById(R.id.btnHuySP);
        btnLuu = view.findViewById(R.id.btnLuuSP);
        btnChonAnh = view.findViewById(R.id.btnChonAnh);
        edtMaSP = view.findViewById(R.id.edtmaSP);
        edtTenSP = view.findViewById(R.id.edttenSP);
        edtGiaSP = view.findViewById(R.id.edtgiaSP);
        edtSoLuong = view.findViewById(R.id.edtSoluong);
        edtMoTa = view.findViewById(R.id.edtMota);
        spinnerMaDM = view.findViewById(R.id.spinnerMaDM);
        anhSanPham = view.findViewById(R.id.imgSanPham);

        loadDanhMuc();

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo Fragment
                QLSPFragment qlspFragment = new QLSPFragment();

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.FL, qlspFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String maSanPham = edtMaSP.getText().toString().trim();
                String tenSanPham = edtTenSP.getText().toString().trim();
                int gia = Integer.parseInt(edtGiaSP.getText().toString().trim());
                int soLuong = Integer.parseInt(edtSoLuong.getText().toString().trim());
                String moTa = edtMoTa.getText().toString().trim();
                String tenDanhMuc = spinnerMaDM.getSelectedItem().toString();

                String[] selectionArg = { tenDanhMuc };
                String maDanhMuc = "";

                Cursor cursor = db.rawQuery("SELECT madanhmuc FROM danhmuc WHERE tendanhmuc=?",selectionArg);
                if (cursor.moveToFirst()) {
                    do {
                        maDanhMuc = cursor.getString(0); // Lấy tên danh mục
                    } while (cursor.moveToNext());
                }

                ContentValues cv = new ContentValues();
                cv.put("masanpham",maSanPham);
                cv.put("madanhmuc",maDanhMuc);
                cv.put("tensanpham",tenSanPham);
                cv.put("anhsanpham",anhSP);
                cv.put("soluong",soLuong);
                cv.put("gia",gia);
                cv.put("mota",moTa);

                long result = db.insert("sanpham",null,cv);
                if (result == -1){
                    Toast.makeText(requireActivity(), "Thêm Sản Phẩm Thất Bại", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireActivity(), "Thêm Sản Phẩm Thành Công", Toast.LENGTH_SHORT).show();
                    edtMaSP.setText("");
                    edtTenSP.setText("");
                    edtGiaSP.setText("");
                    edtSoLuong.setText("");
                    edtMoTa.setText("");
                    anhSanPham.setImageDrawable(getResources().getDrawable(R.drawable.baseline_image_not_supported_24));
                }
            }
        });

        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở thư viện ảnh
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            // Lấy URI của ảnh được chọn
            Uri imageUri = data.getData();

            try {
                // Chuyển URI thành bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                // Hiển thị ảnh trong ImageView (nếu có)
                anhSanPham.setImageBitmap(bitmap);

                // Chuyển đổi bitmap thành byte[]
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                anhSP = stream.toByteArray(); // Lưu ảnh vào biến anhSP

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadDanhMuc() {
        Database database = new Database(requireActivity());
        SQLiteDatabase db = database.openDatabase();
        Cursor cursor = db.rawQuery("SELECT tendanhmuc FROM danhmuc", null);

        // Tạo danh sách để chứa tên danh mục
        List<String> danhMucList = new ArrayList<>();

        // Duyệt qua các bản ghi trong cursor và thêm vào danh sách
        if (cursor.moveToFirst()) {
            do {
                String tenDanhMuc = cursor.getString(0); // Lấy tên danh mục
                danhMucList.add(tenDanhMuc); // Thêm vào danh sách
            } while (cursor.moveToNext());
        }
        cursor.close(); // Đóng cursor sau khi sử dụng

        // Tạo adapter và gán cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, danhMucList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaDM.setAdapter(adapter); // Gán adapter cho Spinner
    }
}