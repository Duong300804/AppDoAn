package com.example.appdoan.fragment.admin;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.tool.Database;

public class SuaTaiKhoan extends Fragment {
    Button btnHuySua,btnXoa,btnLuu;
    EditText edtsuaMaTK,edtsuaTenTK,edtsuaMatKhau,edtsuaEmail,edtsuaLoaiTK;
    String maTaiKhoan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sua_tai_khoan,container,false);

        btnXoa = view.findViewById(R.id.btnXoa);
        btnHuySua = view.findViewById(R.id.btnHuysua);
        btnLuu = view.findViewById(R.id.btnLuusua);
        edtsuaMaTK = view.findViewById(R.id.edtsuaMaTK);
        edtsuaTenTK = view.findViewById(R.id.edtsuaTenTK);
        edtsuaMatKhau = view.findViewById(R.id.edtsuaMatKhauTK);
        edtsuaEmail = view.findViewById(R.id.edtsuaEmail);
        edtsuaLoaiTK = view.findViewById(R.id.edtsuaLoaiTK);

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        // Lấy mã tài khoản từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            maTaiKhoan = bundle.getString("maTaiKhoan");
            edtsuaMaTK.setText(maTaiKhoan);

            String[] selectionArg = {maTaiKhoan};

            Cursor cursor = db.rawQuery("SELECT * FROM taikhoan WHERE mataikhoan = ?", selectionArg);
            if (cursor.moveToFirst()) {
                // Không cần sử dụng do-while, chỉ cần sử dụng if vì chỉ có một bản ghi
                // Chuyển giá trị từ cursor vào các EditText
                edtsuaMaTK.setText(cursor.getString(0));
                edtsuaTenTK.setText(cursor.getString(1));
                edtsuaMatKhau.setText(cursor.getString(2));
                edtsuaEmail.setText(cursor.getString(3));
                edtsuaLoaiTK.setText(cursor.getString(4));

            }
            cursor.close();
        }


        btnHuySua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo Fragment
                QLTKFragment qltkFragment = new QLTKFragment();

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Thay thế Fragment hiện tại bằng Fragment mới
                transaction.replace(R.id.FL, qltkFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
            }
        });


        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenTaiKhoan = edtsuaTenTK.getText().toString().trim();
                String matKhau = edtsuaMatKhau.getText().toString().trim();
                String email = edtsuaEmail.getText().toString().trim();
                String loaiTaiKhoan = edtsuaLoaiTK.toString().trim();

                ContentValues cv = new ContentValues();
                cv.put("mataikhoan",maTaiKhoan);
                cv.put("tentaikhoan",tenTaiKhoan);
                cv.put("matkhau",matKhau);
                cv.put("email",email);
                cv.put("loaitaikhoan",loaiTaiKhoan);

                long result = db.update("taikhoan",cv,"mataikhoan=?",new String[] {maTaiKhoan});
                if (result == -1){
                    Toast.makeText(requireActivity(), "Sửa dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireActivity(), "Sửa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    // Khởi tạo Fragment
                    QLTKFragment qltkFragment = new QLTKFragment();

                    // Lấy FragmentManager và bắt đầu giao dịch
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                    // Thay thế Fragment hiện tại bằng Fragment mới
                    transaction.replace(R.id.FL, qltkFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
                }
            }
        });

        String finalMaTaiKhoan = maTaiKhoan;
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tạo thông báo
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Cảnh Báo");
                builder.setMessage("Bạn có muốn xoá tài khoảm không?");
                builder.setCancelable(false); //Không cho phép thoát bằng cách nhấn ngoài

                //Trường Hợp người dùng bấm "Xoá tài khoản "
                builder.setPositiveButton("Xoá Tài Khoản", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long result = db.delete("taikhoan","mataikhoan=?",new String[]{finalMaTaiKhoan});
                        if(result == -1){
                            Toast.makeText(requireActivity(), "Xoá tài khoản thất bại ", Toast.LENGTH_SHORT).show();
                        }else{
                            //Tạo thông báo
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                            builder.setTitle("Thông báo");
                            builder.setMessage("Xoá tài khoản thành công");
                            builder.setCancelable(false); //Không cho phép thoát bằng cách nhấn ngoài

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Khởi tạo Fragment
                                    QLTKFragment qltkFragment = new QLTKFragment();

                                    // Lấy FragmentManager và bắt đầu giao dịch
                                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                                    // Thay thế Fragment hiện tại bằng Fragment mới
                                    transaction.replace(R.id.FL, qltkFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                });
                //Trường Hợp người dùng bấm "Không xoá"
                builder.setNegativeButton("Không xoá tài khoản", new DialogInterface.OnClickListener() {
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
}