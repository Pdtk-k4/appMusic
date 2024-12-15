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
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
            clickIconHeartPlaylist(playlist);
        }
        if (album != null && !album.getIdAlbum().equals("")) {
            getAlbum(album.getIdAlbum());
            clickIconHeartAlbum(album);
        }
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
                binding.collapsingtoolbar.setTitle(playlist.getTenPlaylist());
                Picasso.get().load(playlist.getAnhPlaylist()).placeholder(R.drawable.img_default).into(binding.imgplaylist);
            }
            if (album != null) {
                binding.collapsingtoolbar.setTitle(album.getTenAlbum());
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
                if (playlist != null) {
                    Toast.makeText(this, playlist.getIdTheLoai(), Toast.LENGTH_SHORT).show();
                }
            }
            if (intent.hasExtra("Album")) {
                album = (Album) intent.getParcelableExtra("Album");
                if (album != null) {
                    Toast.makeText(this, album.getTenAlbum(), Toast.LENGTH_SHORT).show();
                }
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


    private void clickIconHeartPlaylist(Playlist playlist) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference playListYeuThich = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid())
                .child("playListYeuThich");

        // Lắng nghe trạng thái yêu thích của playlist ngay khi mở ứng dụng
        playListYeuThich.child(playlist.getIdPlaylist()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isPlaylistYeuThich = snapshot.exists();
                // Cập nhật giao diện theo trạng thái yêu thích
                if (isPlaylistYeuThich) {
                    binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart_pink));
                } else {
                    binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart));
                }

                // Xử lý sự kiện click vào tim để thêm/xóa playlist
                binding.imgLike.setOnClickListener(v -> {
                    if (isPlaylistYeuThich) {
                        // Nếu playlist đã có trong thư viện yêu thích, xóa nó
                        playListYeuThich.child(playlist.getIdPlaylist()).removeValue((error, ref) -> {
                            if (error == null) {
                                // Cập nhật lại UI ngay lập tức sau khi xóa playlist
                                binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart));
                                showCenteredToast("Đã xóa playlist khỏi thư viện");
                            } else {
                                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi xóa playlist!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Nếu playlist chưa có trong thư viện yêu thích, thêm vào
                        playListYeuThich.child(playlist.getIdPlaylist()).setValue(true, (error, ref) -> {
                            if (error == null) {
                                // Cập nhật lại UI ngay lập tức sau khi thêm playlist
                                binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart_pink));
                                showCenteredToast("Đã thêm playlist vào thư viện");
                            } else {
                                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi thêm playlist!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi tải trạng thái yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickIconHeartAlbum(Album album) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference albumYeuThich = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid())
                .child("albumYeuThich");

        // Lắng nghe trạng thái yêu thích của playlist ngay khi mở ứng dụng
        albumYeuThich.child(album.getIdAlbum()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAlbumYeuThich = snapshot.exists();
                if (isAlbumYeuThich) {
                    binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart_pink));
                } else {
                    binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart));
                }

                // Xử lý sự kiện click vào tim để thêm/xóa playlist
                binding.imgLike.setOnClickListener(v -> {
                    if (isAlbumYeuThich) {
                        albumYeuThich.child(album.getIdAlbum()).removeValue((error, ref) -> {
                            if (error == null) {
                                binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart));
                                showCenteredToast("Đã xóa Album khỏi thư viện");
                            } else {
                                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi xóa Album!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        albumYeuThich.child(album.getIdAlbum()).setValue(true, (error, ref) -> {
                            if (error == null) {
                                binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart_pink));
                                showCenteredToast("Đã thêm Album vào thư viện");
                            } else {
                                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi thêm Album!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi tải trạng thái yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });
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