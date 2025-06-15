package com.example.appdoan.fragment.admin;

import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.tool.BatLoiDanhMuc;
import com.example.appdoan.tool.Database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ThemDanhMuc extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private byte[] anhDM;
    Button btnHuy,btnLuu,btnChonAnhDM;
    EditText edtMaDM,edtTenDM;
    ImageView anhDanhMuc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them_danh_muc, container, false);

        btnChonAnhDM = view.findViewById(R.id.btnChonAnhDM);
        btnHuy = view.findViewById(R.id.btnHuyDM);
        btnLuu = view.findViewById(R.id.btnLuuDM);
        edtMaDM = view.findViewById(R.id.edtmaDM);
        edtTenDM = view.findViewById(R.id.edttenDM);
        anhDanhMuc = view.findViewById(R.id.imgDanhMuc);

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo Fragment
                QLDMFragment qldmFragment = new QLDMFragment();

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.FL, qldmFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });

        BatLoiDanhMuc batloi = new BatLoiDanhMuc();

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maDanhMuc = edtMaDM.getText().toString().trim();
                String tenDanhMuc = edtTenDM.getText().toString().trim();

                if(!batloi.kiemtraTenDanhMuc(tenDanhMuc)){
                    Toast.makeText(requireActivity(), "Tên danh mục không được để trống hoặc sai định dạng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!batloi.kiemtraanhDanhMuc(anhDM)){
                    Toast.makeText(requireActivity(), "Ảnh danh mục không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!batloi.kiemtramaDanhMuc(maDanhMuc)){
                    Toast.makeText(requireActivity(), "Mã danh mục không được để trống hoặc sai định dạng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!batloi.kiemTraMaDanhMucTonTai(maDanhMuc, db)){
                    Toast.makeText(requireActivity(), "Mã danh mục đã tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!batloi.kiemTraTenDanhMucTonTai(tenDanhMuc, db)){
                    Toast.makeText(requireActivity(), "Tên danh mục đã tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues cv = new ContentValues();
                cv.put("madanhmuc",maDanhMuc);
                cv.put("tendanhmuc",tenDanhMuc);
                cv.put("anhdanhmuc",anhDM);

                long result = db.insert("danhmuc",null,cv);
                if (result == -1){
                    Toast.makeText(requireActivity(), "Thêm Danh Mục Thất Bại", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireActivity(), "Thêm Danh Mục Thành Công", Toast.LENGTH_SHORT).show();
                    edtMaDM.setText("");
                    edtTenDM.setText("");
                    anhDanhMuc.setImageDrawable(getResources().getDrawable(R.drawable.baseline_image_not_supported_24));
                }
            }
        });
        btnChonAnhDM.setOnClickListener(new View.OnClickListener() {
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
                anhDanhMuc.setImageBitmap(bitmap);

                // Chuyển đổi bitmap thành byte[]
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                anhDM = stream.toByteArray(); // Lưu ảnh vào biến anhDanhMuc

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}