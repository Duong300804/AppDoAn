package com.example.appdoan.QuanLy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appdoan.MainActivity;
import com.example.appdoan.R;
import com.example.appdoan.fragment.admin.QLDGFragment;
import com.example.appdoan.fragment.admin.QLDHFragment;
import com.example.appdoan.fragment.admin.QLDMFragment;
import com.example.appdoan.fragment.admin.QLSPFragment;
import com.example.appdoan.fragment.admin.QLTKFragment;
import com.example.appdoan.fragment.admin.ThongKeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class QuanLy extends AppCompatActivity {
    BottomNavigationView BNV;
    FrameLayout FL;
    ImageButton imgBack,imgThongKe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);

        BNV = findViewById(R.id.BNV);
        FL = findViewById(R.id.FL);
        imgBack = findViewById(R.id.imgBack);
        imgThongKe = findViewById(R.id.imgThongKe);

        // Hiển thị fragment mặc định khi activity được tạo
        if (savedInstanceState == null) {
            loadFragment(new QLSPFragment()); // Gọi fragment bạn muốn hiển thị mặc định
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuanLy.this, MainActivity.class));
            }
        });

        imgThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ThongKeFragment());
            }
        });

        BNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if(itemID == R.id.QuanLySanPham){
                    loadFragment(new QLSPFragment());
                }
                if (itemID == R.id.QuanLyDanhMuc){
                    loadFragment(new QLDMFragment());
                }
                if (itemID == R.id.QuanLyTaiKhoan){
                    loadFragment(new QLTKFragment());
                }
                if (itemID == R.id.QuanLyDonHang){
                    loadFragment(new QLDHFragment());
                }
                if (itemID == R.id.QuanLyDanhGia){
                    loadFragment(new QLDGFragment());
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment)
    {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FL,fragment);
        fragmentTransaction.commit();
    }
}