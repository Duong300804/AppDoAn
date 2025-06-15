package com.example.appdoan.adapter;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appdoan.R;
import com.example.appdoan.object.DanhGia;
import com.example.appdoan.tool.Database;

import java.util.ArrayList;

public class DanhGiaAdapterAdmin extends ArrayAdapter<DanhGia> {
    Activity context;
    int IDlayout;
    ArrayList<DanhGia> listDanhGia;

    public DanhGiaAdapterAdmin(Activity context, int IDlayout, ArrayList<DanhGia> listDanhGia) {
        super(context,IDlayout,listDanhGia);
        this.context = context;
        this.IDlayout = IDlayout;
        this.listDanhGia = listDanhGia;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Database database = new Database(context);
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        LayoutInflater layoutInflater = context.getLayoutInflater();

        convertView = layoutInflater.inflate(IDlayout,null);

        DanhGia danhGia = listDanhGia.get(position);
        TextView tvMaDanhGia = convertView.findViewById(R.id.tvMaDanhGia);
        TextView tvMaDonHang = convertView.findViewById(R.id.tvMaDonHang);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);
        TextView tvDanhGia = convertView.findViewById(R.id.tvDanhGia);
        TextView tvTenTaiKhoan = convertView.findViewById(R.id.tvTenTaiKhoan);

        tvMaDanhGia.setText("Mã Đánh Giá: "+danhGia.getMaDanhGia());
        tvMaDonHang.setText("Mã Đơn Hàng: "+danhGia.getMaDonHang());
        tvDanhGia.setText("Đánh Giá: "+danhGia.getDanhGia());
        ratingBar.setRating((float)danhGia.getSoSao());

        String tenTaiKhoan = "";
        String hoVaTen =" ";
        String query;
        query = "SELECT dh.mataikhoan \n" +
                "FROM danhgia dg \n" +
                "JOIN donhang dh WHERE dg.madonhang = dh.madonhang AND dh.madonhang=?";
        Cursor cursor;
        cursor = db.rawQuery(query,new String[] {String.valueOf(danhGia.getMaDonHang())});
        if (cursor.moveToFirst()){
            int maTaiKhoan = cursor.getInt(0);

            query = "SELECT * FROM taikhoan WHERE mataikhoan=?";
            cursor = db.rawQuery(query,new String[] {String.valueOf(maTaiKhoan)});
            if(cursor.moveToFirst()){
                tenTaiKhoan = cursor.getString(1);
            }

            query = "SELECT * FROM thongtintaikhoan WHERE mataikhoan=?";
            cursor = db.rawQuery(query,new String[] {String.valueOf(maTaiKhoan)});
            if(cursor.moveToFirst()){
                hoVaTen = cursor.getString(1);
            }
        }
        tvTenTaiKhoan.setText("Tên Tài Khoản: "+tenTaiKhoan +" - "+hoVaTen);

        return convertView;
    }
}
