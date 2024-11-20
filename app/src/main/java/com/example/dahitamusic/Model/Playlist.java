package com.example.dahitamusic.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Playlist implements Parcelable {
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

    protected Playlist(Parcel in) {
        idPlaylist = in.readString();
        idTheLoai = in.readString();
        idChuDe = in.readString();
        tenPlaylist = in.readString();
        anhPlaylist = in.readString();
        yeuThich = in.readByte() != 0;
        createPlaylisYeuThich = in.readByte() != 0;
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idPlaylist);
        parcel.writeString(idTheLoai);
        parcel.writeString(idChuDe);
        parcel.writeString(tenPlaylist);
        parcel.writeString(anhPlaylist);
        parcel.writeByte((byte) (yeuThich ? 1 : 0));
        parcel.writeByte((byte) (createPlaylisYeuThich ? 1 : 0));
    }
}
