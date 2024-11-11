package com.example.dahitamusic.Model;

import java.util.HashMap;
import java.util.Map;

public class Album {

    private String tenAlbum;
    private String tenCaSiAlbum;
    private String anhAlbum;

    public Album() {
    }

    public Album(String tenAlbum, String tenCaSiAlbum, String anhAlbum) {
        this.tenAlbum = tenAlbum;
        this.tenCaSiAlbum = tenCaSiAlbum;
        this.anhAlbum = anhAlbum;
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

