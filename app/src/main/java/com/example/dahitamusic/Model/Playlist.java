package com.example.dahitamusic.Model;

import java.io.Serializable;

public class Playlist implements Serializable {
    private String idPlaylist;
    private String idTheLoai;
    private String idChuDe;
    private String tenPlaylist;
    private String anhPlaylist;

    public Playlist() {
    }

    public Playlist(String idPlaylist, String idTheLoai, String idChuDe, String tenPlaylist, String anhPlaylist) {
        this.idPlaylist = idPlaylist;
        this.idTheLoai = idTheLoai;
        this.idChuDe = idChuDe;
        this.tenPlaylist = tenPlaylist;
        this.anhPlaylist = anhPlaylist;
    }

    public String getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getIdTheLoai() {
        return idTheLoai;
    }

    public void setIdTheLoai(String idTheLoai) {
        this.idTheLoai = idTheLoai;
    }

    public String getIdChuDe() {
        return idChuDe;
    }

    public void setIdChuDe(String idChuDe) {
        this.idChuDe = idChuDe;
    }

    public String getTenPlaylist() {
        return tenPlaylist;
    }

    public void setTenPlaylist(String tenPlaylist) {
        this.tenPlaylist = tenPlaylist;
    }

    public String getAnhPlaylist() {
        return anhPlaylist;
    }

    public void setAnhPlaylist(String anhPlaylist) {
        this.anhPlaylist = anhPlaylist;
    }
}
