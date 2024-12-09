package com.example.dahitamusic.Model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaiHat implements Parcelable {
    private String idBaiHat;
    private String anhBaiHat;
    private String caSi;
    private String tenBaiHat;
    private String linkNhac;
    private String idAlbum;
    private String idPlaylist;
    private String idTheLoai;
    private String anhQuangCao;
    private String idQuangCao;
    private boolean yeuThich;

    public BaiHat() {
    }

    public BaiHat(String idBaiHat, String anhBaiHat, String caSi, String tenBaiHat, String linkNhac, String idAlbum, String idPlaylist, String idTheLoai, String anhQuangCao, String idQuangCao, boolean yeuThich) {
        this.idBaiHat = idBaiHat;
        this.anhBaiHat = anhBaiHat;
        this.caSi = caSi;
        this.tenBaiHat = tenBaiHat;
        this.linkNhac = linkNhac;
        this.idAlbum = idAlbum;
        this.idPlaylist = idPlaylist;
        this.idTheLoai = idTheLoai;
        this.anhQuangCao = anhQuangCao;
        this.idQuangCao = idQuangCao;
        this.yeuThich = yeuThich;
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
        anhQuangCao = in.readString();
        idQuangCao = in.readString();
        yeuThich = in.readByte() != 0;
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
        return anhBaiHat;
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

    public String getAnhQuangCao() {
        return anhQuangCao;
    }

    public void setAnhQuangCao(String anhQuangCao) {
        this.anhQuangCao = anhQuangCao;
    }

    public String getIdQuangCao() {
        return idQuangCao;
    }

    public void setIdQuangCao(String idQuangCao) {
        this.idQuangCao = idQuangCao;
    }

    public boolean isYeuThich() {
        return yeuThich;
    }

    public void setYeuThich(boolean yeuThich) {
        this.yeuThich = yeuThich;
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
        parcel.writeString(idBaiHat);
        parcel.writeString(anhBaiHat);
        parcel.writeString(caSi);
        parcel.writeString(tenBaiHat);
        parcel.writeString(linkNhac);
        parcel.writeString(idAlbum);
        parcel.writeString(idPlaylist);
        parcel.writeString(idTheLoai);
        parcel.writeString(anhQuangCao);
        parcel.writeString(idQuangCao);
        parcel.writeByte((byte) (yeuThich ? 1 : 0));
    }
}
