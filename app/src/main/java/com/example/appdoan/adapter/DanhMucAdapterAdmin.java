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
import com.example.appdoan.object.DanhMuc;

import java.util.ArrayList;

public class DanhMucAdapterAdmin extends ArrayAdapter<DanhMuc> {
    Activity context;
    int IDlayout;
    ArrayList<DanhMuc> listDanhMuc;

    public DanhMucAdapterAdmin(Activity context, int IDlayout, ArrayList<DanhMuc> listDanhMuc) {
        super(context, IDlayout, listDanhMuc);
        this.context = context;
        this.IDlayout = IDlayout;
        this.listDanhMuc = listDanhMuc;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();

        convertView = layoutInflater.inflate(IDlayout,null);

        DanhMuc danhMuc = listDanhMuc.get(position);

        ImageView imgAnhDanhMuc = convertView.findViewById(R.id.imgAnhDanhMuc);

        byte[] anhDanhMucByteArray = danhMuc.getAnhDanhMuc();
        Bitmap bitmap = BitmapFactory.decodeByteArray(anhDanhMucByteArray, 0, anhDanhMucByteArray.length);
        imgAnhDanhMuc.setImageBitmap(bitmap);

        TextView tvMaDanhMuc = convertView.findViewById(R.id.tvMaDanhMuc);
        TextView tvTenDanhMuc = convertView.findViewById(R.id.tvTenDanhMuc);

        tvMaDanhMuc.setText(danhMuc.getMaDanhMuc());
        tvTenDanhMuc.setText(danhMuc.getTenDanhMuc());

        return convertView;
    }
}
