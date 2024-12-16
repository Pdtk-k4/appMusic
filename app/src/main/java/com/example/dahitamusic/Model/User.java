package com.example.dahitamusic.Model;

import java.util.List;

public class User {
    private String tenNguoiDung;
    private String email;
    private List<String> podcastYeuThich;
    private List<String> baiHatYeuThich;
    private List<String> playListYeuThich;
    private List<String> albumYeuThich;
    private List<String> baiHatDaTai;

    public User() {
    }

    public User(String tenNguoiDung, String email) {
        this.tenNguoiDung = tenNguoiDung;
        this.email = email;
    }

    public User(String tenNguoiDung, String email, List<String> baiHatYeuThich, List<String> playListYeuThich, List<String> albumYeuThich, List<String> baiHatDaTai, List<String> podcastYeuThich) {
        this.tenNguoiDung = tenNguoiDung;
        this.email = email;
        this.baiHatYeuThich = baiHatYeuThich;
        this.playListYeuThich = playListYeuThich;
        this.albumYeuThich = albumYeuThich;
        this.baiHatDaTai = baiHatDaTai;
        this.podcastYeuThich = podcastYeuThich;

    }

    public List<String> getPodcastYeuThich() {
        return podcastYeuThich;
    }

    public void setPodcastYeuThich(List<String> podcastYeuThich) {
        this.podcastYeuThich = podcastYeuThich;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
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
