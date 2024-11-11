package com.example.dahitamusic.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Adapter.SongsAdapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.ActivityDsachBaiHatBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DSachBaiHatActivity extends AppCompatActivity {

    ActivityDsachBaiHatBinding binding;
    Playlist playlist;
    DatabaseReference mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDsachBaiHatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        dataIntent();
        init();
        if(playlist != null && !playlist.getIdPlaylist().equals("")){
            getData(playlist.getIdPlaylist());
        }
    }

    private void getData(String idPlaylist) {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        Query query = mData.orderByChild("idPlaylist").equalTo(idPlaylist);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<BaiHat> baiHatList = new ArrayList<>();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    BaiHat baiHat = datasnapshot.getValue(BaiHat.class);
                    if (baiHat != null) {
                        baiHatList.add(baiHat);
                    }
                }
                Log.d("DSachBaiHatActivity", "Số bài hát đã lấy được: " + baiHatList.size());
                displaySongs(baiHatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi lấy dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displaySongs(ArrayList<BaiHat> baiHatList) {
        if (baiHatList.isEmpty()) {
            Toast.makeText(this, "Không có bài hát nào để hiển thị", Toast.LENGTH_SHORT).show();
        } else {
            RecyclerView recyclerView = binding.recyclerviewDanhsachbaihat;
            SongsAdapter adapter = new SongsAdapter(baiHatList, DSachBaiHatActivity.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(DSachBaiHatActivity.this));
            recyclerView.setAdapter(adapter);
        }
    }



    private void init() {
        setSupportActionBar(binding.toolbardanhsach);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (playlist != null) {
                getSupportActionBar().setTitle(playlist.getTenPlaylist());

                Picasso.get().load(playlist.getAnhPlaylist()).into(binding.imgplaylist);
            }
        }

        binding.collapsingtoolbar.setExpandedTitleColor(Color.BLACK);
        binding.collapsingtoolbar.setCollapsedTitleTextColor(Color.BLACK);
        binding.toolbardanhsach.setNavigationOnClickListener(v -> finish());
    }


    private void dataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("Playlist")) {
                playlist = (Playlist) intent.getSerializableExtra("Playlist");
                Toast.makeText(this, playlist.getIdTheLoai(), Toast.LENGTH_SHORT).show();

            }
        }
    }
}