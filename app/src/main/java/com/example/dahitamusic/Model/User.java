package com.example.dahitamusic.Model;

import java.util.List;

public class User {
    private String tenDangNhap;
    private String email;
    private List<String> baiHatYeuThich;
    private List<String> playListYeuThich;
    private List<String> albumYeuThich;
    private List<String> baiHatDaTai;

    public User() {
    }

    public User(String tenDangNhap, String email) {
        this.tenDangNhap = tenDangNhap;
        this.email = email;
    }

    public User(String tenDangNhap, String email, List<String> baiHatYeuThich, List<String> playListYeuThich, List<String> albumYeuThich, List<String> baiHatDaTai) {
        this.tenDangNhap = tenDangNhap;
        this.email = email;
        this.baiHatYeuThich = baiHatYeuThich;
        this.playListYeuThich = playListYeuThich;
        this.albumYeuThich = albumYeuThich;
        this.baiHatDaTai = baiHatDaTai;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getBaiHatYeuThich() {
        return baiHatYeuThich;
    }

    public void setBaiHatYeuThich(List<String> baiHatYeuThich) {
        this.baiHatYeuThich = baiHatYeuThich;
    }

    public List<String> getPlayListYeuThich() {
        return playListYeuThich;
    }

    public void setPlayListYeuThich(List<String> playListYeuThich) {
        this.playListYeuThich = playListYeuThich;
    }

    public List<String> getAlbumYeuThich() {
        return albumYeuThich;
    }

    public void setAlbumYeuThich(List<String> albumYeuThich) {
        this.albumYeuThich = albumYeuThich;
    }

    public List<String> getBaiHatDaTai() {
        return baiHatDaTai;
    }

    public void setBaiHatDaTai(List<String> baiHatDaTai) {
        this.baiHatDaTai = baiHatDaTai;
    }
}
