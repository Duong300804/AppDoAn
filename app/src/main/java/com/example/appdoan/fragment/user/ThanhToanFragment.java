package com.example.appdoan.fragment.user;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.adapter.ThanhToanApdapterUser;
import com.example.appdoan.object.GioHang;
import com.example.appdoan.tool.Database;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ThanhToanFragment extends Fragment {
    ListView lvThanhToan;
    ArrayAdapter<GioHang> arrayAdapter;
    ArrayList<GioHang> listGioHang;
    TextView tongtien;
    Button btnXacNhanThanhToan;
    String DanhSachSanPham = "";
    EditText edtHoTen,edtSoDienThoai,edtDiaChi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thanh_toan, container, false);
        lvThanhToan = view.findViewById(R.id.lvThanhToan);
        tongtien = view.findViewById(R.id.tongtien);
        btnXacNhanThanhToan = view.findViewById(R.id.btnXacNhanThanhToan);
        edtHoTen = view.findViewById(R.id.edtHoTenInput);
        edtSoDienThoai = view.findViewById(R.id.edtSoDienThoaiInput);
        edtDiaChi = view.findViewById(R.id.edtDiaChiInput);
        listGioHang = new ArrayList<>();

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        // Lấy mã tài khoản từ SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("mataikhoan", Context.MODE_PRIVATE);
        int mataikhoan = sharedPref.getInt("mataikhoan", -1); // -1 là giá trị mặc định nếu không tìm thấy "mataikhoan"

        String[] selectionArg = {String.valueOf(mataikhoan)};

        String query;
        query = "SELECT *\n" +
                "FROM giohang gh\n" +
                "JOIN sanpham sp WHERE gh.masanpham = sp.masanpham AND mataikhoan =?";

        Cursor cursor;
        cursor = db.rawQuery(query,selectionArg);

        //Tính phí
        int phiVanChuyen = 20000;
        int tamTinh = 0;

        // Load giỏ hàng
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
                DanhSachSanPham += " "+ tenSanPham + "("+soLuong+")" ;

                listGioHang.add(new GioHang(maGioHang,maSanPham,soLuong,mataikhoan,gia,anhSanPham,tenSanPham,giaSanPham));

            }while (cursor.moveToNext());
        }

        int tongTien = tamTinh + phiVanChuyen;
        tongtien.setText(formatCurrency(tongTien));

        // Tắt các ô edittext
        edtHoTen.setEnabled(false);
        edtSoDienThoai.setEnabled(false);
        edtDiaChi.setEnabled(false);

        // Load các ô edittext
        query = "SELECT * FROM thongtintaikhoan WHERE mataikhoan =?";
        cursor = db.rawQuery(query,new String[] {String.valueOf(mataikhoan)});
        if(cursor.moveToFirst()){
            do {
                edtHoTen.setText(cursor.getString(1));
                edtSoDienThoai.setText(cursor.getString(2));
                edtDiaChi.setText(cursor.getString(5));
            }while (cursor.moveToNext());
        }

        cursor.close();

        arrayAdapter = new ThanhToanApdapterUser(requireActivity(),R.layout.item_thanhtoan,listGioHang);
        lvThanhToan.setAdapter(arrayAdapter);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String ngaymua = sdf.format(Calendar.getInstance().getTime());

        btnXacNhanThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
                cv.put("mataikhoan",mataikhoan);
                cv.put("danhsachsanpham",DanhSachSanPham);
                cv.put("tongtien",tongTien);
                cv.put("ngaymua",ngaymua);
                cv.put("trangthai","Đang Giao");

                if(edtDiaChi.getText().toString().trim().equals("") || edtSoDienThoai.getText().toString().trim().equals("") || edtHoTen.getText().toString().trim().equals("")){
                    Toast.makeText(requireActivity(), "Vui lòng đăng ký đầy đủ thông tin trước khi thanh toán", Toast.LENGTH_SHORT).show();
                }else{
                    long result = db.insert("donhang",null,cv);
                    if (result == -1){
                        Toast.makeText(requireActivity(), "Tạo Đơn Hàng Thất Bại", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(requireActivity(), "Tạo Đơn Hàng Thành Công", Toast.LENGTH_SHORT).show();

                        // Vòng lặp để trừ số lượng sản phẩm trong bảng sanpham
                        for (GioHang gioHang : listGioHang) {
                            String updateQuery = "UPDATE sanpham SET soluong = soluong - ? WHERE masanpham = ?";
                            db.execSQL(updateQuery, new Object[]{gioHang.getSoLuong(), gioHang.getMaSanPham()});
                        }

                        // Xoá giỏ hàng
                        long xoagiohang = db.delete("giohang","mataikhoan=?",new String[] {String.valueOf(mataikhoan)});
                        if(xoagiohang == -1){
                            Toast.makeText(requireActivity(), "DELETE FAILED", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            loadFragment(new OrderFragment());
                        }
                    }
                }
            }
        });
        return view;
    }

    private void loadFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
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