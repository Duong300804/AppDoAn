package com.example.appdoan.tool;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BatLoiDanhMuc {
    public boolean kiemtraTenDanhMuc(String tenDanhMuc ) {
        if (tenDanhMuc.isEmpty()){
        return false;
    }
        if (!tenDanhMuc.matches("^[\\p{L}\\s]{6,20}+$")){
            return false;
        }
        return true;
    }
    //        p{L} khớp với bất kỳ ký tự chữ cái nào, bao gồm cả các ký tự có dấu.
    //        s cho phép khoảng trắng

    public boolean kiemtraanhDanhMuc(byte[] anhDanhMuc){
        if (anhDanhMuc == null){
            return false;
        }
        return true;
    }

    public boolean kiemtramaDanhMuc(String maDanhMuc ) {
        if (maDanhMuc.isEmpty()){
            return false;
        }
        if (!maDanhMuc.matches("^[a-zA-Z0-9]+$")){
            return false;
        }
        return true;
    }

    public Boolean kiemTraTenDanhMucTonTai(String tenDanhMuc,SQLiteDatabase db){
        String[] selectionArg = { tenDanhMuc};
        Cursor cursor = db.rawQuery("SELECT * FROM danhmuc WHERE tendanhmuc = ?",selectionArg);
        if(cursor.getCount() !=0){
            return  false;
        }
        return true;
    }

    public Boolean kiemTraMaDanhMucTonTai(String maDanhMuc,SQLiteDatabase db){
        String[] selectionArg = { maDanhMuc};
        Cursor cursor = db.rawQuery("SELECT * FROM danhmuc WHERE madanhmuc = ?",selectionArg);
        if(cursor.getCount() !=0){
            return  false;
        }
        return true;
    }
}
