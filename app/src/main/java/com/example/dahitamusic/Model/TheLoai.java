package com.example.dahitamusic.Model;

public class TheLoai {
    private String tenTheLoai;
    private String anhTheLoai;
    private String idTheLoai;
    private int flag;

    public TheLoai() {
    }

    public TheLoai(String tenTheLoai, String anhTheLoai, String idTheLoai, int flag) {
        this.tenTheLoai = tenTheLoai;
        this.anhTheLoai = anhTheLoai;
        this.idTheLoai = idTheLoai;
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public String getAnhTheLoai() {
        return anhTheLoai;
    }

    public void setAnhTheLoai(String anhTheLoai) {
        this.anhTheLoai = anhTheLoai;
    }

    public String getIdTheLoai() {
        return idTheLoai;
    }

    public void setIdTheLoai(String idTheLoai) {
        this.idTheLoai = idTheLoai;
    }
}
