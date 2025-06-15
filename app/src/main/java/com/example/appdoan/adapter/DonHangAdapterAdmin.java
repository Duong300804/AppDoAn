package com.example.appdoan.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appdoan.R;
import com.example.appdoan.object.DonHang;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DonHangAdapterAdmin extends ArrayAdapter<DonHang> {
    Activity context;
    int IDLayout;
    ArrayList<DonHang> listDonHang;

    public DonHangAdapterAdmin(Activity context, int IDLayout, ArrayList<DonHang> listDonHang) {
        super(context, IDLayout,listDonHang);
        this.context = context;
        this.IDLayout = IDLayout;
        this.listDonHang = listDonHang;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Tạo đế chứa layout
        LayoutInflater layoutInflater = context.getLayoutInflater();

        // Đặt IDlayout lên đế để tạo thành view
        convertView = layoutInflater.inflate(IDLayout,null);

        // Lấy 1 phần tử trong mảng
        DonHang donHang = listDonHang.get(position);
        TextView tvMaDonHang = convertView.findViewById(R.id.tvMaDonHang);
        TextView tvMaTaiKhoan = convertView.findViewById(R.id.tvMaTaiKhoan);
        TextView tvDanhSachSanPham = convertView.findViewById(R.id.tvDanhSachSanPham);
        TextView tvTongTien = convertView.findViewById(R.id.tvTongTien);
        TextView tvNgayMua = convertView.findViewById(R.id.tvNgayMua);
        TextView tvTrangThai = convertView.findViewById(R.id.tvTrangThai);

        tvMaDonHang.setText("Mã Đơn Hàng: "+donHang.getMaDonHang());
        tvMaTaiKhoan.setText("Mã Tài Khoản: "+donHang.getMaTaiKhoan());
        tvDanhSachSanPham.setText("Danh Sách Sản Phẩm: "+donHang.getDanhSachSanPham());
        tvTongTien.setText("Tổng Tiền: "+formatCurrency(donHang.getTongTien()));
        tvNgayMua.setText("Ngày Mua: "+donHang.getNgayMua());
        tvTrangThai.setText("Trạng Thái: "+donHang.getTrangThai());

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
