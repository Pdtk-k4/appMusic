package com.example.dahitamusic.Model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class BaiHat implements Parcelable {
    private String idBaiHat;
    private String anhBaiHat;
    private String caSi;
    private String tenBaiHat;
    private String linkNhac;
    private String idAlbum;
    private String idPlaylist;
    private String idTheLoai;
    private int luotThich;

    public BaiHat() {
    }

    public BaiHat(String idBaiHat, String anhBaiHat, String caSi, String tenBaiHat, String linkNhac, String idAlbum, String idPlaylist, String idTheLoai, int luotThich) {
        this.idBaiHat = idBaiHat;
        this.anhBaiHat = anhBaiHat;
        this.caSi = caSi;
        this.tenBaiHat = tenBaiHat;
        this.linkNhac = linkNhac;
        this.idAlbum = idAlbum;
        this.idPlaylist = idPlaylist;
        this.idTheLoai = idTheLoai;
        this.luotThich = luotThich;
    }

    protected BaiHat(Parcel in) {
        idBaiHat = in.readString();
        anhBaiHat = in.readString();
        caSi = in.readString();
        tenBaiHat = in.readString();
        linkNhac = in.readString();
        idAlbum = in.readString();
        idPlaylist = in.readString();
        idTheLoai = in.readString();
        luotThich = in.readInt();
    }

    public static final Creator<BaiHat> CREATOR = new Creator<BaiHat>() {
        @Override
        public BaiHat createFromParcel(Parcel in) {
            return new BaiHat(in);
        }

        @Override
        public BaiHat[] newArray(int size) {
            return new BaiHat[size];
        }
    };

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getAnhBaiHat() {
        return this.anhBaiHat;
    }

    public void setAnhBaiHat(String anhBaiHat) {
        this.anhBaiHat = anhBaiHat;
    }

    public String getCaSi() {
        return caSi;
    }

    public void setCaSi(String caSi) {
        this.caSi = caSi;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getLinkNhac() {
        return linkNhac;
    }

    public void setLinkNhac(String linkNhac) {
        this.linkNhac = linkNhac;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
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

    public int getLuotThich() {
        return luotThich;
    }

    public void setLuotThich(int luotThich) {
        this.luotThich = luotThich;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idBaiHat);
        parcel.writeString(anhBaiHat);
        parcel.writeString(caSi);
        parcel.writeString(tenBaiHat);
        parcel.writeString(linkNhac);
        parcel.writeString(idAlbum);
        parcel.writeString(idPlaylist);
        parcel.writeString(idTheLoai);
        parcel.writeInt(luotThich);
    }
}
