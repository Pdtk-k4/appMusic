package com.example.dahitamusic.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Album implements Parcelable {

    private String tenAlbum;
    private String tenCaSiAlbum;
    private String anhAlbum;
    private String idAlbum;

    public Album() {
    }

    public Album(String tenAlbum, String tenCaSiAlbum, String anhAlbum, String idAlbum) {
        this.tenAlbum = tenAlbum;
        this.tenCaSiAlbum = tenCaSiAlbum;
        this.anhAlbum = anhAlbum;
        this.idAlbum = idAlbum;
    }

    protected Album(Parcel in) {
        tenAlbum = in.readString();
        tenCaSiAlbum = in.readString();
        anhAlbum = in.readString();
        idAlbum = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getTenCaSiAlbum() {
        return tenCaSiAlbum;
    }

    public void setTenCaSiAlbum(String tenCaSiAlbum) {
        this.tenCaSiAlbum = tenCaSiAlbum;
    }

    public String getTenAlbum() {
        return tenAlbum;
    }

    public void setTenAlbum(String tenAlbum) {
        this.tenAlbum = tenAlbum;
    }

    public String getAnhAlbum() {
        return anhAlbum;
    }

    public void setAnhAlbum(String anhAlbum) {
        this.anhAlbum = anhAlbum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(tenAlbum);
        parcel.writeString(tenCaSiAlbum);
        parcel.writeString(anhAlbum);
        parcel.writeString(idAlbum);
    }

    //    @Override
//    public String toString() {
//        return "Album{\n" +
//                "  \"idAlbum\": \"" + idAlbum + "\",\n" +
//                "  \"tenAlbum\": \"" + tenAlbum + "\",\n" +
//                "  \"tenCaSiAlbum\": \"" + tenCaSiAlbum + "\",\n" +
//                "  \"anhAlbum\": \"" + anhAlbum + "\"\n" +
//                "}";
//    }
}

