package com.example.dahitamusic.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dahitamusic.Adapter.ThuVien_Playlist_Adapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.Model.TheLoai;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.ActivityChuDeTheLoaiBinding;
import com.example.dahitamusic.databinding.ActivityDsachBaiHatBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ChuDeTheLoaiActivity extends AppCompatActivity {
    private ActivityChuDeTheLoaiBinding binding;
    private DatabaseReference mData;
    private ArrayList<Playlist> mListPlaylist;
    private ArrayList<TheLoai> mListTheLoai;
    private ThuVien_Playlist_Adapter playlist_adapter;
    private boolean isExpended = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChuDeTheLoaiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getDataIntent();
        initToolbar();
        initRcyclerView();
        initToolbanAnimation();
//        loadTheloai("chill");
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra("playlist")) {
                // Lấy danh sách bài hát từ Intent
                mListPlaylist = intent.getParcelableArrayListExtra("playlist");
            }

        }
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbarChudevatheloai);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadTheloai(String idTheLoai){
        mData = FirebaseDatabase.getInstance().getReference("TheLoai");
        Query query = mData.orderByChild("idTheLoai").equalTo(idTheLoai);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TheLoai theLoai = dataSnapshot.getValue(TheLoai.class);
                    if (theLoai != null) {
                        mListTheLoai.add(theLoai);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }


    private void initRcyclerView(){
        binding.rcvChuDeTheLoai.setHasFixedSize(true);
        binding.rcvChuDeTheLoai.setLayoutManager(new LinearLayoutManager(this));
        playlist_adapter = new ThuVien_Playlist_Adapter(mListPlaylist, this, new ThuVien_Playlist_Adapter.IClickListner() {

            @Override
            public void onClick(Playlist playlist) {

            }
        });
        binding.rcvChuDeTheLoai.setAdapter(playlist_adapter);
    }

    private void initToolbanAnimation(){
        binding.collapsingtoolbarChudevatheloai.setTitle("Chill/Thư giãn");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_default);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                binding.collapsingtoolbarChudevatheloai.setStatusBarScrimColor(getResources().getColor(R.color.black_trans));
            }
        });

        binding.appbarlayoutChudevatheloai.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(Math.abs(verticalOffset) > 200 ){
                    isExpended = false;
                } else{
                    isExpended = true;
                }
                invalidateOptionsMenu();
            }
        });
    }
}