package com.example.dahitamusic.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dahitamusic.Model.Album;

import java.util.ArrayList;


// Trong ViewModel:
public class AlbumViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Album>> albums;

    public AlbumViewModel() {
        albums = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Album>> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums.setValue(albums);
    }
}


