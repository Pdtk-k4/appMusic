package com.example.dahitamusic.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Podcast implements Parcelable {
    private String idPodcast;
    private String tenPodcast;
    private String anhPodcast;
    private String tacGia;
    private String linkPodcast;

    public Podcast() {
    }

    public Podcast(String idPodcast, String tenPodcast, String anhPodcast, String tacGia, String linkPodcast) {
        this.idPodcast = idPodcast;
        this.tenPodcast = tenPodcast;
        this.anhPodcast = anhPodcast;
        this.tacGia = tacGia;
        this.linkPodcast = linkPodcast;
    }

    protected Podcast(Parcel in) {
        idPodcast = in.readString();
        tenPodcast = in.readString();
        anhPodcast = in.readString();
        tacGia = in.readString();
        linkPodcast = in.readString();
    }

    public static final Creator<Podcast> CREATOR = new Creator<Podcast>() {
        @Override
        public Podcast createFromParcel(Parcel in) {
            return new Podcast(in);
        }

        @Override
        public Podcast[] newArray(int size) {
            return new Podcast[size];
        }
    };

    public String getIdPodcast() {
        return idPodcast;
    }

    public void setIdPodcast(String idPodcast) {
        this.idPodcast = idPodcast;
    }

    public String getTenPodcast() {
        return tenPodcast;
    }

    public void setTenPodcast(String tenPodcast) {
        this.tenPodcast = tenPodcast;
    }

    public String getAnhPodcast() {
        return anhPodcast;
    }

    public void setAnhPodcast(String anhPodcast) {
        this.anhPodcast = anhPodcast;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getLinkPodcast() {
        return linkPodcast;
    }

    public void setLinkPodcast(String linkPodcast) {
        this.linkPodcast = linkPodcast;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idPodcast);
        parcel.writeString(tenPodcast);
        parcel.writeString(anhPodcast);
        parcel.writeString(tacGia);
        parcel.writeString(linkPodcast);
    }
}
