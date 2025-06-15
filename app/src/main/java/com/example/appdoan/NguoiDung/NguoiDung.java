package com.example.appdoan.NguoiDung;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appdoan.MainActivity;
import com.example.appdoan.R;
import com.example.appdoan.fragment.admin.QLSPFragment;
import com.example.appdoan.fragment.user.CartFragment;
import com.example.appdoan.fragment.user.EvaluateFragment;
import com.example.appdoan.fragment.user.HomeFragment;
import com.example.appdoan.fragment.user.MeFragment;
import com.example.appdoan.fragment.user.OrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NguoiDung extends AppCompatActivity {

    BottomNavigationView bottom_nav_bar;
    FrameLayout frame_layout;
    ImageButton imgDangXuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nguoi_dung);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottom_nav_bar = findViewById(R.id.bottom_nav_bar);
        frame_layout = findViewById(R.id.frame_layout);
        imgDangXuat = findViewById(R.id.imgDangXuat);

        // Hiển thị fragment mặc định khi activity được tạo
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment()); // Gọi fragment bạn muốn hiển thị mặc định
        }

        bottom_nav_bar.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if(itemID == R.id.home){
                    loadFragment(new HomeFragment());
                }
                if(itemID == R.id.me){
                    loadFragment(new MeFragment());
                }
                if(itemID == R.id.order){
                    loadFragment(new OrderFragment());
                }
                if(itemID == R.id.evaluate){
                    loadFragment(new EvaluateFragment());
                }
                if(itemID == R.id.cart){
                    loadFragment(new CartFragment());
                }
                return true;
            }
        });

        imgDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tạo thông báo
                AlertDialog.Builder builder = new AlertDialog.Builder(NguoiDung.this);
                builder.setTitle("Thông Báo");
                builder.setMessage("Bạn có muốn đăng xuất không?");
                builder.setCancelable(false); //Không cho phép thoát bằng cách nhấn ngoài

                //Trường Hợp người dùng bấm "Có"
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(NguoiDung.this, MainActivity.class));
                    }
                });
                //Trường Hợp người dùng bấm "Không"
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                //Khởi tạo và hiển thị hộp thoại Dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private void loadFragment(Fragment fragment)
    {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}