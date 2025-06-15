package com.example.appdoan.object;

public class DonHang {
    private int maDonHang;
    private int maTaiKhoan;
    private String danhSachSanPham;
    private int tongTien;
    private String ngayMua;
    private String trangThai;

    public DonHang(int maDonHang, int maTaiKhoan, String danhSachSanPham, int tongTien, String ngayMua, String trangThai) {
        this.maDonHang = maDonHang;
        this.maTaiKhoan = maTaiKhoan;
        this.danhSachSanPham = danhSachSanPham;
        this.tongTien = tongTien;
        this.ngayMua = ngayMua;
        this.trangThai = trangThai;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public void setDanhSachSanPham(String danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(String ngayMua) {
        this.ngayMua = ngayMua;
    }
}