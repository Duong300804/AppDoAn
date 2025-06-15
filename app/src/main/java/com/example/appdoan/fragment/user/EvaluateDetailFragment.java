package com.example.appdoan.fragment.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdoan.R;
import com.example.appdoan.fragment.admin.QLSPFragment;
import com.example.appdoan.tool.Database;

public class EvaluateDetailFragment extends Fragment {
    RatingBar ratingBar;
    TextView tvOrderCode;
    EditText edtComment,edtComment2,edtComment3;
    Button btnGuiDanhGia;
    float ratingValue = 0;
    int maDonHang = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluate_detail, container, false);

        ratingBar = view.findViewById(R.id.ratingBar);
        tvOrderCode = view.findViewById(R.id.tvOrderCode);
        edtComment = view.findViewById(R.id.edtComment);
        edtComment2 = view.findViewById(R.id.edtComment2);
        edtComment3 = view.findViewById(R.id.edtComment3);
        btnGuiDanhGia = view.findViewById(R.id.btnGuiDanhGia);

        Database database = new Database(requireActivity());
        database.createDatabase();
        SQLiteDatabase db = database.openDatabase();

        Bundle bundle = getArguments();
        if (bundle != null) {
            maDonHang = bundle.getInt("maDonHang");
            tvOrderCode.setText("Mã đơn hàng: "+maDonHang);
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingValue = v;
            }
        });

        btnGuiDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingValue == 0){
                    Toast.makeText(requireActivity(), "Vui lòng chọn sao!", Toast.LENGTH_SHORT).show();
                }else{
                    int soSao = (int) ratingValue;
                    String nhanXet = edtComment.getText().toString();
                    String nhanXet2 = edtComment2.getText().toString();
                    String nhanXet3 = edtComment3.getText().toString();
                    String danhGia = "Nhận xét: " + nhanXet + " Dịch vụ người bán: " + nhanXet2 + " Tốc độ giao hàng: "+ nhanXet3;

                    ContentValues cv = new ContentValues();
                    cv.put("madonhang",maDonHang);
                    cv.put("sosao",soSao);
                    cv.put("danhgia",danhGia);

                    long result = db.insert("danhgia",null,cv);
                    if(result == -1){
                        Toast.makeText(requireActivity(), "Đánh giá thất bại", Toast.LENGTH_SHORT).show();
                    }else{
                        cv = new ContentValues();

                        cv.put("trangthai","Hoàn Thành");

                        result = db.update("donhang",cv,"madonhang=?",new String[] {String.valueOf(maDonHang)});
                        if (result == -1){
                            Toast.makeText(requireActivity(), "UPDATE DONHANG FAILED", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(requireActivity(), "Đánh Giá Thành Công", Toast.LENGTH_SHORT).show();
                            // Khởi tạo Fragment
                            EvaluateFragment evaluateFragment = new EvaluateFragment();

                            // Lấy FragmentManager và bắt đầu giao dịch
                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                            // Thay thế Fragment hiện tại bằng Fragment mới
                            transaction.replace(R.id.frame_layout, evaluateFragment).addToBackStack(null).commit(); // Thực hiện giao dịch
                        }
                    }
                }
            }
        });

        return view;
    }
}