package com.example.appdoan.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appdoan.R;
import com.example.appdoan.object.SanPham;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SanPhamAdapterAdmin extends ArrayAdapter<SanPham> {
    Activity context;
    int IDLayout;
    ArrayList<SanPham> listSanPham;

    public SanPhamAdapterAdmin(Activity context, int IDLayout, ArrayList<SanPham> listSanPham) {
        super(context, IDLayout,listSanPham);
        this.context = context;
        this.IDLayout = IDLayout;
        this.listSanPham = listSanPham;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Tạo đế chứa layout
        LayoutInflater layoutInflater = context.getLayoutInflater();

        // Đặt IDlayout lên đế để tạo thành view
        convertView = layoutInflater.inflate(IDLayout,null);

        // Lấy 1 phần tử trong mảng
        SanPham sanPham = listSanPham.get(position);

        //Khai báo, tham chiếu ID và hiển thị ảnh
        ImageView imgAnhSanPham = convertView.findViewById(R.id.imgAnhSanPham);

        byte[] anhSanPhamByteArray = sanPham.getAnhSanPham();
        Bitmap bitmap = BitmapFactory.decodeByteArray(anhSanPhamByteArray, 0, anhSanPhamByteArray.length);
        imgAnhSanPham.setImageBitmap(bitmap);

        TextView tvMaSanPham = convertView.findViewById(R.id.tvProductCode);
        TextView tvTenSanPham = convertView.findViewById(R.id.tvProductName);
        TextView tvSoLuong = convertView.findViewById(R.id.tvQuantity);
        TextView tvMoTa = convertView.findViewById(R.id.tvDescription);
//        TextView tvMaGiamGia = convertView.findViewById(R.id.tvDiscountCode);
        TextView tvMaDanhMuc = convertView.findViewById(R.id.tvCategory);
        TextView tvGia = convertView.findViewById(R.id.tvPrice);

        tvMaSanPham.setText("Mã Sản Phẩm: "+sanPham.getMaSanPham());
        tvTenSanPham.setText("Tên Sản Phẩm: "+sanPham.getTenSanPham());
        tvSoLuong.setText("Số Lượng: "+(sanPham.getSoLuong()));
        tvMoTa.setText("Mô Tả: "+sanPham.getMoTa());
        tvMaDanhMuc.setText("Mã Danh Mục: "+sanPham.getMaDanhMuc());
        tvGia.setText("Giá: "+formatCurrency(sanPham.getGia()));

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
