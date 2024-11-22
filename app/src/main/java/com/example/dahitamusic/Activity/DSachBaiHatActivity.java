package com.example.dahitamusic.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Adapter.SongsAdapter;
import com.example.dahitamusic.Fragment.HomeFragment;
import com.example.dahitamusic.Fragment.LibaryFragment;
import com.example.dahitamusic.Fragment.ProfileFragment;
import com.example.dahitamusic.Fragment.RadioFragment;
import com.example.dahitamusic.Model.Album;
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
import java.util.Collections;

public class DSachBaiHatActivity extends AppCompatActivity {

    private ActivityDsachBaiHatBinding binding;
    private Playlist playlist;
    private Album album;
    private DatabaseReference mData;
    private ValueEventListener playlistListener, albumListener;

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

        if (playlist != null && !playlist.getIdPlaylist().equals("")) {
            getPlaylist(playlist.getIdPlaylist());
        }
        if (album != null && !album.getIdAlbum().equals("")) {
            getAlbum(album.getIdAlbum());
        }
        clickIconHeart(playlist);
    }


    private void getPlaylist(String idPlaylist) {
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
                eventClick(baiHatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi lấy dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAlbum(String idAlbum) {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        Query query = mData.orderByChild("idAlbum").equalTo(idAlbum);
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
                eventClick(baiHatList);
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
                Picasso.get().load(playlist.getAnhPlaylist()).placeholder(R.drawable.img_default).into(binding.imgplaylist);
            }
            if (album != null) {
                getSupportActionBar().setTitle(album.getTenAlbum());
                Picasso.get().load(album.getAnhAlbum()).placeholder(R.drawable.img_default).into(binding.imgplaylist);
            }
        }
        binding.toolbardanhsach.setNavigationOnClickListener(v -> finish());
    }


    private void dataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("Playlist")) {
                playlist = intent.getParcelableExtra("Playlist");
                Toast.makeText(this, playlist.getIdTheLoai(), Toast.LENGTH_SHORT).show();
            }
            if (intent.hasExtra("Album")) {
                album = (Album) intent.getParcelableExtra("Album");
                Toast.makeText(this, album.getTenAlbum(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void eventClick(ArrayList<BaiHat> baiHatList) {
        if (baiHatList == null || baiHatList.isEmpty()) {
            Toast.makeText(this, "Không có bài hát nào để phát", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnPhatngaynhien.setOnClickListener(view -> {
            Collections.shuffle(baiHatList);
            Intent intent = new Intent(DSachBaiHatActivity.this, PlayMusicActivity.class);
            intent.putExtra("BaiHat", baiHatList);
            startActivity(intent);
        });
    }


    private void clickIconHeart(Playlist playlist) {
        if (playlist != null) {
            mData = FirebaseDatabase.getInstance().getReference("Playlist");
            if (playlist.getYeuThich()) {
                binding.imgLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart_pink));
                binding.imgLike.setColorFilter(Color.parseColor("#F05080"), PorterDuff.Mode.SRC_ATOP);
            } else {
                binding.imgLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart));
                binding.imgLike.clearColorFilter();
            }

            binding.imgLike.setOnClickListener(v -> {
                boolean currentState = playlist.getYeuThich();
                playlist.setYeuThich(!currentState); // Thay đổi trạng thái yêu thích

                if (playlist.getYeuThich()) {
                    binding.imgLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart_pink));
                    binding.imgLike.setColorFilter(Color.parseColor("#F05080"), PorterDuff.Mode.SRC_ATOP);

                    // Hiển thị thông báo: Đã thêm playlist vào thư viện
                    showCenteredToast("Đã thêm playlist vào thư viện");
                } else {
                    binding.imgLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart));
                    binding.imgLike.clearColorFilter();

                    // Hiển thị thông báo: Đã xóa playlist khỏi thư viện
                    showCenteredToast("Đã xóa playlist khỏi thư viện");
                }
            });
        } else {
            Log.e("DSachBaiHatActivity", "Playlist is null in clickIconHeart");
        }
    }

    // Phương thức hiển thị Toast ở giữa màn hình
    private void showCenteredToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0); // Đặt Toast ở giữa màn hình
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mData != null) {
            if (playlistListener != null) {
                mData.removeEventListener(playlistListener);
            }
            if (albumListener != null) {
                mData.removeEventListener(albumListener);
            }
        }
    }
}