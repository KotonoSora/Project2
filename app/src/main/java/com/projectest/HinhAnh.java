package com.projectest;

/**
 * Created by Admin on 8/21/2017.
 */

public class HinhAnh {
    private String Ten;
    private int Hinh;

    public HinhAnh(String ten, int hinh) {
        Ten = ten;
        Hinh = hinh;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public int getHinh() {
        return Hinh;
    }

    public void setHinh(int hinh) {
        Hinh = hinh;
    }
}
