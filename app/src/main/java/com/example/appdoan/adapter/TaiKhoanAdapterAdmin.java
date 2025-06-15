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
import com.example.appdoan.object.TaiKhoan;

import java.util.ArrayList;

public class TaiKhoanAdapterAdmin extends ArrayAdapter<TaiKhoan> {
    Activity context;
    int IDLayout;
    ArrayList<TaiKhoan> listTaiKhoan;

    public TaiKhoanAdapterAdmin(Activity context, int IDLayout, ArrayList<TaiKhoan> listTaiKhoan) {
        super(context, IDLayout,listTaiKhoan);
        this.context = context;
        this.IDLayout = IDLayout;
        this.listTaiKhoan = listTaiKhoan;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Tạo đế chứa layout
        LayoutInflater layoutInflater = context.getLayoutInflater();

        // Đặt IDlayout lên đế để tạo thành view
        convertView = layoutInflater.inflate(IDLayout,null);

        // Lấy 1 phần tử trong mảng
        TaiKhoan taiKhoan = listTaiKhoan.get(position);

        TextView tvMaTaiKhoan = convertView.findViewById(R.id.tvMaTaiKhoan);
        TextView tvTenTaiKhoan = convertView.findViewById(R.id.tvTenTaiKhoan);
        TextView tvMatKhauTK = convertView.findViewById(R.id.tvMatKhauTK);
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        TextView tvLoaiTaiKHoan = convertView.findViewById(R.id.tvLoaiTaiKhoan);

        tvMaTaiKhoan.setText("Mã Tài Khoản: "+taiKhoan.getMaTaiKhoan());
        tvTenTaiKhoan.setText("Tên Tài Khoản: "+taiKhoan.getTenTaiKhoan());
        tvMatKhauTK.setText("Mật Khẩu: "+taiKhoan.getMatKhau());
        tvEmail.setText("Email: "+taiKhoan.getEmail());
        tvLoaiTaiKHoan.setText("Loại Tài Khoản: "+taiKhoan.getLoaiTaiKhoan());

        return convertView;
    }


}