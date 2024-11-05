package com.example.dahitamusic.Model;

import java.util.HashMap;
import java.util.Map;

public class Album {

    private String idAlbum;
    private String tenAlbum;
    private String tenCaSiAlbum;
    private String anhAlbum;

    public Album() {
    }

    public Album(String idAlbum, String tenAlbum, String tenCaSiAlbum, String anhAlbum) {
        this.idAlbum = idAlbum;
        this.tenAlbum = tenAlbum;
        this.tenCaSiAlbum = tenCaSiAlbum;
        this.anhAlbum = anhAlbum;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("idAlbum", idAlbum);
        result.put("tenAlbum", tenAlbum);
        result.put("tenCaSiAlbum", tenCaSiAlbum);
        result.put("anhAlbum", anhAlbum);
        return result;

    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getTenAlbum() {
        return tenAlbum;
    }

    public void setTenAlbum(String tenAlbum) {
        this.tenAlbum = tenAlbum;
    }

    public String getTenCaSiAlbum() {
        return tenCaSiAlbum;
    }

    public void setTenCaSiAlbum(String tenCaSiAlbum) {
        this.tenCaSiAlbum = tenCaSiAlbum;
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

