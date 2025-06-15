package com.example.appdoan.fragment.admin;

import android.app.AlertDialog;
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

public class SuaDanhMuc extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private byte[] anhDM;
    Button btnHuySua,btnXoa,btnLuu,btnChonAnhDM;
    EditText edtsuaMaDM,edtsuaTenDM;
    String maDanhMuc;
    ImageView imgDanhMuc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sua_danh_muc,container,false);
        btnChonAnhDM = view.findViewById(R.id.btnChonAnhDM);
        btnXoa = view.findViewById(R.id.btnXoa);
        btnHuySua = view.findViewById(R.id.btnHuysua);
        btnLuu = view.findViewById(R.id.btnLuusua);
        edtsuaMaDM = view.findViewById(R.id.edtSuamaDM);
        edtsuaTenDM = view.findViewById(R.id.edtSuatenDM);
        imgDanhMuc = view.findViewById(R.id.imgDanhMuc);

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        // Lấy mã danh mục từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            maDanhMuc = bundle.getString("maDanhMuc");
            edtsuaMaDM.setText(maDanhMuc);

            String[] selectionArg = {maDanhMuc};

            Cursor cursor = db.rawQuery("SELECT * FROM danhmuc WHERE madanhmuc = ?", selectionArg);
            if (cursor.moveToFirst()) {
                // Không cần sử dụng do-while, chỉ cần sử dụng if vì chỉ có một bản ghi
                // Chuyển giá trị từ cursor vào các EditText và ImageView
                edtsuaMaDM.setText(cursor.getString(0)); // Mã danh mục
                edtsuaTenDM.setText(cursor.getString(1));
                anhDM = cursor.getBlob(2);

                if (anhDM != null) {
                    // Chuyển đổi mảng byte thành Bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(anhDM, 0, anhDM.length);
                    // Đặt Bitmap vào ImageView
                    imgDanhMuc.setImageBitmap(bitmap);
                }

            }
            cursor.close();
        }

        btnHuySua.setOnClickListener(new View.OnClickListener() {
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

        btnChonAnhDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở thư viện ảnh
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        BatLoiDanhMuc batloi = new BatLoiDanhMuc();

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenDanhMuc = edtsuaTenDM.getText().toString().trim();

                if(!batloi.kiemtraTenDanhMuc(tenDanhMuc)){
                    Toast.makeText(requireActivity(), "Tên danh mục không được để trống hoặc sai định dạng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!batloi.kiemtraanhDanhMuc(anhDM)){
                    Toast.makeText(requireActivity(), "Ảnh danh mục không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!batloi.kiemTraTenDanhMucTonTai(tenDanhMuc, db)){
                    Toast.makeText(requireActivity(), "Tên danh mục đã tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues cv = new ContentValues();

                cv.put("tendanhmuc",tenDanhMuc);
                cv.put("anhdanhmuc",anhDM);

                long result = db.update("danhmuc",cv,"madanhmuc=?",new String[] {maDanhMuc});
                if (result == -1){
                    Toast.makeText(requireActivity(), "Sửa dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireActivity(), "Sửa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    // Khởi tạo Fragment
                    QLDMFragment qldmFragment = new QLDMFragment();

                    // Lấy FragmentManager và bắt đầu giao dịch
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                    // Thay thế Fragment hiện tại bằng Fragment mới
                    transaction.replace(R.id.FL, qldmFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
                }
            }
        });

        String finalMaDanhMuc = maDanhMuc;
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tạo thông báo
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Cảnh Báo");
                builder.setMessage("Bạn có muốn xoá danh mục không?");
                builder.setCancelable(false); //Không cho phép thoát bằng cách nhấn ngoài

                //Trường Hợp người dùng bấm "Xoá danh mục"
                builder.setPositiveButton("Xoá Danh Mục", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long result = db.delete("danhmuc","madanhmuc=?",new String[]{finalMaDanhMuc});
                        if(result == -1){
                            Toast.makeText(requireActivity(), "Xoá danh mục thất bại ", Toast.LENGTH_SHORT).show();
                        }else{
                            //Tạo thông báo
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                            builder.setTitle("Thông báo");
                            builder.setMessage("Xoá danh mục thành công");
                            builder.setCancelable(false); //Không cho phép thoát bằng cách nhấn ngoài

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Khởi tạo Fragment
                                    QLDMFragment qldmFragment = new QLDMFragment();

                                    // Lấy FragmentManager và bắt đầu giao dịch
                                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                                    // Thay thế Fragment hiện tại bằng Fragment mới
                                    transaction.replace(R.id.FL, qldmFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                });
                //Trường Hợp người dùng bấm "Không xoá"
                builder.setNegativeButton("Không xoá danh mục", new DialogInterface.OnClickListener() {
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
                imgDanhMuc.setImageBitmap(bitmap);

                // Chuyển đổi bitmap thành byte[]
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                anhDM = stream.toByteArray(); // Lưu ảnh vào biến anhSP

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}