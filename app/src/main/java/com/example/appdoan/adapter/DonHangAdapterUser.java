package com.example.appdoan.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.appdoan.R;
import com.example.appdoan.object.DonHang;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DonHangAdapterUser extends ArrayAdapter<DonHang> {
    Activity context;
    int IDLayout;
    ArrayList<DonHang> listDonHang;

    public DonHangAdapterUser(Activity context, int IDLayout, ArrayList<DonHang> listDonHang) {
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
        TextView tv_order_code = convertView.findViewById(R.id.tv_order_code);
        TextView tv_date = convertView.findViewById(R.id.tv_date);
        TextView tv_listProduct = convertView.findViewById(R.id.tv_listProduct);
        TextView tv_status = convertView.findViewById(R.id.tv_status);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);

        if(donHang.getTrangThai().equals("Đang Giao")){
            tv_status.setTextColor(Color.RED);
        }else if(donHang.getTrangThai().equals("Đã Giao")){
            tv_status.setTextColor(ContextCompat.getColor(context, R.color.orange));
        }else if(donHang.getTrangThai().equals("Hoàn Thành")){
            tv_status.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        tv_order_code.setText("Mã Đơn Hàng: DH"+donHang.getMaDonHang());
        tv_date.setText("Ngày Tạo Đơn: "+donHang.getNgayMua());
        tv_listProduct.setText("Danh Sách Sản Phẩm: "+donHang.getDanhSachSanPham());
        tvPrice.setText("Tổng Tiền: " + formatCurrency(donHang.getTongTien()));
        tv_status.setText("Trạng Thái: "+donHang.getTrangThai());

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
