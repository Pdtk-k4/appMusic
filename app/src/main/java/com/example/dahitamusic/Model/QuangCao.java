package com.example.dahitamusic.Model;

import java.io.Serializable;

public class QuangCao implements Serializable {
    private String anhBaiHat;
    private String idBaiHat;
    private String noiDung;
    private String anhQuangCao;

    public QuangCao() {
    }

    public QuangCao(String anhBaiHat, String idBaiHat, String noiDung, String anhQuangCao) {
        this.anhBaiHat = anhBaiHat;
        this.idBaiHat = idBaiHat;
        this.noiDung = noiDung;
        this.anhQuangCao = anhQuangCao;
    }

    public String getAnhBaiHat() {
        return anhBaiHat;
    }

    public void setAnhBaiHat(String anhBaiHat) {
        this.anhBaiHat = anhBaiHat;
    }

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getAnhQuangCao() {
        return anhQuangCao;
    }

    public void setAnhQuangCao(String anhQuangCao) {
        this.anhQuangCao = anhQuangCao;
    }
}
