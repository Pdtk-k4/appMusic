package com.example.dahitamusic.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dahitamusic.Model.BaiHat;

import java.util.ArrayList;

public class BaiHatViewModel extends ViewModel {
    private MutableLiveData<ArrayList<BaiHat>> baihats;

    public BaiHatViewModel() {
        baihats = new MutableLiveData<>();
    }

    public LiveData<ArrayList<BaiHat>> getBaiHats() {
        return baihats;
    }

    public void setBaiHats(ArrayList<BaiHat> baihats) {
        this.baihats.setValue(baihats);
    }
}
