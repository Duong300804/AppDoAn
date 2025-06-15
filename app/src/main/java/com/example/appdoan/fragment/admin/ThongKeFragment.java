package com.example.appdoan.fragment.admin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.adapter.SanPhamAdapterAdmin;
import com.example.appdoan.object.SanPham;
import com.example.appdoan.tool.Database;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ThongKeFragment extends Fragment {
    TextView tvTongDoanhThu, tvTongDonHang;
    ListView lvSanPhamOut;
    ArrayList<SanPham> listSanPham;
    ArrayAdapter<SanPham> sanPhamArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_ke, container, false);

        tvTongDoanhThu = view.findViewById(R.id.tvTongDoanhThu);
        tvTongDonHang = view.findViewById(R.id.tvTongDonHang);
        lvSanPhamOut = view.findViewById(R.id.lvSanPhamOut);
        listSanPham = new ArrayList<>();

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        Cursor cursor;
        cursor = db.rawQuery("SELECT count(*) FROM donhang ",null);
        if(cursor.moveToFirst()){
            int tongDonHang = cursor.getInt(0);
            tvTongDonHang.setText("Số Lượng Đơn Hàng: "+ tongDonHang );
        }
        cursor = db.rawQuery("SELECT sum(tongtien) FROM donhang WHERE trangthai=?",new String[] { "Hoàn thành" });
        if(cursor.moveToFirst()){
            int tongDoanhThu = cursor.getInt(0);
            tvTongDoanhThu.setText("Tổng Doanh Thu: " +formatCurrency(tongDoanhThu));
        }

        cursor = db.rawQuery("SELECT * FROM sanpham WHERE soluong = 0",null);
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                String maSanPham = cursor.getString(0);
                String maDanhMuc = cursor.getString(1);
                String tenSanPham = cursor.getString(2);
                byte[] anhSanPham = cursor.getBlob(3);
                int soLuong = cursor.getInt(4);
                int gia = cursor.getInt(5);
                String moTa = cursor.getString(6);

                listSanPham.add(new SanPham(maSanPham,maDanhMuc,tenSanPham,anhSanPham,soLuong,gia,moTa));
            }
        }
        cursor.close();

        sanPhamArrayAdapter = new SanPhamAdapterAdmin(getActivity(),R.layout.item_sanphamadmin,listSanPham);
        lvSanPhamOut.setAdapter(sanPhamArrayAdapter);

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
}