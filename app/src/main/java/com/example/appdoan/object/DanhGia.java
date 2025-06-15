package com.example.appdoan.object;

public class DanhGia {
    private int maDanhGia;
    private int maDonHang;
    private int soSao;
    private String danhGia;

    public DanhGia(int maDanhGia, int maDonHang, int soSao, String danhGia) {
        this.maDanhGia = maDanhGia;
        this.maDonHang = maDonHang;
        this.soSao = soSao;
        this.danhGia = danhGia;
    }

    public int getMaDanhGia() {
        return maDanhGia;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public int getSoSao() {
        return soSao;
    }

    public String getDanhGia() {
        return danhGia;
    }
}
