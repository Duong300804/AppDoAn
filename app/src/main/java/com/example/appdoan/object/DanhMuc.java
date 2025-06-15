package com.example.appdoan.object;

public class DanhMuc {
    private String maDanhMuc;
    private String tenDanhMuc;
    private byte[] anhDanhMuc;

    public DanhMuc(String maDanhMuc, String tenDanhMuc, byte[] anhDanhMuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.anhDanhMuc = anhDanhMuc;
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public byte[] getAnhDanhMuc() {
        return anhDanhMuc;
    }

    public void setAnhDanhMuc(byte[] anhDanhMuc) {
        this.anhDanhMuc = anhDanhMuc;
    }
}
