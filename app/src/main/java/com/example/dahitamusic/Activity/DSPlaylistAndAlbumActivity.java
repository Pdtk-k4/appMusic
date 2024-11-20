package com.example.dahitamusic.Activity;

import android.os.Bundle;
import android.widget.GridLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.dahitamusic.Adapter.DS_Playlist_Adapter;
import com.example.dahitamusic.Adapter.Playlist_Adapter;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.ActivityDsplaylistAndAlbumBinding;

import java.util.ArrayList;


public class DSPlaylistAndAlbumActivity extends AppCompatActivity {
    private ActivityDsplaylistAndAlbumBinding binding;
    private DS_Playlist_Adapter adapter;
    private ArrayList<Playlist> playlists;

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

        playlists = getIntent().getParcelableArrayListExtra("danhsach");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rcvDanhsachplaylistalbum.setLayoutManager(gridLayoutManager);
        adapter = new DS_Playlist_Adapter(playlists);
        binding.rcvDanhsachplaylistalbum.setAdapter(adapter);

        toolbar();
    }

    private void toolbar() {
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