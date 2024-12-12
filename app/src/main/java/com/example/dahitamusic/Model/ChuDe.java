package com.example.dahitamusic.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.lang.reflect.ParameterizedType;

public class ChuDe implements Parcelable {
    private String tenChuDe;
    private String idChuDe;
    private String anhChuDe;

    public ChuDe() {
    }

    public ChuDe(String tenChuDe, String idChuDe, String anhChuDe) {
        this.tenChuDe = tenChuDe;
        this.idChuDe = idChuDe;
        this.anhChuDe = anhChuDe;
    }

    protected ChuDe(Parcel in) {
        tenChuDe = in.readString();
        idChuDe = in.readString();
        anhChuDe = in.readString();
    }

    public static final Creator<ChuDe> CREATOR = new Creator<ChuDe>() {
        @Override
        public ChuDe createFromParcel(Parcel in) {
            return new ChuDe(in);
        }

        @Override
        public ChuDe[] newArray(int size) {
            return new ChuDe[size];
        }
    };

    public String getTenChuDe() {
        return tenChuDe;
    }

    public void setTenChuDe(String tenChuDe) {
        this.tenChuDe = tenChuDe;
    }

    public String getIdChuDe() {
        return idChuDe;
    }

    public void setIdChuDe(String idChuDe) {
        this.idChuDe = idChuDe;
    }

    public String getAnhChuDe() {
        return anhChuDe;
    }

    public void setAnhChuDe(String anhChuDe) {
        this.anhChuDe = anhChuDe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(tenChuDe);
        parcel.writeString(idChuDe);
        parcel.writeString(anhChuDe);
    }
}
