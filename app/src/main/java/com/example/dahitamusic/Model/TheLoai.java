package com.example.dahitamusic.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TheLoai implements Parcelable {
    private String tenTheLoai;
    private String anhTheLoai;
    private String idTheLoai;
    private int flag;
    private int quocGia;

    public TheLoai() {
    }

    public TheLoai(String tenTheLoai, String anhTheLoai, String idTheLoai, int flag, int quocGia) {
        this.tenTheLoai = tenTheLoai;
        this.anhTheLoai = anhTheLoai;
        this.idTheLoai = idTheLoai;
        this.flag = flag;
        this.quocGia = quocGia;
    }

    protected TheLoai(Parcel in) {
        tenTheLoai = in.readString();
        anhTheLoai = in.readString();
        idTheLoai = in.readString();
        flag = in.readInt();
        quocGia = in.readInt();
    }

    public static final Creator<TheLoai> CREATOR = new Creator<TheLoai>() {
        @Override
        public TheLoai createFromParcel(Parcel in) {
            return new TheLoai(in);
        }

        @Override
        public TheLoai[] newArray(int size) {
            return new TheLoai[size];
        }
    };

    public int getQuocGia() {
        return quocGia;
    }

    public void setQuocGia(int quocGia) {
        this.quocGia = quocGia;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(tenTheLoai);
        parcel.writeString(anhTheLoai);
        parcel.writeString(idTheLoai);
        parcel.writeInt(flag);
        parcel.writeInt(quocGia);
    }
}
