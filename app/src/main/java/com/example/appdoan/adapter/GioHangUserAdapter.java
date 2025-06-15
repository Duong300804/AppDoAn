package com.example.appdoan.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appdoan.NguoiDung.NguoiDung;
import com.example.appdoan.R;
import com.example.appdoan.fragment.user.CartFragment;
import com.example.appdoan.fragment.user.HomeFragment;
import com.example.appdoan.object.GioHang;
import com.example.appdoan.tool.Database;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GioHangUserAdapter extends ArrayAdapter<GioHang> {
    Activity context;
    int Idlayout;
    ArrayList<GioHang> listGioHang;
    int soLuong = 0;
    FragmentManager fragmentManager;

    public GioHangUserAdapter(Activity context, int idlayout, ArrayList<GioHang> listGioHang, FragmentManager fragmentManager) {
        super(context, idlayout,listGioHang);
        this.context = context;
        this.Idlayout = idlayout;
        this.listGioHang = listGioHang;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Database database = new Database(context);
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        LayoutInflater layoutInflater = context.getLayoutInflater();

        convertView = layoutInflater.inflate(Idlayout,null);

        GioHang gioHang = listGioHang.get(position);

        ImageView productimage = convertView.findViewById(R.id.productImage);

        byte[] anhSanPham = gioHang.getAnhSanPham();
        Bitmap bitmap = BitmapFactory.decodeByteArray(anhSanPham, 0, anhSanPham.length);
        productimage.setImageBitmap(bitmap);

        TextView productName = convertView.findViewById(R.id.productName);
        TextView productPrice = convertView.findViewById(R.id.productPrice);
        TextView quantity = convertView.findViewById(R.id.quantityText);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        ImageButton decreaseButton = convertView.findViewById(R.id.decreaseButton);
        ImageButton increaseButton = convertView.findViewById(R.id.increaseButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long result = db.delete("giohang","magiohang=?",new String[] {String.valueOf(gioHang.getMaGioHang())});

                if (result == -1){
                    Toast.makeText(context, "Xoá Thất Bại", Toast.LENGTH_SHORT).show();
                }else{
                    // Xoá item khỏi danh sách
                    listGioHang.remove(position);
                    // Thông báo Adapter cập nhật lại dữ liệu
                    notifyDataSetChanged();

                    //Load lại fragment làm thay đổi tổng tiền
                    Fragment newFragment = new CartFragment();
                    loadFragment(newFragment);

                    // Kiểm tra nếu danh sách trống
                    if (listGioHang.isEmpty()) {
                        loadFragment(newFragment);//Sau khi thêm vào giỏ hàng thành công, quay lại trang Sản Phẩm
                    }
                }
            }
        });

        productName.setText(gioHang.getTenSanPham());
        productPrice.setText(formatCurrency(gioHang.getGia()));
        quantity.setText(gioHang.getSoLuong()+"");


        // Xử lý sự kiện nút tăng số lượng
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluongtrongkho = 0;
                Cursor cursor = db.rawQuery("SELECT soluong FROM sanpham WHERE masanpham = ?",new String[] {gioHang.getMaSanPham() });
                if(cursor.moveToFirst()){
                    soluongtrongkho = cursor.getInt(0);
                }
                cursor.close();
                soLuong = gioHang.getSoLuong();
                if (soLuong < soluongtrongkho){
                    soLuong++;
                    quantity.setText(String.valueOf(soLuong));
                    int giaSanPham = gioHang.getGiaSanPham();
                    int gia = giaSanPham * soLuong;
                    productPrice.setText(formatCurrency(gia));

                    //Cập nhật lại giá và số lượng trong bảng giohang
                    ContentValues cv = new ContentValues();
                    cv.put("soluong",soLuong);
                    cv.put("gia",gia);

                    long result = db.update("giohang",cv,"magiohang=?",new String[] {String.valueOf(gioHang.getMaGioHang())});
                    if (result == -1){
                        Log.d("UPDATE GIO HANG","SỬA THẤT BẠI");
                    }else{
                        Log.d("UPDATE GIO HANG","SỬA THÀNH CÔNG");
                        Fragment newFragment = new CartFragment();
                        loadFragment(newFragment);
                    }
                }else{
                    Toast.makeText(context, "Số lượng vượt quá sản phẩm của Shop", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soLuong = gioHang.getSoLuong();
                if (soLuong > 1) {
                    soLuong--;
                    quantity.setText(String.valueOf(soLuong));
                    int giaSanPham = gioHang.getGiaSanPham();
                    int gia = giaSanPham * soLuong;
                    productPrice.setText(formatCurrency(gia));

                    //Cập nhật lại giá và số lượng trong bảng giohang
                    ContentValues cv = new ContentValues();
                    cv.put("soluong",soLuong);
                    cv.put("gia",gia);

                    long result = db.update("giohang",cv,"magiohang = ?",new String[] {String.valueOf(gioHang.getMaGioHang())});
                    if (result == -1){
                        Log.d("UPDATE GIO HANG","SỬA THẤT BẠI");
                    }else{
                        Log.d("UPDATE GIO HANG","SỬA THÀNH CÔNG");
                        Fragment newFragment = new CartFragment();
                        loadFragment(newFragment);
                    }
                }
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

    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm vào back stack nếu cần
        fragmentTransaction.commit();
    }
}
