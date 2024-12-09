package com.example.dahitamusic.Activity;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.dahitamusic.Adapter.DS_Album_Adapter;
import com.example.dahitamusic.Adapter.DS_Playlist_Adapter;
import com.example.dahitamusic.Adapter.Playlist_Adapter;
import com.example.dahitamusic.Model.Album;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.ActivityDsplaylistAndAlbumBinding;

import java.util.ArrayList;


public class DSPlaylistAndAlbumActivity extends AppCompatActivity {
    private ActivityDsplaylistAndAlbumBinding binding;
    private ArrayList<Playlist> playlists;
    private ArrayList<Album> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDsplaylistAndAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        playlists = getIntent().getParcelableArrayListExtra("danhsachplaylist");
        albums = getIntent().getParcelableArrayListExtra("danhsachalbum");

        if (playlists != null) {
            getDataPlaylist();
        }
        if (albums != null) {
            getDataAlbum();
        }

    }

    private void getDataPlaylist() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rcvDanhsachplaylistalbum.setLayoutManager(gridLayoutManager);
        DS_Playlist_Adapter adapterPlaylist = new DS_Playlist_Adapter(playlists);
        binding.rcvDanhsachplaylistalbum.setAdapter(adapterPlaylist);

        toolbarPlaylist();
    }

    private void getDataAlbum() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rcvDanhsachplaylistalbum.setLayoutManager(gridLayoutManager);
        DS_Album_Adapter adapterAlbum = new DS_Album_Adapter(albums);
        binding.rcvDanhsachplaylistalbum.setAdapter(adapterAlbum);

        toolbarAlbum();
    }

    private void toolbarAlbum() {
        setSupportActionBar(binding.toolbarDanhsachplaylistalbum);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Album hot");
        }
        binding.toolbarDanhsachplaylistalbum.setNavigationOnClickListener(v -> {
            finish();
        });
    }


    private void toolbarPlaylist() {
        setSupportActionBar(binding.toolbarDanhsachplaylistalbum);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Có thể bạn muốn nghe");
        }
        binding.toolbarDanhsachplaylistalbum.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}