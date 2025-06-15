package com.example.appdoan.fragment.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appdoan.R;
import com.example.appdoan.adapter.GioHangUserAdapter;
import com.example.appdoan.object.GioHang;
import com.example.appdoan.tool.Database;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartFragment extends Fragment {
    Button btnCheckOut;
    ListView lvGioHang;
    TextView tvSubtotalAmount,tvShippingAmount,tvTotalAmount,txtGioHang;
    ArrayList<GioHang> listGioHang;
    ArrayAdapter<GioHang> adapterGioHang;
    ConstraintLayout cartSummaryLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        btnCheckOut = view.findViewById(R.id.btnCheckout);
        lvGioHang = view.findViewById(R.id.lvGioHang);
        tvSubtotalAmount = view.findViewById(R.id.tvSubtotalAmount);
        tvShippingAmount = view.findViewById(R.id.tvShippingAmount);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        txtGioHang = view.findViewById(R.id.txtgiohang);
        cartSummaryLayout = view.findViewById(R.id.cartSummaryLayout);

        int phiVanChuyen = 20000;
        tvShippingAmount.setText(formatCurrency(phiVanChuyen));

        listGioHang = new ArrayList<>();

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        // Lấy mã tài khoản từ SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("mataikhoan", Context.MODE_PRIVATE);
        int mataikhoan = sharedPref.getInt("mataikhoan", -1); // -1 là giá trị mặc định nếu không tìm thấy "mataikhoan"

        String query = "SELECT *\n" +
                "FROM giohang gh\n" +
                "JOIN sanpham sp WHERE gh.masanpham = sp.masanpham AND mataikhoan =?";

        String[] selectionArg = {String.valueOf(mataikhoan)};

        Cursor cursor = db.rawQuery(query,selectionArg);

        if(cursor.getCount() == 0){ //Kiểm tra xem trong giỏ hàng của tài khoản này có gì chưa
            txtGioHang.setText("Giỏ hàng của bạn đang trống!");
            cartSummaryLayout.setVisibility(View.INVISIBLE); //Để Trống ListView
            btnCheckOut.setVisibility(View.INVISIBLE); //Để Trống Button Thanh Toán
        }else{
            int tamTinh = 0;
            if(cursor.moveToFirst()){
                do{
                    int maGioHang = cursor.getInt(0);
                    String maSanPham = cursor.getString(1);
                    int giaSanPham = cursor.getInt(10);

                    byte[] anhSanPham = cursor.getBlob(8);
                    String tenSanPham = cursor.getString(7);
                    int soLuong = cursor.getInt(2);
                    int gia = cursor.getInt(4);

                    int tongTienMotSanPham = soLuong * giaSanPham;
                    tamTinh += tongTienMotSanPham;

                    listGioHang.add(new GioHang(maGioHang,maSanPham,soLuong,mataikhoan,gia,anhSanPham,tenSanPham,giaSanPham));

                }while (cursor.moveToNext());
            }
            tvSubtotalAmount.setText(formatCurrency(tamTinh));

            int tongTien = tamTinh + phiVanChuyen;
            tvTotalAmount.setText(formatCurrency(tongTien));

            cursor.close();

            adapterGioHang = new GioHangUserAdapter(requireActivity(),R.layout.item_giohang,listGioHang, requireActivity().getSupportFragmentManager());
            lvGioHang.setAdapter(adapterGioHang);
        }

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ThanhToanFragment());
            }
        });
        return view;
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
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}