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
    private String userId;
    private Map<String, Boolean> danhSachBaiHat;

    public Playlist() {
    }

    public Playlist(String idPlaylist, String tenPlaylist, String userId) {
        this.idPlaylist = idPlaylist;
        this.tenPlaylist = tenPlaylist;
        this.userId = userId;
    }

    public Playlist(String idPlaylist, String idTheLoai, String idChuDe, String tenPlaylist, String anhPlaylist, String userId, Map<String, Boolean> danhSachBaiHat) {
        this.idPlaylist = idPlaylist;
        this.idTheLoai = idTheLoai;
        this.idChuDe = idChuDe;
        this.tenPlaylist = tenPlaylist;
        this.anhPlaylist = anhPlaylist;
        this.userId = userId;
        this.danhSachBaiHat = danhSachBaiHat;
    }

    protected Playlist(Parcel in) {
        idPlaylist = in.readString();
        idTheLoai = in.readString();
        idChuDe = in.readString();
        tenPlaylist = in.readString();
        anhPlaylist = in.readString();
        userId = in.readString();

        int size = in.readInt();
        danhSachBaiHat = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            boolean value = in.readByte() != 0;
            danhSachBaiHat.put(key, value);
        }
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

    public Map<String, Boolean> getDanhSachBaiHat() {
        return danhSachBaiHat;
    }

    public void setDanhSachBaiHat(Map<String, Boolean> danhSachBaiHat) {
        this.danhSachBaiHat = danhSachBaiHat;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        parcel.writeString(userId);

        if (danhSachBaiHat != null) {
            parcel.writeInt(danhSachBaiHat.size());
            for (Map.Entry<String, Boolean> entry : danhSachBaiHat.entrySet()) {
                parcel.writeString(entry.getKey());
                parcel.writeByte((byte) (entry.getValue() ? 1 : 0));
            }
        } else {
            parcel.writeInt(0); // Ghi 0 nếu danh sách bài hát null
        }
    }

}
