package com.example.appdoan.object;

public class GioHang {
    private int maGioHang;
    private String maSanPham;
    private int soLuong;
    private int maTaiKhoan;
    private int gia;
    private byte[] anhSanPham;
    private String tenSanPham;
    private int giaSanPham;

    public GioHang(int maGioHang, String maSanPham, int soLuong, int maTaiKhoan, int gia, byte[] anhSanPham, String tenSanPham,int giaSanPham) {
        this.maGioHang = maGioHang;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.maTaiKhoan = maTaiKhoan;
        this.gia = gia;
        this.anhSanPham = anhSanPham;
        this.tenSanPham = tenSanPham;
        this.giaSanPham = giaSanPham;
    }

    public int getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(int giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public int getMaGioHang() {
        return maGioHang;
    }

    public void setMaGioHang(int maGioHang) {
        this.maGioHang = maGioHang;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public byte[] getAnhSanPham() {
        return anhSanPham;
    }

    public void setAnhSanPham(byte[] anhSanPham) {
        this.anhSanPham = anhSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }
}
