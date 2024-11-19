package com.example.dahitamusic.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Playlist implements Serializable {
    private String idPlaylist;
    private String idTheLoai;
    private String idChuDe;
    private String tenPlaylist;
    private String anhPlaylist;
    private boolean yeuThich;
    private boolean createPlaylisYeuThich;

    public Playlist() {
    }
    public Playlist(String idPlaylist, String tenPlaylist, boolean yeuThich, boolean createPlaylisYeuThich) {
        this.idPlaylist = idPlaylist;
        this.tenPlaylist = tenPlaylist;
        this.yeuThich = yeuThich;
        this.createPlaylisYeuThich = createPlaylisYeuThich;
    }

    public Playlist(String idPlaylist, String idTheLoai, String idChuDe, String tenPlaylist, String anhPlaylist, Boolean yeuThich) {
        this.idPlaylist = idPlaylist;
        this.idTheLoai = idTheLoai;
        this.idChuDe = idChuDe;
        this.tenPlaylist = tenPlaylist;
        this.anhPlaylist = anhPlaylist;
        this.yeuThich = yeuThich;
    }

    public boolean isCreatePlaylisYeuThich() {
        return createPlaylisYeuThich;
    }

    public void setCreatePlaylisYeuThich(boolean createPlaylisYeuThich) {
        this.createPlaylisYeuThich = createPlaylisYeuThich;
    }

    public boolean getYeuThich() {
        return yeuThich;
    }

    public void setYeuThich(boolean yeuThich) {
        this.yeuThich = yeuThich;
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

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("yeuThich", yeuThich);
        return result;
    }
}
