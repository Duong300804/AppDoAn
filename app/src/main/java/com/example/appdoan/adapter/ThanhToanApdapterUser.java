package com.example.appdoan.adapter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appdoan.R;
import com.example.appdoan.object.GioHang;
import com.example.appdoan.tool.Database;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ThanhToanApdapterUser extends ArrayAdapter<GioHang> {
    Activity context;
    int Idlayout;
    ArrayList<GioHang> listGioHang;
    int soLuong = 0;

    public ThanhToanApdapterUser(Activity context, int idlayout, ArrayList<GioHang> listGioHang) {
        super(context, idlayout,listGioHang);
        this.context = context;
        this.Idlayout = idlayout;
        this.listGioHang = listGioHang;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();

        convertView = layoutInflater.inflate(Idlayout, null);

        GioHang gioHang = listGioHang.get(position);

        ImageView productimage = convertView.findViewById(R.id.productImage);

        byte[] anhSanPham = gioHang.getAnhSanPham();
        Bitmap bitmap = BitmapFactory.decodeByteArray(anhSanPham, 0, anhSanPham.length);
        productimage.setImageBitmap(bitmap);

        TextView productName = convertView.findViewById(R.id.productName);
        TextView productPrice = convertView.findViewById(R.id.productPrice);
        TextView quantity = convertView.findViewById(R.id.quantityText);

        productName.setText(gioHang.getTenSanPham());
        productPrice.setText("Tổng tiền: "+formatCurrency(gioHang.getGia()));
        quantity.setText("Số Lượng: "+gioHang.getSoLuong());

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
