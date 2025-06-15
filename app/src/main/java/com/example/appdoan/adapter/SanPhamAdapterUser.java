package com.example.appdoan.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appdoan.R;
import com.example.appdoan.fragment.user.HomeFragment;
import com.example.appdoan.object.SanPham;
import com.example.appdoan.tool.Database;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SanPhamAdapterUser extends ArrayAdapter<SanPham> {
    Activity context;
    int IDLayout;
    ArrayList<SanPham> listSanPham;

    public SanPhamAdapterUser (Activity context, int IDLayout, ArrayList<SanPham> listSanPham) {
        super(context, IDLayout, listSanPham);
        this.context = context;
        this.IDLayout = IDLayout;
        this.listSanPham = listSanPham;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Lấy mã tài khoản từ SharedPreferences
        SharedPreferences sharedPref = context.getSharedPreferences("mataikhoan", Context.MODE_PRIVATE);
        int mataikhoan = sharedPref.getInt("mataikhoan", -1); // -1 là giá trị mặc định nếu không tìm thấy "mataikhoan"

        Database database = new Database(context);
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        LayoutInflater layoutInflater = context.getLayoutInflater();

        convertView = layoutInflater.inflate(IDLayout,null);

        SanPham sanPham = listSanPham.get(position);

        ImageView imgAnhSanPham = convertView.findViewById(R.id.imgAnhSanPham);

        byte[] anhSanPhamByteArray = sanPham.getAnhSanPham();
        Bitmap bitmap = BitmapFactory.decodeByteArray(anhSanPhamByteArray, 0, anhSanPhamByteArray.length);
        imgAnhSanPham.setImageBitmap(bitmap);

        TextView tvTenSanPham = convertView.findViewById(R.id.tvTenSanPham);
        TextView tvGiaSanPham = convertView.findViewById(R.id.tvGiaSanPham);

        tvTenSanPham.setText(sanPham.getTenSanPham());
        tvGiaSanPham.setText(formatCurrency(sanPham.getGia()));

        ImageButton imgThemVaoGioHang = convertView.findViewById(R.id.btnThemVaoGio);
        
        imgThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra xem sản phẩm này đã có trong giỏ hàng chưa
                String[] selectionArg = {String.valueOf(mataikhoan), sanPham.getMaSanPham()};
                Cursor cursor = db.rawQuery("SELECT * FROM giohang WHERE mataikhoan=? AND masanpham=?", selectionArg);

                if (cursor.getCount() > 0) {
                    Toast.makeText(context, "Sản phẩm đã có trong giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor checkSoLuong = db.rawQuery("SELECT soluong FROM sanpham WHERE masanpham =?", new String[]{sanPham.getMaSanPham()});

                    if (checkSoLuong.moveToFirst()) { // Di chuyển con trỏ đến dòng đầu tiên
                        int soluongtrongkho = checkSoLuong.getInt(0);

                        if (soluongtrongkho >= 1) {
                            int gia = 1 * sanPham.getGia();

                            ContentValues cv = new ContentValues();
                            cv.put("masanpham", sanPham.getMaSanPham());
                            cv.put("soluong", 1);
                            cv.put("mataikhoan", mataikhoan);
                            cv.put("gia", gia);

                            long result = db.insert("giohang", null, cv);
                            if (result == -1) {
                                Toast.makeText(context, "Thêm Vào Giỏ Hàng Thất Bại", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Số lượng bạn đặt vượt quá số lượng Shop đang có!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    checkSoLuong.close(); // Đóng Cursor sau khi dùng
                }
                cursor.close(); // Đóng Cursor đầu tiên sau khi dùng
            }
        });

        return convertView;
    }

    public static String formatCurrency(int amount) {
        // Tạo đối tượng NumberFormat cho tiền tệ
        NumberFormat formatter = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));

        // Định dạng số
        String formattedAmount = formatter.format(amount);

        // Thêm ký hiệu "đ" vào cuối
        return formattedAmount + " đ";
    }

}
