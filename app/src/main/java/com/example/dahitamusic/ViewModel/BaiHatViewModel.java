package com.example.dahitamusic.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dahitamusic.Model.BaiHat;

import java.util.ArrayList;

public class BaiHatViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<BaiHat>> baiHats = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<ArrayList<BaiHat>> getBaiHats() {
        return baiHats;
    }

    public void setBaiHats(ArrayList<BaiHat> baiHatList) {
        baiHats.setValue(baiHatList);
    }
}
