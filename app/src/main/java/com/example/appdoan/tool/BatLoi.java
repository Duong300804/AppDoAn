package com.example.appdoan.tool;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BatLoi {
    public Boolean kiemTraTenDangNhap(String tenDangNhap){
        if (tenDangNhap.isEmpty()) {
            return false;
        }
        if (!tenDangNhap.matches("^[a-zA-Z0-9]{6,20}$")){
            return false;
        }
        return true;
    }

    public Boolean kiemTraMatKhau (String matKhau){
        if (matKhau.isEmpty()) {
            return false;
        }
        if (!matKhau.matches("^[a-zA-Z0-9]{6,20}$")){
            return false;
        }
        return true;
    }

    public Boolean kiemTraEmail (String email){
        if (email.isEmpty()) {
            return false;
        }
        if (!email.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")){
            return false;
        }
        // ^[a-zA-Z0-9]+: Phần đầu tiên của email (username) có thể chứa các ký tự chữ, số
        // @[a-zA-Z0-9.-]+: Phần tên miền ngay sau dấu @ chỉ chứa chữ cái, số, dấu chấm và dấu gạch ngang.
        // \\.[a-zA-Z]{2,6}$: Phần đuôi của tên miền (ví dụ: .com, .org, .net) với độ dài từ 2 đến 6 ký tự.
        return true;
    }

    public Boolean kiemTraEmailTonTai(String email, SQLiteDatabase db){
        String[] selectionArg = { email };
        Cursor cursor = db.rawQuery("SELECT * FROM taikhoan WHERE EMAIL = ?",selectionArg);

        if(cursor.getCount() != 0){
            return false;
        }
        return true;
    }

    public Boolean kiemTraTenDangNhapTonTai(String tenDangNhap,SQLiteDatabase db){
        String[] selectionArg = { tenDangNhap };
        Cursor cursor = db.rawQuery("SELECT * FROM taikhoan WHERE tentaikhoan = ?",selectionArg);

        if(cursor.getCount() !=0){
            return  false;
        }
        return true;
    }
}
