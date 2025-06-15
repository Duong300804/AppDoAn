package com.example.appdoan.fragment.user;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.tool.Database;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailFragment extends Fragment {

    private byte[] anhSP; // Biến lưu trữ ảnh dưới dạng byte[]

    ImageButton btnGiam, btnTang;
    ImageView imgAnhSanPham;
    TextView tvTenSanPham, tvGiaSanPham, tvmoTa, tvSoLuong;
    int soLuong = 1;
    int giaSanPham = 0;
    String maSanPham;
    Button btnThemVaoGio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        // Ánh xạ các view từ layout
        btnGiam = view.findViewById(R.id.btnGiam);
        btnTang = view.findViewById(R.id.btnTang);
        btnThemVaoGio = view.findViewById(R.id.btnThemVaoGio);
        tvTenSanPham = view.findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = view.findViewById(R.id.tvGiaSanPham);
        tvmoTa = view.findViewById(R.id.tvmoTa);
        tvSoLuong = view.findViewById(R.id.tvSoLuong);
        imgAnhSanPham = view.findViewById(R.id.imgAnhSanPham);

        // Khởi tạo database
        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        // Lấy mã sản phẩm từ Bundle
        Bundle bundle = getArguments();
        maSanPham = bundle.getString("maSanPham");

        // Lấy mã tài khoản từ SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("mataikhoan", Context.MODE_PRIVATE);
        int mataikhoan = sharedPref.getInt("mataikhoan", -1); // -1 là giá trị mặc định nếu không tìm thấy "mataikhoan"

        if (bundle != null) {
            // Truy vấn sản phẩm theo mã sản phẩm
            String[] selectionArgs = {maSanPham};
            Cursor cursor = db.rawQuery("SELECT * FROM sanpham WHERE maSanPham = ?", selectionArgs);

            if (cursor.moveToFirst()) {
                // Lấy thông tin từ cursor
                String tenSanPham = cursor.getString(2);
                anhSP = cursor.getBlob(3); // Lưu trữ ảnh dưới dạng byte[]
                giaSanPham = cursor.getInt(5);
                String moTaSanPham = cursor.getString(6);

                // Hiển thị dữ liệu lên các TextView và ImageView
                tvTenSanPham.setText(tenSanPham);
                tvGiaSanPham.setText(formatCurrency(giaSanPham));
                tvmoTa.setText(moTaSanPham);
                tvSoLuong.setText(String.valueOf(soLuong));

                // Hiển thị ảnh sản phẩm (chuyển đổi từ byte[] thành Bitmap)
                if (anhSP != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(anhSP, 0, anhSP.length);
                    imgAnhSanPham.setImageBitmap(bitmap);
                }
            } else {
                Toast.makeText(getActivity(), "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }

        // Xử lý sự kiện nút tăng số lượng
        btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soLuong++;
                tvSoLuong.setText(String.valueOf(soLuong));
            }
        });

        btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (soLuong > 1) {
                    soLuong--;
                    tvSoLuong.setText(String.valueOf(soLuong));
                }
            }
        });

        btnThemVaoGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra xem sản phẩm này đã có trong giỏ hàng chưa
                String[] selectionArg = {String.valueOf(mataikhoan), maSanPham};
                Cursor cursor = db.rawQuery("SELECT * FROM giohang WHERE mataikhoan=? AND masanpham=?", selectionArg);

                if (cursor.getCount() > 0) {
                    Toast.makeText(requireActivity(), "Sản phẩm đã có trong giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor checkSoLuong = db.rawQuery("SELECT soluong FROM sanpham WHERE masanpham =?", new String[]{maSanPham});

                    if (checkSoLuong.moveToFirst()) { // Di chuyển con trỏ đến dòng đầu tiên
                        int soluongtrongkho = checkSoLuong.getInt(0);

                        if (soluongtrongkho >= soLuong) {
                            int gia = soLuong * giaSanPham;

                            ContentValues cv = new ContentValues();
                            cv.put("masanpham", maSanPham);
                            cv.put("soluong", soLuong);
                            cv.put("mataikhoan", mataikhoan);
                            cv.put("gia", gia);

                            long result = db.insert("giohang", null, cv);
                            if (result == -1) {
                                Toast.makeText(requireActivity(), "Thêm Vào Giỏ Hàng Thất Bại", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireActivity(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                                loadFragment(new HomeFragment()); // Quay lại trang Sản Phẩm
                            }
                        } else {
                            Toast.makeText(requireActivity(), "Số lượng bạn đặt vượt quá số lượng Shop đang có!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    checkSoLuong.close(); // Đóng Cursor sau khi dùng
                }
                cursor.close(); // Đóng Cursor đầu tiên sau khi dùng
            }
        });
        return view;
    }
    public static String formatCurrency(int amount) {
        // Tạo đối tượng NumberFormat cho tiền tệ
        NumberFormat formatter = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));

        // Định dạng số
        String formattedAmount = formatter.format(amount);

        // Thêm ký hiệu "đ" vào cuối
        return formattedAmount + " đ";
    }

    private void loadFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}