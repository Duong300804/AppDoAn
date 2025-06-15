package com.example.appdoan.fragment.admin;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.example.appdoan.tool.Database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuaSanPham extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private byte[] anhSP; // Biến lưu trữ ảnh dưới dạng byte[]

    Button btnHuySua,btnXoa,btnLuu,btnChonAnh;
    EditText edtsuaMaSP,edtsuaTenSP,edtsuaGiaSP,edtsuaSoLuong,edtsuaMaDanhMuc,edtsuaMoTa;
    Spinner spinnerMaDM;
    ImageView imgSanPham;
    String tenDanhMuc,maSanPham;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sua_san_pham,container,false);

        btnXoa = view.findViewById(R.id.btnXoa);
        btnHuySua = view.findViewById(R.id.btnHuysua);
        btnLuu = view.findViewById(R.id.btnLuusua);
        btnChonAnh = view.findViewById(R.id.btnChonAnh);
        edtsuaMaSP = view.findViewById(R.id.edtSuamaSP);
        edtsuaTenSP = view.findViewById(R.id.edtSuatenSP);
        edtsuaGiaSP = view.findViewById(R.id.edtSuaGia);
        edtsuaSoLuong = view.findViewById(R.id.edtSuasoluong);
        spinnerMaDM = view.findViewById(R.id.spinnerMaDM);
        edtsuaMoTa = view.findViewById(R.id.edtSuaMota);
        imgSanPham = view.findViewById(R.id.imgSanPham);

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        // Lấy mã sản phẩm từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            maSanPham = bundle.getString("maSanPham");
            edtsuaMaSP.setText(maSanPham);

            String[] selectionArg = {maSanPham};

            Cursor cursor = db.rawQuery("SELECT * FROM sanpham WHERE masanpham = ?", selectionArg);
            if (cursor.moveToFirst()) {
                // Không cần sử dụng do-while, chỉ cần sử dụng if vì chỉ có một bản ghi
                // Chuyển giá trị từ cursor vào các EditText và ImageView
                edtsuaMaSP.setText(cursor.getString(0)); // Mã sản phẩm

                String[] selectionArg1 = { cursor.getString(1) };
                Cursor cursor1 = db.rawQuery("SELECT tendanhmuc FROM danhmuc WHERE madanhmuc = ?",selectionArg1);
                if(cursor1.moveToFirst()){
                    tenDanhMuc = cursor1.getString(0);
                }

                edtsuaTenSP.setText(cursor.getString(2)); // Tên sản phẩm

                // Lấy Blob từ cursor
                anhSP = cursor.getBlob(3);
                if (anhSP != null) {
                    // Chuyển đổi mảng byte thành Bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(anhSP, 0, anhSP.length);
                    // Đặt Bitmap vào ImageView
                    imgSanPham.setImageBitmap(bitmap);
                }

                // Chuyển đổi kiểu dữ liệu cho số lượng và giá
                edtsuaSoLuong.setText(String.valueOf(cursor.getInt(4))); // Số lượng
                edtsuaGiaSP.setText(String.valueOf(cursor.getInt(5))); // Giá
                edtsuaMoTa.setText(cursor.getString(6)); // Mô tả
            }
            cursor.close();
        }

        loadDanhMuc();

        btnHuySua.setOnClickListener(new View.OnClickListener() {
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

        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở thư viện ảnh
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenSanPham = edtsuaTenSP.getText().toString().trim();
                int gia = Integer.parseInt(edtsuaGiaSP.getText().toString().trim());
                int soLuong = Integer.parseInt(edtsuaSoLuong.getText().toString().trim());
                String moTa = edtsuaMoTa.getText().toString().trim();
                String tenDanhMuc = spinnerMaDM.getSelectedItem().toString();
                String maDanhMuc = "";

                Cursor cursor = db.rawQuery("SELECT madanhmuc FROM danhmuc WHERE tendanhmuc = ?",new String[] {tenDanhMuc});
                if (cursor.moveToFirst()) {
                    do {
                        maDanhMuc = cursor.getString(0); // Lấy tên danh mục
                    } while (cursor.moveToNext());
                }

                ContentValues cv = new ContentValues();
                cv.put("madanhmuc",maDanhMuc);
                cv.put("tensanpham",tenSanPham);
                cv.put("anhsanpham",anhSP);
                cv.put("soluong",soLuong);
                cv.put("gia",gia);
                cv.put("mota",moTa);

                long result = db.update("sanpham",cv,"masanpham=?",new String[] {maSanPham});
                if (result == -1){
                    Toast.makeText(requireActivity(), "Sửa dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireActivity(), "Sửa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    // Khởi tạo Fragment
                    QLSPFragment qlspFragment = new QLSPFragment();

                    // Lấy FragmentManager và bắt đầu giao dịch
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                    // Thay thế Fragment hiện tại bằng Fragment mới
                    transaction.replace(R.id.FL, qlspFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
                }
            }
        });

        String finalMaSanPham = maSanPham;
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tạo thông báo
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Cảnh Báo");
                builder.setMessage("Bạn có muốn xoá sản phẩm không?");
                builder.setCancelable(false); //Không cho phép thoát bằng cách nhấn ngoài

                //Trường Hợp người dùng bấm "Xoá sản phẩm"
                builder.setPositiveButton("Xoá Sản Phẩm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long result = db.delete("sanpham","masanpham=?",new String[]{finalMaSanPham});
                        if(result == -1){
                            Toast.makeText(requireActivity(), "Xoá sản phẩm thất bại ", Toast.LENGTH_SHORT).show();
                        }else{
                            //Tạo thông báo
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                            builder.setTitle("Thông báo");
                            builder.setMessage("Xoá sản phẩm thành công");
                            builder.setCancelable(false); //Không cho phép thoát bằng cách nhấn ngoài

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Khởi tạo Fragment
                                    QLSPFragment qlspFragment = new QLSPFragment();

                                    // Lấy FragmentManager và bắt đầu giao dịch
                                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                                    // Thay thế Fragment hiện tại bằng Fragment mới
                                    transaction.replace(R.id.FL, qlspFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                });
                //Trường Hợp người dùng bấm "Không xoá"
                builder.setNegativeButton("Không xoá sản phẩm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                //Khởi tạo và hiển thị hộp thoại Dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
                imgSanPham.setImageBitmap(bitmap);

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

        int position = adapter.getPosition(tenDanhMuc);
        if (position >= 0) { // Kiểm tra nếu position hợp lệ
            spinnerMaDM.setSelection(position);
        }
    }
}