package com.example.dahitamusic.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dahitamusic.Model.Playlist;

import java.util.ArrayList;


// Trong ViewModel:
public class PlayListViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Playlist>> playlists;

    public PlayListViewModel() {
        playlists = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Playlist>> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists.setValue(playlists);
    }
}


