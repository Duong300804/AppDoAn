package com.example.appdoan.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdoan.R;
import com.example.appdoan.fragment.user.ProductByCategoryFragment;
import com.example.appdoan.object.DanhMuc;

import java.util.ArrayList;

public class DanhMucAdapterUser extends RecyclerView.Adapter<DanhMucAdapterUser.MyViewHolder> {
    int IDLayout;
    Activity context;
    ArrayList<DanhMuc> listDanhMuc;

    public DanhMucAdapterUser(Activity context,int IDLayout, ArrayList<DanhMuc> listDanhMuc) {
        this.context = context;
        this.IDLayout = IDLayout;
        this.listDanhMuc = listDanhMuc;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view =layoutInflater.inflate(IDLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DanhMuc danhMuc = listDanhMuc.get(position);
        holder.tvTenDanhMuc.setText(danhMuc.getTenDanhMuc());

        byte[] anhDanhMucByteArray = danhMuc.getAnhDanhMuc();
        Bitmap bitmap = BitmapFactory.decodeByteArray(anhDanhMucByteArray, 0, anhDanhMucByteArray.length);
        holder.anhDanhMuc.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo ProductByCategoryFragment
                ProductByCategoryFragment productByCategoryFragment = new ProductByCategoryFragment();

                // Tạo Bundle và truyền mã danh mục
                Bundle bundle = new Bundle();
                bundle.putString("maDanhMuc", danhMuc.getMaDanhMuc());
                productByCategoryFragment.setArguments(bundle);

                // Lấy FragmentManager và bắt đầu giao dịch
                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, productByCategoryFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDanhMuc.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTenDanhMuc;
        ImageView anhDanhMuc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDanhMuc=itemView.findViewById(R.id.tvTenDanhMuc);
            anhDanhMuc=itemView.findViewById(R.id.anhDanhMuc);
        }
    }
}
